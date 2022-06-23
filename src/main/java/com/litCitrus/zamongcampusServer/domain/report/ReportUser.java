package com.litCitrus.zamongcampusServer.domain.report;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private ReportType reportType;

    public static ReportUser CreateReportUser(User reporter, User user, ReportType reportType){
        return ReportUser.builder()
                .reporter(reporter)
                .user(user)
                .reportType(reportType)
                .build();
    }
}