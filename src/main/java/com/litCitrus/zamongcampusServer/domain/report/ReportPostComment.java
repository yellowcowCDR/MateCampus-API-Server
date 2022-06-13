package com.litCitrus.zamongcampusServer.domain.report;

import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment postComment;

    public static ReportPostComment CreateReportComment(User reporter, PostComment postComment){
        return ReportPostComment.builder()
                .reporter(reporter)
                .postComment(postComment)
                .build();
    }
}
