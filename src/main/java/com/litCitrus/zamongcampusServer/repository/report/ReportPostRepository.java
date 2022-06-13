package com.litCitrus.zamongcampusServer.repository.report;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.report.ReportPost;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {

    int countAllByPost(Post post);
    boolean existsByReporterAndPost(User reporter, Post post);
}
