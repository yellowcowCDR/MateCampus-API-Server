package com.litCitrus.zamongcampusServer.repository.report;

import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.report.ReportPostComment;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostCommentRepository extends JpaRepository<ReportPostComment, Long> {

    int countAllByPostComment(PostComment postComment);
    boolean existsByReporterAndPostComment(User reporter, PostComment postComment);
}
