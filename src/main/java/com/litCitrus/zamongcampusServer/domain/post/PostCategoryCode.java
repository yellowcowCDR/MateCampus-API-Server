package com.litCitrus.zamongcampusServer.domain.post;

import lombok.Getter;

@Getter
public enum PostCategoryCode {
    POSTCATEGORY0001("일상"),
    POSTCATEGORY0002("취업"),
    POSTCATEGORY0003("진로"),
    POSTCATEGORY0004("고민상담"),
    POSTCATEGORY0005("학교생활"),
    POSTCATEGORY0006("연애"),
    POSTCATEGORY0007("토론"),
    POSTCATEGORY0008("맛집"),
    POSTCATEGORY0009("꿀팁"),
    POSTCATEGORY0010("정보"),
    POSTCATEGORY0011("게임");

    private String korName;
    PostCategoryCode(String korName){
        this.korName = korName;
    }
}
