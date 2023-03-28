package com.litCitrus.zamongcampusServer.domain.history;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="WORK_HISTORY")
@Entity
public class WorkHistory {
    @Column(name="HISTORY_ID")
    @Id
    private Long id;

    @Column(name="FUNCTION_CODE")
    @Enumerated(EnumType.STRING)
    private WorkHistoryType.FunctionType functionType;

    @Column(name="WORK_CODE")
    @Enumerated(EnumType.STRING)
    private WorkHistoryType.WorkType workType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private User workUser;

    @Column(name="WORK_IP")
    private String workIp;

    @Column(name="WORK_DATE")
    private LocalDateTime workDate;
}
