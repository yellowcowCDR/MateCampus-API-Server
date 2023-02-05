package com.litCitrus.zamongcampusServer.domain.user;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class College {
    /* 기본키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 학교명 */
    private String collegeName;

    /* 캠퍼스 */
    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY)
    private List<Campus> campusList;

    public static College createCollege(String collegeName) {
        College college = new College();
        college.collegeName = collegeName;

        return college;
    }
}
