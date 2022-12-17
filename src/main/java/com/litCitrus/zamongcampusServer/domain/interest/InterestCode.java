package com.litCitrus.zamongcampusServer.domain.interest;

import lombok.Getter;

@Getter
public enum InterestCode {
    INTEREST0001("INFP"),
    INTEREST0002("ISTJ"),
    INTEREST0003("ENTP"),
    INTEREST0004("ENFP"),
    INTEREST0005("ISFP"),
    INTEREST0006("INTJ"),
    INTEREST0007("ESTP"),
    INTEREST0008("ENFJ"),
    INTEREST0009("ENTJ"),
    INTEREST0010("INFJ"),
    INTEREST0011("ISTP"),
    INTEREST0012("ESFP"),
    INTEREST0013("ESTJ"),
    INTEREST0014("INTP"),
    INTEREST0015("ISFJ"),
    INTEREST0016("ESFJ"),
    INTEREST0017("운동"),
    INTEREST0018("다이어트"),
    INTEREST0019("연애"),
    INTEREST0020("여행"),
    INTEREST0021("코딩"),
    INTEREST0022("자기계발"),
    INTEREST0023("아르바이트"),
    INTEREST0024("휴학생"),
    INTEREST0025("편입"),
    INTEREST0026("복수전공"),
    INTEREST0027("새내기"),
    INTEREST0028("취업"),
    INTEREST0029("진로"),
    INTEREST0030("공모전"),
    INTEREST0031("스터디"),
    INTEREST0032("주식"),
    INTEREST0033("코인"),
    INTEREST0034("자취"),
    INTEREST0035("반려동물"),
    INTEREST0036("독서"),
    INTEREST0037("요리"),
    INTEREST0038("패션"),
    INTEREST0039("반려식물"),
    INTEREST0040("기숙사"),
    INTEREST0041("학생회"),
    INTEREST0042("동아리"),
    INTEREST0043("게임"),
    INTEREST0044("아이돌"),
    INTEREST0045("케이팝"),
    INTEREST0046("힙합"),
    INTEREST0047("발라드"),
    INTEREST0048("영화"),
    INTEREST0049("애니메이션"),
    INTEREST0050("인터넷방송");


    private String korName;
    InterestCode(String korName){
        this.korName = korName;
    }

}
