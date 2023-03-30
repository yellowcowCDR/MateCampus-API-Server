package com.litCitrus.zamongcampusServer.api.statistics;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistoryType;
import com.litCitrus.zamongcampusServer.dto.history.WorkHistoryReqDto;
import com.litCitrus.zamongcampusServer.dto.history.WorkHistoryResDto;
import com.litCitrus.zamongcampusServer.service.history.WorkHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkHistoryController {

    private final WorkHistoryService workHistoryService;

    @PostMapping("/work-history")
    public void saveWorkHistory(WorkHistoryType.WorkType workType, WorkHistoryType.FunctionType functionType) {
        workHistoryService.saveWorkHistory(workType, functionType);
    }

    @GetMapping("/work-history")
    public List<WorkHistoryResDto> getWorkHistory(WorkHistoryReqDto workHistoryReq){
        return workHistoryService.getWorkHistory(workHistoryReq);
    }
}
