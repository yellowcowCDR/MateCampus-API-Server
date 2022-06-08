package com.litCitrus.zamongcampusServer.service.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.chat.SystemMessageDto;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.exception.voiceRoom.VoiceRoomNotFoundException;
import com.litCitrus.zamongcampusServer.io.agora.AgoraHandler;
import com.litCitrus.zamongcampusServer.io.agora.AgoraRepository;
import com.litCitrus.zamongcampusServer.io.fcm.FCMDto;
import com.litCitrus.zamongcampusServer.io.fcm.FCMHandler;
import com.litCitrus.zamongcampusServer.repository.chat.ChatRoomRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
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

    public VoiceRoomDtoRes.DetailRes createVoiceRoom(
             VoiceRoomDtoReq.Create dto) {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        // 1. participant, chatroom, voiceroom 생성
        Participant participant = Participant.CreateParticipant(Arrays.asList(user), "voice");
        participantRepository.save(participant);
        ChatRoom chatRoom = ChatRoom.createMultiChatRoom(participant);
        chatRoomRepository.save(chatRoom);
        VoiceRoom voiceRoom = VoiceRoom.createVoiceRoom(user, dto, chatRoom);
        voiceRoomRepository.save(voiceRoom);
        // 2. owner의 token 발행
        String token = agoraHandler.getRTCToken(new AgoraRepository(voiceRoom.getChatRoom().getRoomId(), Math.toIntExact(user.getId()), 3600, 2));
        // 3. 초대한 friend에게 fcm 알림 보내기
        List<User> recipients = userRepository.findAllByLoginIdIsIn(dto.getSelectedMemberLoginIds());
        if(!recipients.isEmpty()){
            // FCM dto에 담는게 맞는지. 아니면 voiceroomID만 넘기고 다시 서버에서 값을 한번 더 불러올지.
            FCMDto fcmDto = new FCMDto(user.getNickname() + "님이 만드신 \"" + dto.getTitle() + "\" 방에 참여해보세요!" ,
                    new HashMap<String,String>(){{
                        put("type", chatRoom.getType());
                        put("type", chatRoom.getType());
                    }});
            fcmHandler.sendNotification(fcmDto, "fcm_voiceroom_invite_channel", recipients);
        }
        // 4. dto 반환
        return new VoiceRoomDtoRes.DetailRes(voiceRoom, token, Math.toIntExact(user.getId()));
    }

    public List<VoiceRoomDtoRes.Res> getVoiceRooms(){
        List<VoiceRoom> voiceRooms = voiceRoomRepository.findAll();
        return voiceRooms.stream().map(VoiceRoomDtoRes.Res::new).collect(Collectors.toList());
    }
//
    @Transactional
    public VoiceRoomDtoRes.DetailRes joinAndGetVoiceRoom(Long voiceRoomId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId).orElseThrow(VoiceRoomNotFoundException::new);
        // 1. participant에 추가
        voiceRoom.getChatRoom().getParticipant().addUser(user);
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

        voiceRoom.getChatRoom().getParticipant().removeUser(user);
        // 1. 만약 마지막사람이면 방을 아예 삭제
        if(voiceRoom.getChatRoom().getUsers().isEmpty()){
            voiceRoomRepository.delete(voiceRoom);
        }else{
            // 2. 그게 아니면 stomp으로 exit 보내기 + 방 양도
            VoiceRoomDtoRes.UpdateMemberInfo updateMemberInfo = new VoiceRoomDtoRes.UpdateMemberInfo(user, "exit");
            messagingTemplate.convertAndSend("/sub/chat/room/" + voiceRoom.getChatRoom().getRoomId(), updateMemberInfo);
            if(owner.getLoginId().equals(user.getLoginId())){
                voiceRoom.updateOwner(voiceRoom.getChatRoom().getUsers().get(0));
            }
        }
    }

}
