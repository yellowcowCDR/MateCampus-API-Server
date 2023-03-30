package com.litCitrus.zamongcampusServer.dto.history;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistoryType;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class WorkHistoryResDto {
    private String workDate;
    private User workUser;
    private WorkHistoryType.FunctionType functionType;
    private WorkHistoryType.WorkType workType;
    private Long workCount;

    public WorkHistoryResDto(String workDate, WorkHistoryType.FunctionType functionType, WorkHistoryType.WorkType workType, Long workCount){
        this.workDate = workDate;
        this.functionType = functionType;
        this.workType = workType;
        this.workCount = workCount;
    }
}
