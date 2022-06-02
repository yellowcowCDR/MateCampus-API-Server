package com.litCitrus.zamongcampusServer.domain.user;

import lombok.Getter;

@Getter
public enum MajorCode {
    MAJOR0001("경영학과"),
    MAJOR0002("소프트웨어학"),
    MAJOR0003("산업디자인과"),
    MAJOR0004("컴퓨터공학과");

    private String korName;
    MajorCode(String korName){
        this.korName = korName;
    }
}
