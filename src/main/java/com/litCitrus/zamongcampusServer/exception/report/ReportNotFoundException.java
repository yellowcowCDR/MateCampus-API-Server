package com.litCitrus.zamongcampusServer.exception.report;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReportNotFoundException extends BusinessException {
    public ReportNotFoundException(){
        super(HttpStatus.NOT_FOUND, "U001", "신고내역을 찾을 수 없습니다.");
    }
}
