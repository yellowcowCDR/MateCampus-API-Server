package com.litCitrus.zamongcampusServer.domain.user;

import lombok.Getter;

@Getter
public enum CollegeCode {
    COLLEGE0000("빈값"),
    COLLEGE0001("가톨릭대학교"),
    COLLEGE0002("강서대학교"),
    COLLEGE0003("건국대학교"),
    COLLEGE0004("경기대학교"),
    COLLEGE0005("경희대학교"),
    COLLEGE0006("고려대학교"),
    COLLEGE0007("광운대학교"),
    COLLEGE0008("국민대학교"),
    COLLEGE0009("동국대학교"),
    COLLEGE0010("명지대학교"),
    COLLEGE0011("삼육대학교"),
    COLLEGE0012("상명대학교"),
    COLLEGE0013("서강대학교"),
    COLLEGE0014("서울과학기술대학교"),
    COLLEGE0015("서울대학교"),
    COLLEGE0016("서울시립대학교"),
    COLLEGE0017("세종대학교"),
    COLLEGE0018("성균관대학교"),
    COLLEGE0019("성공회대학교"),
    COLLEGE0020("숭실대학교"),
    COLLEGE0021("연세대학교"),
    COLLEGE0022("중앙대학교"),
    COLLEGE0023("총신대학교"),
    COLLEGE0024("추계예술대학교"),
    COLLEGE0025("한국외국어대학교"),
    COLLEGE0026("한국체육대학교"),
    COLLEGE0027("한성대학교"),
    COLLEGE0028("한양대학교"),
    COLLEGE0029("홍익대학교"),
    COLLEGE0030("덕성여자대학교"),
    COLLEGE0031("동덕여자대학교"),
    COLLEGE0032("서울여자대학교"),
    COLLEGE0033("성신여자대학교"),
    COLLEGE0034("숙명여자대학교"),
    COLLEGE0035("이화여자대학교"),
    COLLEGE0036("동양미래대학교"),
    COLLEGE0037("명지전문대학교"),
    COLLEGE0038("삼육보건대학교"),
    COLLEGE0039("서일대학교"),
    COLLEGE0040("인덕대학교"),
    COLLEGE0041("배화여자대학교"),
    COLLEGE0042("서울여자간호대학교"),
    COLLEGE0043("숭의여자대학교"),
    COLLEGE0044("한양여자대학교"),
    COLLEGE0045("서울교육대학교"),
    COLLEGE0046("한국과학기술원"),
    COLLEGE0047("한국예술종합학교"),
    COLLEGE0048("육군사관학교"),
    COLLEGE0049("한국폴리텍대학(서울정수캠)"),
    COLLEGE0050("국제예술대학교"),
    COLLEGE0051("백석예술대학교"),
    COLLEGE0052("정화예술대학교");
    
    private String korName;
    CollegeCode(String korName){
        this.korName = korName;
    }
}
