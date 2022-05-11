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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    final private UserRepository userRepository;
    final private ParticipantRepository participantRepository;
    final private ChatRoomRepository chatRoomRepository;
    private final SystemMessageComponent systemMessageComponent;

    public ChatRoomDtoRes createChatRoom(ChatRoomDtoReq.Create chatRoomDto){
        /* 1. 채팅방 만들기 */
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User other = userRepository.findByLoginId(chatRoomDto.getOtherLoginId()).orElseThrow(UserNotFoundException::new);
        List<User> members = Arrays.asList(other);
        Participant participant = Participant.CreateParticipant(members);
        participantRepository.save(participant);
        ChatRoom chatRoom = ChatRoom.createSingleChatRoom(participant);
        chatRoomRepository.save(chatRoom);

        /* 2. 채팅방, 채팅방 멤버 정보 전달 */
        /* (CREATE message 실시간 전송 + 본인 제외!! 참여한 user들의 modifiedChatInfo db 저장) */
        systemMessageComponent.sendSaveCreateSystemMessage(chatRoom, members, user);
        return new ChatRoomDtoRes(chatRoom, members, other);
        // dto로 뭐가 필요한지 확인.
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
