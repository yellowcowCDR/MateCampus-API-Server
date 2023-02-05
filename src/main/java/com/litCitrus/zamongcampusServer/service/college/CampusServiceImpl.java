package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.Campus;
import com.litCitrus.zamongcampusServer.exception.college.CollegeException;
import com.litCitrus.zamongcampusServer.repository.college.CampusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CampusServiceImpl implements CampusService{
    private final CampusRepository campusRepository;

    public Campus getCampus(Long collegeSeq){
        return campusRepository.findByCollegeSeq(collegeSeq).orElseThrow(()->CollegeException.NOT_FOUND);
    }
}
