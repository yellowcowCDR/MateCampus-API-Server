package com.litCitrus.zamongcampusServer.api.report;

import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/report")
@RestController
public class ReportApiController {
    @Autowired
    ReportService reportService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addReport(@RequestBody ReportReq.Create reportCreateReq){
        reportService.createReport(reportCreateReq);
    }

    @GetMapping
    public List<ReportRes> getAllReportList(@ModelAttribute ReportReq.Search reportReq){
        return reportService.getAllReportList(reportReq);
    }

    @GetMapping("/profile")
    public List<ReportRes> getProfileReportList(@ModelAttribute ReportReq.Search reportReq){
        return reportService.getProfileReportList(reportReq);
    }

    @GetMapping("/feed")
    public List<ReportRes> getFeedReportList(@ModelAttribute ReportReq.Search reportReq){
        return reportService.getFeedReportList(reportReq);
    }

    @GetMapping("/feedComment")
    public List<ReportRes> getFeedCommentReportList(@ModelAttribute ReportReq.Search reportReq){
        return reportService.getFeedCommentReportList(reportReq);
    }

    @GetMapping("/chatting")
    public List<ReportRes> getChattingReportList(@ModelAttribute ReportReq.Search reportReq){
        return reportService.getChattingReportList(reportReq);
    }


}
