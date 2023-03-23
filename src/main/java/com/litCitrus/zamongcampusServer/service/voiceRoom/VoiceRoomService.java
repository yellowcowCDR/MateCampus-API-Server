package com.litCitrus.zamongcampusServer.service.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.chat.ParticipantType;
import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceCategory;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceCategoryCode;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.exception.voiceRoom.VoiceRoomNotFoundException;
import com.litCitrus.zamongcampusServer.io.agora.AgoraHandler;
import com.litCitrus.zamongcampusServer.io.agora.AgoraRepository;
import com.litCitrus.zamongcampusServer.io.fcm.FCMDto;
import com.litCitrus.zamongcampusServer.io.fcm.FCMHandler;
import com.litCitrus.zamongcampusServer.repository.chat.ChatRoomRepository;
import com.litCitrus.zamongcampusServer.repository.notification.NotificationRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.VoiceCategoryRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.VoiceRoomRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoiceRoomService {

    private final VoiceRoomRepository voiceRoomRepository;
    private final ParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final AgoraHandler agoraHandler;
    private final SimpMessageSendingOperations messagingTemplate;
    private final FCMHandler fcmHandler;
    private final VoiceCategoryRepository voiceCategoryRepository;
    private final NotificationRepository notificationRepository;

    public VoiceRoomDtoRes.DetailRes createVoiceRoom(
             VoiceRoomDtoReq.Create dto) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        // 1. participant, chatroom, voiceroom 생성
        Participant participant = Participant.CreateParticipant(user, ParticipantType.VOICE);
        participantRepository.save(participant);
        ChatRoom chatRoom = ChatRoom.createMultiChatRoom(Arrays.asList(participant));
        chatRoomRepository.save(chatRoom);
        List<VoiceCategory> voiceCategories = voiceCategoryRepository.findByVoiceCategoryCodeIsIn(
                dto.getCategoryCodeList().stream().map(categoryCode -> VoiceCategoryCode.valueOf(categoryCode.toUpperCase())).collect(Collectors.toList()));
        VoiceRoom voiceRoom = VoiceRoom.createVoiceRoom(user, dto, chatRoom, voiceCategories);
        voiceRoomRepository.save(voiceRoom);
        // 2. owner의 token 발행
        String token = agoraHandler.getRTCToken(new AgoraRepository(voiceRoom.getChatRoom().getRoomId(), Math.toIntExact(user.getId()), 3600, 2));
        // 3. 초대한 friend에게 fcm 알림 보내기
        sendFcm(user, voiceRoom, dto.getSelectedMemberLoginIds());
        // 4. dto 반환
        return new VoiceRoomDtoRes.DetailRes(voiceRoom, token, Math.toIntExact(user.getId()));
    }

    public List<VoiceRoomDtoRes.Res> getVoiceRooms(){
        List<VoiceRoom> voiceRooms = voiceRoomRepository.findAll();
        return voiceRooms.stream().map(VoiceRoomDtoRes.Res::new).collect(Collectors.toList());
    }

    @Transactional
    public VoiceRoomDtoRes.DetailRes joinAndGetVoiceRoom(Long voiceRoomId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId).orElseThrow(VoiceRoomNotFoundException::new);
        // !! 꽉찼는지 먼저 확인 (최대 8명)
        if(voiceRoom.isFull()){
            return new VoiceRoomDtoRes.DetailRes(voiceRoom);
        }
        // 1. participant에 추가
        Participant participant = Participant.CreateParticipant(user, ParticipantType.VOICE);
        participantRepository.save(participant);
        // 2. stomp으로 이미 참여자들에게 내 정보 실시간 전송
        VoiceRoomDtoRes.UpdateMemberInfo updateMemberInfo = new VoiceRoomDtoRes.UpdateMemberInfo(user, "enter");
        messagingTemplate.convertAndSend("/sub/chat/room/" + voiceRoom.getChatRoom().getRoomId(), updateMemberInfo);
        // 3. token 발행 (user_id)로
        String token = agoraHandler.getRTCToken(new AgoraRepository(voiceRoom.getChatRoom().getRoomId(), Math.toIntExact(user.getId()), 3600, 2));
        // 4. dto 반환
        return new VoiceRoomDtoRes.DetailRes(voiceRoom, token, Math.toIntExact(user.getId()));
    }

    @Transactional
    public void exitVoiceRoom(Long voiceRoomId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId).orElseThrow(VoiceRoomNotFoundException::new);
        User owner = voiceRoom.getOwner();

        participantRepository.deleteVcePctByUserAndChatRoom(user, voiceRoom.getChatRoom());
        // 1. 만약 마지막사람이면 방을 아예 삭제
        if(voiceRoom.getChatRoom().getUsers().isEmpty()){
            voiceRoomRepository.delete(voiceRoom);
        }else{
            // 2. 그게 아니면 방 양도 + stomp으로 exit 보내기
            String newOwnerLoginId = null;
            if(owner.getLoginId().equals(user.getLoginId())){
                User newOwner = voiceRoom.getChatRoom().getUsers().get(0);
                voiceRoom.updateOwner(newOwner);
                newOwnerLoginId = newOwner.getLoginId();
            }
            VoiceRoomDtoRes.UpdateMemberInfo updateMemberInfo = new VoiceRoomDtoRes.UpdateMemberInfo(user, "exit", newOwnerLoginId);
            messagingTemplate.convertAndSend("/sub/chat/room/" + voiceRoom.getChatRoom().getRoomId(), updateMemberInfo);
        }
    }

    public void inviteMembers(Long voiceRoomId, VoiceRoomDtoReq.UpdateInvite dto){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId).orElseThrow(VoiceRoomNotFoundException::new);
        sendFcm(user, voiceRoom, dto.getSelectedMemberLoginIds());
    }

    public void sendFcm(User actor, VoiceRoom voiceRoom, List<String> selectedMemberLoginIds){
        List<User> recipients = userRepository.findAllByLoginIdIsIn(selectedMemberLoginIds);
        if(!recipients.isEmpty()){
            String nickname = actor.getNickname();
            String body = voiceRoom.getTitle();
            String message = "";
            if(body.length() < 18 - nickname.length()){
                message = nickname + "님이 " + "\'" + body + "\'";
            }else{
                message = nickname + "님이 " + "\'" + body.substring(0, 18 - nickname.length()) + "...\'";
            }
            message = message + "\n\uD83C\uDF99음성대화방에 초대했습니다!";
            // ** fcm send와 notificationList에도 값 더하기.
            for(User recipient : recipients){
                Notification newNotification = notificationRepository.save(Notification.CreateVoiceRoomNotification(recipient, voiceRoom, actor));
                FCMDto fcmDto = new FCMDto(message,
                        new HashMap<String,String>(){{
                            put("navigate","/voiceDetail");
                            put("voiceRoomId", voiceRoom.getId().toString());
                            put("notificationId", newNotification.getId().toString());
                            put("validTime", "300"); // 300초가 초대 유효시간?
                        }});
                fcmHandler.sendNotificationOne(fcmDto, "fcm_voiceroom_invite_channel", recipient, null);
            }
        }
    }

}
