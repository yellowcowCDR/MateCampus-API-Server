package com.litCitrus.zamongcampusServer.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.litCitrus.zamongcampusServer.domain.report.Report;
import com.litCitrus.zamongcampusServer.domain.report.ReportCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReportRes {
    private final String reportedUserId;
    private final String reportedUserNickname;
    private final String targetUserId;
    private final String targetUserNickname;
    private final String reportContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdDate;

    public ReportRes(Report report){
         this.reportedUserId = report.getReportedUser().getLoginId();
         this.reportedUserNickname = report.getReportedUser().getNickname();
         this.targetUserId = report.getTargetUser().getLoginId();
         this.targetUserNickname = report.getTargetUser().getNickname();

         this.reportContent = report.getReportContent();

         this.createdDate = report.getCreatedAt();
    }
}
