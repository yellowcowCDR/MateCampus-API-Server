package com.litCitrus.zamongcampusServer.repository.major;

import com.litCitrus.zamongcampusServer.domain.major.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByMajorSeqAndName(long majorSeq, String name);
}
