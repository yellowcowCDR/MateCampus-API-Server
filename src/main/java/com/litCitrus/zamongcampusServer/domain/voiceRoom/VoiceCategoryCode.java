package com.litCitrus.zamongcampusServer.domain.voiceRoom;

import lombok.Getter;

@Getter
public enum VoiceCategoryCode {
    VOICECATEGORY0001("친목/수다"),
    VOICECATEGORY0002("취업/진로"),
    VOICECATEGORY0003("고민상담"),
    VOICECATEGORY0004("학교생활"),
    VOICECATEGORY0005("연애"),
    VOICECATEGORY0006("토론"),
    VOICECATEGORY0007("기타");

    private String korName;
    VoiceCategoryCode(String korName){
        this.korName = korName;
    }
}
