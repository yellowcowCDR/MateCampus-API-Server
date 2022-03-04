package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.user.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findByUser(User user);

    List<UserInterest> findAllByInterestContaining(Interest interest);

    List<UserInterest> findByInterest(Interest interest);

}
