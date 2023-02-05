package com.litCitrus.zamongcampusServer.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
public class UserCollege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private College college;

    @OneToOne
    private Campus campus;

    public UserCollege(College college, Campus campus){
        this.college = college;
        this.campus = campus;
    }

    public static UserCollege createUserCollege(College college, Campus campus){
        return new UserCollege(college, campus);
    }
}
