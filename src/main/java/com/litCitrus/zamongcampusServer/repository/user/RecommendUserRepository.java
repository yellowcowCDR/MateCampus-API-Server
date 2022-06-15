package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.user.RecommendUser;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendUserRepository extends JpaRepository<RecommendUser, Long> {

    boolean existsByRecipientAndRecommendedUser(User recipient, User recommendedUser);
    RecommendUser findByRecipientAndRecommendedUser(User recipient, User recommendedUser);
}
