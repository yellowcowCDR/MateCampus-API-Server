package com.litCitrus.zamongcampusServer.service.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoRes;
import com.litCitrus.zamongcampusServer.exception.chat.ChatRoomNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.chat.ChatRoomRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    final private UserRepository userRepository;
    final private ParticipantRepository participantRepository;
    final private ChatRoomRepository chatRoomRepository;
    private final SystemMessageComponent systemMessageComponent;

    public ChatRoomDtoRes createOrGetChatRoom(ChatRoomDtoReq.Create chatRoomDto){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User other = userRepository.findByLoginId(chatRoomDto.getOtherLoginId()).orElseThrow(UserNotFoundException::new);
        List<User> members = Arrays.asList(user, other);
        members.sort(Comparator.comparingLong(User::getId));
        Participant participant = participantRepository.findByUsersIn(members);
        // user와 other 두 명만 가지고 있는 채팅방을 찾아야한다. => TODO: 테스트 필요(2명+1명 있는 방도 가져오는지 등)
        /// case 1) 이미 존재하면 get
        if(participant != null){
            return new ChatRoomDtoRes(chatRoomRepository.findByParticipant(participant), members, other);
        }

        /// case 2) 존재하지 않으면 create
        /* 1. 채팅방 만들기 */
        Participant newParticipant = Participant.CreateParticipant(members);
        participantRepository.save(newParticipant);
        ChatRoom chatRoom = ChatRoom.createSingleChatRoom(newParticipant);
        chatRoomRepository.save(chatRoom);

        /* 2. 채팅방, 채팅방 멤버 정보 전달 */
        /* (CREATE message 실시간 전송 + 본인 제외!! 참여한 user들의 modifiedChatInfo db 저장) */
        systemMessageComponent.sendSaveCreateSystemMessage(chatRoom, members, user, other);
        return new ChatRoomDtoRes(chatRoom, members, user);
    }

    @Transactional
    public void enterChatRoom(Long chatRoomId){
        /* 1. 멤버 추가 */
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.addUser(user);

        /* 2. Enter 실시간 전송 + 메세지 저장 */
        systemMessageComponent.sendSaveEnterSystemMessage(user, chatRoom);
    }

    @Transactional
    public void exitChatRoom(Long chatRoomId){
        /* 1. 멤버 삭제 */
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.deleteUser(user);

        /* 2. exit 실시간 전송 + 메세지 저장 */
        systemMessageComponent.sendSaveExitSystemMessage(user, chatRoom);
    }
}
