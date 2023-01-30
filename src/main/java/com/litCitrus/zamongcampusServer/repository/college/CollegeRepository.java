package com.litCitrus.zamongcampusServer.repository.college;

import com.litCitrus.zamongcampusServer.domain.user.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
    @Override
    Optional<College> findById(Long id);
    Optional<College> findByCollegeSeqAndCollegeName(Long collegeSeq, String collegeName);
}
