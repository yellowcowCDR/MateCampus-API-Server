package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.College;

import java.util.Optional;

public interface CollegeApiService {
    Optional<College> searchCollege(Long collegeSeq, String collegeName);
}
