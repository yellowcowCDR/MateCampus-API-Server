package com.litCitrus.zamongcampusServer.exception.report;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReportCategoryNotFoundException extends BusinessException {
    public ReportCategoryNotFoundException(){
        super(HttpStatus.NOT_FOUND, "U001", "신고 카테고리를 찾을 수 없습니다.");
    }
}
