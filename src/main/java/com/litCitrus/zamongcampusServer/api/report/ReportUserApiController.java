package com.litCitrus.zamongcampusServer.api.report;

import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.service.report.ReportUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report/user")
public class ReportUserApiController {

    private final ReportUserService reportUserService;

    @PostMapping("{loginId}")
    public ResponseEntity<ReportRes> reportUser(@Valid @PathVariable String loginId, @Valid @RequestBody ReportReq dto){
        return ResponseEntity.ok(reportUserService.reportUser(loginId, dto));
    }
}
