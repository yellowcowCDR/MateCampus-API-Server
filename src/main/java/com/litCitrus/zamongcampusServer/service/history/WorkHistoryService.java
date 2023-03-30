package com.litCitrus.zamongcampusServer.service.history;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistory;
import com.litCitrus.zamongcampusServer.domain.history.WorkHistoryType;
import com.litCitrus.zamongcampusServer.repository.history.WorkHistoryRepository;
import com.litCitrus.zamongcampusServer.util.IpUtil;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkHistoryService {

    private final WorkHistoryRepository workHistoryRepository;

    public void saveWorkHistory(WorkHistoryType.WorkType workType, WorkHistoryType.FunctionType functionType) {
        workHistoryRepository.save(WorkHistory.createWorkHistory(workType, functionType, SecurityUtil.getUser(), IpUtil.getClientIP()));
    }
}
