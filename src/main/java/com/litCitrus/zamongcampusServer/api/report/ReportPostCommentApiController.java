package com.litCitrus.zamongcampusServer.api.report;

import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.service.report.ReportPostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report/postComment")
public class ReportPostCommentApiController {

    private final ReportPostCommentService reportPostCommentService;

    @PostMapping("{postCommentId}")
    public ResponseEntity<ReportRes> reportPostComment(@Valid @PathVariable Long postCommentId){
        return ResponseEntity.ok(reportPostCommentService.reportPostComment(postCommentId));
    }
 }
