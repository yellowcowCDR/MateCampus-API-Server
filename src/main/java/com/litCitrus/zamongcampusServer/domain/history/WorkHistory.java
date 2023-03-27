package com.litCitrus.zamongcampusServer.domain.history;

import com.litCitrus.zamongcampusServer.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="WORK_HISTORY")
@Entity
public class WorkHistory {
    @Column(name="HISTORY_ID")
    @Id
    private Long id;

    @Column(name="FUNCTION_CODE")
    @Enumerated(EnumType.STRING)
    private WorkHistoryType.FunctionType functionCode;

    @Column(name="WORK_CODE")
    @Enumerated(EnumType.STRING)
    private WorkHistoryType.WorkType workCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private User workUser;

    @Column(name="WORK_IP")
    private String workIp;

    @Column(name="WORK_DATE")
    private LocalDateTime workDate;
}
