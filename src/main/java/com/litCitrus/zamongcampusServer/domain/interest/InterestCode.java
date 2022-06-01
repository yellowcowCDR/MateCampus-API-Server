package com.litCitrus.zamongcampusServer.domain.interest;

import lombok.Getter;

@Getter
public enum InterestCode {
    INTEREST0001("친목/일상"),
    INTEREST0002("문화"),
    INTEREST0003("암호화폐"),
    INTEREST0004("워홀"),
    INTEREST0005("진로상담"),
    INTEREST0006("한달살기"),
    INTEREST0007("게임"),
    INTEREST0008("오버워치"),
    INTEREST0009("배그"),
    INTEREST0010("연애"),
    INTEREST0011("맛집투어"),
    INTEREST0012("카페투어"),
    INTEREST0013("치느님"),
    INTEREST0014("다이어트"),
    INTEREST0015("공부"),
    INTEREST0016("미술/예술");

    private String korName;
    InterestCode(String korName){
        this.korName = korName;
    }

}
