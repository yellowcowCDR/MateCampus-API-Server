package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.Campus;
import com.litCitrus.zamongcampusServer.domain.user.College;
import com.litCitrus.zamongcampusServer.dto.college.CollegeResDto;
import com.litCitrus.zamongcampusServer.exception.college.CollegeException;
import com.litCitrus.zamongcampusServer.repository.college.CampusRepository;
import com.litCitrus.zamongcampusServer.repository.college.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CampusServiceImpl implements CampusService{
    private final CampusApiService campusApiService;

    private final CollegeRepository collegeRepository;

    private final CampusRepository campusRepository;

    public Campus getCampus(Long collegeSeq){
        return campusRepository.fetchCampusByCollegeSeq(collegeSeq).orElseThrow(()->CollegeException.NOT_FOUND);
    }

    @Override
    public Campus searchCampus(Long collegeSeq, String collegeName) {
        //DB에서 캠퍼스 조회(seq)
        Campus campus = campusRepository.fetchCampusByCollegeSeq(collegeSeq).orElseGet(()->{
            //캠퍼스 없을 경우
            //학교 Open API 조회
            CollegeResDto newCampusInfo = campusApiService.searchCampus(collegeSeq, collegeName).orElseThrow(()->CollegeException.NOT_FOUND);
            //DB에서 학교조회
            College college = collegeRepository.findByCollegeName(collegeName).orElseGet(()->{
                //학교 없을 경우
                //학교정보 저장
                College newCollege = College.createCollege(newCampusInfo.getCollegeName());
                collegeRepository.save(newCollege);

                return newCollege;
            });
            //캠퍼스 정보저장
            Campus newCampus = Campus.createCampus(newCampusInfo.getCollegeSeq(), newCampusInfo.getCampusName(), newCampusInfo.getAddress(), college);
            campusRepository.save(newCampus);

            return newCampus;
        });

        //조회한 campus의 college 조회
        College college = campus.getCollege();
        //조회된 캠퍼스의 대학교와 요청된 대학교 이름 일치여부 확인
        //일치할 경우
        if(college.getCollegeName().equals(collegeName)) {
            //조회된 캠퍼스와 학교 정보 반환
            return campus;
        }
        //불일치할 경우
        else {
            //Exception 발생
            throw CollegeException.NOT_MATCHED;
        }
    }
}
