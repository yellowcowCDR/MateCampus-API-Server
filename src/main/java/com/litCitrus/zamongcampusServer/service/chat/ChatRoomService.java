package com.litCitrus.zamongcampusServer.service.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.chat.ParticipantType;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    final private UserRepository userRepository;
    final private ParticipantRepository participantRepository;
    final private ChatRoomRepository chatRoomRepository;
    private final SystemMessageComponent systemMessageComponent;

    public ChatRoomDtoRes createOrGetChatRoom(ChatRoomDtoReq.Create chatRoomDto){
        User user = SecurityUtil.getUser(userRepository);
        User other = userRepository.findByLoginId(chatRoomDto.getOtherLoginId()).orElseThrow(UserNotFoundException::new);
        List<User> members = Arrays.asList(user, other);

        List<Participant> newParticipants = Arrays.asList(Participant.CreateParticipant(user, ParticipantType.CHAT), Participant.CreateParticipant(other, ParticipantType.CHAT));
        chatRoomRepository.fetchUserByParticipants(members);
        List<ChatRoom> chatRooms = chatRoomRepository.fetchUserByParticipants(members).stream().filter(c -> c.getParticipants().size() == 2).collect(Collectors.toList());

        /// case 1) 이미 존재하면 get
        if(!chatRooms.isEmpty()){
            return new ChatRoomDtoRes(chatRooms.get(0), members, user);
        }

        /// case 2) 존재하지 않으면 create
        /* 1. 채팅방 만들기 */
        ChatRoom chatRoom = ChatRoom.createSingleChatRoom();
        chatRoomRepository.save(chatRoom);
        newParticipants.stream().forEach(p -> p.configChatRoom(chatRoom));
        participantRepository.saveAll(newParticipants);

        /* 2. 채팅방, 채팅방 멤버 정보 전달 */
        /* (CREATE message 실시간 전송 + 본인 제외!! 참여한 user들의 modifiedChatInfo db 저장) */
        systemMessageComponent.sendSaveCreateSystemMessage(chatRoom, members, user, other);
        return new ChatRoomDtoRes(chatRoom, members, user);
    }

    @Transactional
    public void enterChatRoom(Long chatRoomId){
        /* 1. 멤버 추가 */
        //ToDo 로그인된 유저 정보 가져오는 방법 수정
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByLoginId).orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.addUser(user);

        /* 2. Enter 실시간 전송 + 메세지 저장 */
        systemMessageComponent.sendSaveEnterSystemMessage(user, chatRoom);
    }

    /*@Transactional
    public void exitChatRoom(Long chatRoomId){
        *//* 1. 멤버 삭제 *//*
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.deleteUser(user);

        *//* 2. exit 실시간 전송 + 메세지 저장 *//*
        systemMessageComponent.sendSaveExitSystemMessage(user, chatRoom);
    }*/

    @Transactional
    public void exitChatRoom(String roomId){
        //ToDo 로그인된 유저 정보 가져오는 방법 수정
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByLoginId).orElseThrow(UserNotFoundException::new);
        //참석자 삭제
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.deleteUser(user);


        systemMessageComponent.sendSaveExitSystemMessage(user, chatRoom);
    }
}
