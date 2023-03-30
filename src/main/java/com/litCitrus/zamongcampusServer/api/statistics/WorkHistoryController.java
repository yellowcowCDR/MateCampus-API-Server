package com.litCitrus.zamongcampusServer.api.statistics;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistory;
import com.litCitrus.zamongcampusServer.domain.history.WorkHistoryType;
import com.litCitrus.zamongcampusServer.service.history.WorkHistoryService;
import com.litCitrus.zamongcampusServer.util.IpUtil;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkHistoryController {

    WorkHistoryService workHistoryService;

    @PostMapping("/work-history")
    public void saveWorkHistory(WorkHistoryType.WorkType workType, WorkHistoryType.FunctionType functionType) {
        workHistoryService.saveWorkHistory(workType, functionType);
    }
}
