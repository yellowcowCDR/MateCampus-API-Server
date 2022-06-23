package com.litCitrus.zamongcampusServer.domain.report;

import lombok.Getter;

@Getter
public enum ReportType {
    REPORT0000(""),
    REPORT0001("욕설 및 폭력적 언어 사용"),
    REPORT0002("개인 정보 유출 및 도용"),
    REPORT0003("음란성 및 불건전한 만남 유도"),
    REPORT0004("분란 조장 및 혐오 표현 사용"),
    REPORT0005("상업적 광고 및 판매"),
    REPORT0006("낚시 및 도배성 글 작성");


    private String korName;
    ReportType(String korName){
        this.korName = korName;
    }
}
