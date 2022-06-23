package com.litCitrus.zamongcampusServer.dto.report;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReportReq {

    @NotBlank(message = "신고 타입이 비워있습니다")
    private String reportType;
}
