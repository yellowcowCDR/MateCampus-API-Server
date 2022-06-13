package com.litCitrus.zamongcampusServer.dto.report;

public enum ReportStatus {

    DUPLICATE("이미 신고되었습니다"),
    SUCCESS("신고 성공");

    private final String korName;
    ReportStatus(String korName){
        this.korName = korName;
    }
}