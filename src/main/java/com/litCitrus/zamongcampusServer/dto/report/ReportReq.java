package com.litCitrus.zamongcampusServer.dto.report;

import com.litCitrus.zamongcampusServer.domain.report.ReportCategory;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReportReq {

    @Getter
    @Setter
    public static class Create{
        private String reportedUserId;
        private String targetUserId;
        private String reportContent;
        private ReportCategory reportCategory;
    }

    @Getter
    @Setter
    public static class Search{
        private String reportedUserId;
        private String targetUserId;
        private String reportContent;
        private ReportCategory reportCategory;
        private String createdDateStart;
        private String createdDateEnd;
        private LocalDateTime _createdDateStart;
        private LocalDateTime _createdDateEnd;
    }

}
