package com.litCitrus.zamongcampusServer.domain.history;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="WORK_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkHistory {
    @Column(name="ID")
    @Id
    private Long id;

    @Column(name="FUNCTION_TYPE")
    @Enumerated(EnumType.STRING)
    private WorkHistoryType.FunctionType functionType;

    @Column(name="WORK_TYPE")
    @Enumerated(EnumType.STRING)
    private WorkHistoryType.WorkType workType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User workUser;

    @Column(name="WORK_IP")
    private String workIp;

    @Column(name="WORK_DATE")
    private LocalDateTime workDate;

    public static WorkHistory createWorkHistory(WorkHistoryType.WorkType workType, WorkHistoryType.FunctionType functionType, User user, String workIp) {
        WorkHistory w = new WorkHistory();
        w.functionType = functionType;
        w.workType  = workType;
        w.workUser = user;
        w.workIp = workIp;
        w.workDate = LocalDateTime.now();
        return w;
    }
}
