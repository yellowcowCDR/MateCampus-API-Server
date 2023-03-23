package com.litCitrus.zamongcampusServer.repository.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByParticipants_User(User user);

    @Query("select c from ChatRoom c inner join c.participants p on p.chatRoom = c inner join p.user u on u = p.user  where u in :users")
    List<ChatRoom> fetchUserByParticipants(List<User> users);
    Optional<ChatRoom> findByRoomId(String roomId);
}
