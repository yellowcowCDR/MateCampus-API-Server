package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.College;
import com.litCitrus.zamongcampusServer.exception.college.CollegeException;
import com.litCitrus.zamongcampusServer.repository.college.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements CollegeService{

    private final CollegeApiService collegeApiService;

    private final CollegeRepository collegeRepository;

    @Override
    public College searchCollege(Long collegeSeq, String collegeName) {
        return collegeRepository.findByCollegeSeqAndCollegeName(collegeSeq, collegeName).orElseGet(()->{
            College college=collegeApiService.searchCollege(collegeSeq, collegeName).orElseThrow(()->CollegeException.NOT_FOUND);
            collegeRepository.save(college);
            return college;
        });
    }
}
