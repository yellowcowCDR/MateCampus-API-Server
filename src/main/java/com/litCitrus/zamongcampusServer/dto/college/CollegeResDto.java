package com.litCitrus.zamongcampusServer.dto.college;

import com.litCitrus.zamongcampusServer.domain.user.Campus;
import com.litCitrus.zamongcampusServer.domain.user.College;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CollegeResDto {
    private Long collegeSeq;
    private String collegeName;
    private String campusName;
    private String address;

    public static CollegeResDto createCollegeDto(Long collegeSeq, String collegeName, String campusName, String address){
        return CollegeResDto.builder()
                .collegeSeq(collegeSeq)
                .collegeName(collegeName)
                .campusName(campusName)
                .address(address)
                .build();
    }
    public static CollegeResDto createCollegeDto(College college, Campus campus){
        return CollegeResDto.builder()
                .collegeSeq(campus.getCollegeSeq())
                .collegeName(college.getCollegeName())
                .campusName(campus.getName())
                .address(campus.getAddress())
                .build();
    }
}
