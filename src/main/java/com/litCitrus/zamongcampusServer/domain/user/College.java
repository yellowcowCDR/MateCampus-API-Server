package com.litCitrus.zamongcampusServer.domain.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class College {
    /* 기본키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 학교 Sequence Number */
    private Long collegeSeq;

    /* 학교명 */
    private String collegeName;

    /* 캠퍼스명 */
    private String campusName;

    /* 학교주소 */
    private String address;

    public static College createCollege(Long collegeSeq, String collegeName, String campusName, String address){
        College college = new College();
        college.collegeSeq = collegeSeq;
        college.collegeName = collegeName;
        college.campusName = campusName;
        college.address = address;

        return college;
    }
}
