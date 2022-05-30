package com.litCitrus.zamongcampusServer.repository.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByParticipant_Users(User user);
    boolean existsByParticipant(Participant participant);
    ChatRoom findByParticipant(Participant participant);
    Optional<ChatRoom> findByRoomId(String roomId);
}
