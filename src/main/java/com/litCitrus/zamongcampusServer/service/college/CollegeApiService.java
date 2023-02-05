package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.dto.college.CollegeResDto;

import java.util.Optional;

public interface CollegeApiService {
    Optional<CollegeResDto> searchCollege(Long collegeSeq, String collegeName);
}
