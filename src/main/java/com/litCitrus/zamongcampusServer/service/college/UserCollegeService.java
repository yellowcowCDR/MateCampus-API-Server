package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.UserCollege;

public interface UserCollegeService {
    UserCollege getUserCollege(Long Id);

    void save(UserCollege userCollege);
}
