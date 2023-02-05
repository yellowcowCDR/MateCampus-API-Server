package com.litCitrus.zamongcampusServer.repository.college;

import com.litCitrus.zamongcampusServer.domain.user.UserCollege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollegeRepository extends JpaRepository<UserCollege, Long> {
}
