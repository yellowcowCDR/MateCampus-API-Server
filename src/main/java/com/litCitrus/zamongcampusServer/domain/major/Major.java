package com.litCitrus.zamongcampusServer.domain.major;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long majorSeq;

    public static Major createMajor(long majorSeq, String name) {
        final Major major = new Major();
        major.majorSeq = majorSeq;
        major.name = name;
        return major;
    }
}
