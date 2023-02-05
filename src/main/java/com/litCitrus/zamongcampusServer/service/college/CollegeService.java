package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.dto.college.CollegeResDto;

public interface CollegeService {
    CollegeResDto searchCollege(Long collegeSeq, String collegeName);
}
