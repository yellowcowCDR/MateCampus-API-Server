package com.litCitrus.zamongcampusServer.api.report;

import com.litCitrus.zamongcampusServer.domain.report.ReportPost;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.service.report.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report/post")
public class ReportPostApiController {

    private final ReportPostService reportPostService;

    @PostMapping("{postId}")
    public ResponseEntity<ReportRes> reportPost(@Valid @PathVariable Long postId){
        return ResponseEntity.ok(reportPostService.reportPost(postId));
    }
}
