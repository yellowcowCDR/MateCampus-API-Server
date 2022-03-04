package com.litCitrus.zamongcampusServer.repository.interest;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

}
