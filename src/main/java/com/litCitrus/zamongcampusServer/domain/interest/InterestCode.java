package com.litCitrus.zamongcampusServer.domain.interest;

import lombok.Getter;

@Getter
public enum InterestCode {
    INTEREST0001("mbti과몰입"),
    INTEREST0002("갓생살기"),
    INTEREST0003("카페투어"),
    INTEREST0004("술자리"),
    INTEREST0005("혼코노"),

    INTEREST0006("헬스/운동"),
    INTEREST0007("유튜브"),
    INTEREST0008("한강나들이"),
    INTEREST0009("넷플릭스"),
    INTEREST0010("패션/쇼핑"),

    INTEREST0011("혼술"),
    INTEREST0012("인스타그램"),
    INTEREST0013("여행"),
    INTEREST0014("사진찍기"),
    INTEREST0015("틱톡"),

    INTEREST0016("맛집투어"),
    INTEREST0017("연애고수"),
    INTEREST0018("핵인싸"),
    INTEREST0019("연애하수"),
    INTEREST0020("카공"),

    INTEREST0021("미팅/과팅"),
    INTEREST0022("콘서트"),
    INTEREST0023("웹드라마"),
    INTEREST0024("동네친구"),
    INTEREST0025("동아리"),

    INTEREST0026("자취"),
    INTEREST0027("비트코인"),
    INTEREST0028("주식"),
    INTEREST0029("패알못"),
    INTEREST0030("퍼스널컬러"),

    INTEREST0031("취업/진로"),
    INTEREST0032("랩/힙합"),
    INTEREST0033("발라드"),
    INTEREST0034("영화"),
    INTEREST0035("드라마"),

    INTEREST0036("스트릿댄스"),
    INTEREST0037("1일1코노"),
    INTEREST0038("다꾸"),
    INTEREST0039("바디프로필"),
    INTEREST0040("아이돌"),

    INTEREST0041("학점관리"),
    INTEREST0042("봉사활동"),
    INTEREST0043("코딩"),
    INTEREST0044("브이로그"),
    INTEREST0045("냥집사"),

    INTEREST0046("대외활동"),
    INTEREST0047("다이어트"),
    INTEREST0048("댕댕이"),
    INTEREST0049("반려동물"),
    INTEREST0050("LoL(롤)"),

    INTEREST0051("꾸안꾸"),
    INTEREST0052("EPL/축구"),
    INTEREST0053("배그"),
    INTEREST0054("반려식물"),
    INTEREST0055("메이플"),

    INTEREST0056("서든어택"),
    INTEREST0057("로스트아크"),
    INTEREST0058("피파온라인4"),
    INTEREST0059("산책"),
    INTEREST0060("요리왕"),

    INTEREST0061("학생회"),
    INTEREST0062("자발적아싸"),
    INTEREST0063("미술/예술"),
    INTEREST0064("필라테스");

    private String korName;
    InterestCode(String korName){
        this.korName = korName;
    }

}
