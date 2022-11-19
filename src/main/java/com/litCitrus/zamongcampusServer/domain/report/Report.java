package com.litCitrus.zamongcampusServer.domain.report;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User targetUser;

    @Lob
    private String reportContent;

    @Enumerated(EnumType.ORDINAL)
    private ReportCategory reportCategory;
}
