package com.litCitrus.zamongcampusServer.dto.report;

import com.litCitrus.zamongcampusServer.domain.report.Report;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class ReportRes {
    private final String reportedUserId;
    private final String reportedUserNickname;
    private final String targetUserId;
    private final String targetUserNickname;
    private final String reportContent;
    private final String createdDate;
    private final String category;

    public ReportRes(Report report){
         reportedUserId = report.getReportedUser().getLoginId();
         reportedUserNickname = report.getReportedUser().getNickname();
         targetUserId = report.getTargetUser().getLoginId();
         targetUserNickname = report.getTargetUser().getNickname();
         reportContent = report.getReportContent();
         createdDate = report.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE);
         category = report.getReportCategory().getName();
    }
}
