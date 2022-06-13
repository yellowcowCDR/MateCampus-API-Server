package com.litCitrus.zamongcampusServer.service.report;

import com.litCitrus.zamongcampusServer.domain.report.ReportUser;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.dto.report.ReportStatus;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.report.ReportUserRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReportUserService {

    private final UserRepository userRepository;
    private final ReportUserRepository reportUserRepository;

    @Transactional
    public ReportRes reportUser(String loginId){
        User reporter = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User user = userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        if(reportUserRepository.existsByReporterAndUser(reporter, user)) return new ReportRes(ReportStatus.DUPLICATE);
        reportUserRepository.save(ReportUser.CreateReportUser(reporter, user));

        if(reportUserRepository.countAllByUser(user) >= 10){
            user.updateActivated(false);
        }
        return new ReportRes(ReportStatus.SUCCESS);
    }
}
