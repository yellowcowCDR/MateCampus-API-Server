package com.litCitrus.zamongcampusServer.domain.report;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static ReportPost CreateReportPost(User reporter, Post post){
        return ReportPost.builder()
                .reporter(reporter)
                .post(post)
                .build();
    }
}
