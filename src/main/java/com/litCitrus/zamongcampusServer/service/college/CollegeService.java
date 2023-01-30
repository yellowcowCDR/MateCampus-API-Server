package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.College;

public interface CollegeService {
    public College searchCollege(Long collegeSeq, String collegeName);
}
