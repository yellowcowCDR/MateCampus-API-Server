package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.user.BlockedUser;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    List<BlockedUser> findByRequestedUser(User requestedUser);
    Optional<BlockedUser> findByRequestedUserAndBlockedUser(User requestedUser, User blockedUser);
    Boolean existsByRequestedUserAndBlockedUser(User requestedUser, User blockedUser);
}
