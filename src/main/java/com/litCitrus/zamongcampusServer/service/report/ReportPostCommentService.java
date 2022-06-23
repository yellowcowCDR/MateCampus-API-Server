package com.litCitrus.zamongcampusServer.service.report;

import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.report.ReportPostComment;
import com.litCitrus.zamongcampusServer.domain.report.ReportType;
import com.litCitrus.zamongcampusServer.domain.report.ReportUser;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.dto.report.ReportStatus;
import com.litCitrus.zamongcampusServer.exception.post.PostCommentNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.PostCommentRepository;
import com.litCitrus.zamongcampusServer.repository.report.ReportPostCommentRepository;
import com.litCitrus.zamongcampusServer.repository.report.ReportUserRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPostCommentService {

    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final ReportPostCommentRepository reportPostCommentRepository;
    private final ReportUserRepository reportUserRepository;

    @Transactional
    public ReportRes reportPostComment(Long postCommentId, ReportReq dto){
        User reporter = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(PostCommentNotFoundException::new);
        User reportedUser = postComment.getUser();
        if(reportPostCommentRepository.existsByReporterAndPostComment(reporter, postComment)) return new ReportRes(ReportStatus.DUPLICATE);
        reportPostCommentRepository.save(ReportPostComment.CreateReportComment(reporter, postComment));
        if(reportPostCommentRepository.countAllByPostComment(postComment) >= 5){
            postComment.changeExposed(false);
        }
        reportUserRepository.save(ReportUser.CreateReportUser(reporter, reportedUser, ReportType.valueOf(dto.getReportType().toUpperCase())));
        if(reportUserRepository.countAllByUser(reportedUser) >= 10){
            reportedUser.updateActivated(false);
        }
        return new ReportRes(ReportStatus.SUCCESS);

    }
}
