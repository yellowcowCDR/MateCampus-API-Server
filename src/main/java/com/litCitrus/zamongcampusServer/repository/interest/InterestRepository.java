package com.litCitrus.zamongcampusServer.repository.interest;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.interest.InterestCode;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByInterestCode(InterestCode interestCode);

    List<Interest> findAllByUserInterests_User(User user);
}
