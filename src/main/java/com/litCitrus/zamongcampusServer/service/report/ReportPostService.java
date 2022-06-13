package com.litCitrus.zamongcampusServer.service.report;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.report.ReportPost;
import com.litCitrus.zamongcampusServer.domain.report.ReportUser;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.dto.report.ReportStatus;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.report.ReportPostRepository;
import com.litCitrus.zamongcampusServer.repository.report.ReportUserRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportPostRepository reportPostRepository;
    private final ReportUserRepository reportUserRepository;

    @Transactional
    public ReportRes reportPost(Long postId){
        User reporter = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User reportedUser = post.getUser();
        if(reportPostRepository.existsByReporterAndPost(reporter, post)) return new ReportRes(ReportStatus.DUPLICATE);
        reportPostRepository.save(ReportPost.CreateReportPost(reporter, post));
        if(reportPostRepository.countAllByPost(post) >= 5){
            post.changeExposed(false);
        }
        reportUserRepository.save(ReportUser.CreateReportUser(reporter, reportedUser));
        if(reportUserRepository.countAllByUser(reportedUser) >= 10){
            reportedUser.updateActivated(false);
        }
        return new ReportRes(ReportStatus.SUCCESS);

    }
}
