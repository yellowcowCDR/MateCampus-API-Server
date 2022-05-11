package com.litCitrus.zamongcampusServer.service.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoReq;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.chat.ChatRoomRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    final private UserRepository userRepository;
    final private ParticipantRepository participantRepository;
    final private ChatRoomRepository chatRoomRepository;

    public void createChatRoom(ChatRoomDtoReq chatRoomDto){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User other = userRepository.findByLoginId(chatRoomDto.getOtherLoginId()).orElseThrow(UserNotFoundException::new);
        List<User> users = new ArrayList<User>();
        users.add(user);
        users.add(other);
        Participant participant = Participant.CreateParticipant(users);
        participantRepository.save(participant);
        ChatRoom chatRoom = ChatRoom.createSingleChatRoom(participant);
        chatRoomRepository.save(chatRoom);
        // dto로 뭐가 필요한지 확인.
    }
}
