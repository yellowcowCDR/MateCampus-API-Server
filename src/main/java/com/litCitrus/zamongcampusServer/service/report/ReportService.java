package com.litCitrus.zamongcampusServer.service.report;

import com.litCitrus.zamongcampusServer.domain.report.Report;
import com.litCitrus.zamongcampusServer.domain.report.ReportCategory;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.exception.report.ReportNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.report.ReportRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    public void createReport(ReportReq.Create reportCreateReq){
        User reportedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findByLoginId(reportCreateReq.getTargetUserId()).orElseThrow(UserNotFoundException::new);

        Report report = Report.builder()
                .reportedUser(reportedUser)
                .targetUser(targetUser)
                .reportContent(reportCreateReq.getReportContent())
                .reportCategory(reportCreateReq.getReportCategory())
                .build();

        reportRepository.save(report);
    }


    public List<ReportRes> getAllReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;
        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }
    public List<ReportRes> getProfileReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 프로필로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0002);

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }

    public List<ReportRes> getFeedReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 피드로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0003);

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }

    public List<ReportRes> getFeedCommentReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 피드댓글로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0004);

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }
    public List<ReportRes> getChattingReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 채팅으로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0005);

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }


}
