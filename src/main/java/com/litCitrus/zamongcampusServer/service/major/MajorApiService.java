package com.litCitrus.zamongcampusServer.service.major;

import com.litCitrus.zamongcampusServer.domain.major.Major;

import java.util.Optional;

public interface MajorApiService {
    Optional<Major> searchMajor(int majorSeq, String mName);
}
