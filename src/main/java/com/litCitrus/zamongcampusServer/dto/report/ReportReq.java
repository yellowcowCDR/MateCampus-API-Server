package com.litCitrus.zamongcampusServer.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.litCitrus.zamongcampusServer.domain.report.ReportCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDateTime createdDateStart;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDateTime createdDateEnd;
    }

}
