package com.litCitrus.zamongcampusServer.repository.college;

import com.litCitrus.zamongcampusServer.domain.user.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    @Query("SELECT c FROM Campus c join fetch c.college" +
            " WHERE c.collegeSeq = :collegeSeq")
    Optional<Campus> findByCollegeSeq(Long collegeSeq);
}
