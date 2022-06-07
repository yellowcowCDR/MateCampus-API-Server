package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend findByRequestorAndRecipient(User requestor, User recipient);
    // 두 user 값을 동일인물에 넣으면 해당 인물의 모든 친구가 나온다.
    List<Friend> findByRequestorOrRecipient(User requestor, User recipient);
    //
}
