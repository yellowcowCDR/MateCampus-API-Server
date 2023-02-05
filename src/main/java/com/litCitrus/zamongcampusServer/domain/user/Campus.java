package com.litCitrus.zamongcampusServer.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Campus {
    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 캠퍼스 이름 */
    private String name;

    /** 학교주소 */
    private String address;

    /** 학교 Sequence Number */
    private Long collegeSeq;

    /** 학교 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="college_id")
    private College college;

    public static Campus createCampus(Long collegeSeq, String campusName, String address, College college){
        return Campus.builder()
                .name(campusName)
                .collegeSeq(collegeSeq)
                .address(address)
                .college(college)
                .build();
    }
}
