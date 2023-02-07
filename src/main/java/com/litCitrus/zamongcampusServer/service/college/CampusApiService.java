package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.dto.college.CollegeResDto;

import java.util.Optional;

public interface CampusApiService {
    Optional<CollegeResDto> searchCampus(Long collegeSeq, String collegeName);
}
