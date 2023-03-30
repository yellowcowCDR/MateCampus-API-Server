package com.litCitrus.zamongcampusServer.dto.history;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class WorkHistoryReqDto {
   private String loginId;
    private WorkHistoryType.WorkType workType;
    private WorkHistoryType.FunctionType functionType;
}
