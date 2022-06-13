package com.litCitrus.zamongcampusServer.dto.report;

import lombok.Getter;

@Getter
public class ReportRes {
    private final ReportStatus reportStatus;

    public ReportRes(ReportStatus reportStatus){
        this.reportStatus = reportStatus;
    }
}
