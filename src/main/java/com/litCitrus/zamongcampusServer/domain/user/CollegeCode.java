package com.litCitrus.zamongcampusServer.domain.user;

import lombok.Getter;

@Getter
public enum CollegeCode {
    COLLEGE0000("빈값"),
    COLLEGE0001("단국대학교"),
    COLLEGE0002("건국대학교"),
    COLLEGE0003("동국대학교"),
    COLLEGE0004("홍익대학교");

    private String korName;
    CollegeCode(String korName){
        this.korName = korName;
    }
}
