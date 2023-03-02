package com.litCitrus.zamongcampusServer.service.report;

import com.litCitrus.zamongcampusServer.domain.report.Report;
import com.litCitrus.zamongcampusServer.domain.report.ReportCategory;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import com.litCitrus.zamongcampusServer.dto.report.ReportRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.report.ReportRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        //User reportedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User reportedUser = SecurityUtil.getUser();
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

        //날짜 파라미터 변환
        if(searchInfo.getCreatedDateStart()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateStart(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("00:00:00"));
            searchInfo.set_createdDateStart(ldt);
        }
        if(searchInfo.getCreatedDateEnd()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateEnd(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("23:59:59"));
            searchInfo.set_createdDateEnd(ldt);
        }

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }
    public List<ReportRes> getProfileReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 프로필로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0002);

        //날짜 파라미터 변환
        if(searchInfo.getCreatedDateStart()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateStart(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("00:00:00"));
            searchInfo.set_createdDateStart(ldt);
        }
        if(searchInfo.getCreatedDateEnd()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateEnd(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("23:59:59"));
            searchInfo.set_createdDateEnd(ldt);
        }

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }

    public List<ReportRes> getFeedReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 피드로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0003);

        //날짜 파라미터 변환
        if(searchInfo.getCreatedDateStart()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateStart(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("00:00:00"));
            searchInfo.set_createdDateStart(ldt);
        }
        if(searchInfo.getCreatedDateEnd()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateEnd(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("23:59:59"));
            searchInfo.set_createdDateEnd(ldt);
        }

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }

    public List<ReportRes> getFeedCommentReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 피드댓글로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0004);

        //날짜 파라미터 변환
        if(searchInfo.getCreatedDateStart()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateStart(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("00:00:00"));
            searchInfo.set_createdDateStart(ldt);
        }
        if(searchInfo.getCreatedDateEnd()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateEnd(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("23:59:59"));
            searchInfo.set_createdDateEnd(ldt);
        }

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }
    public List<ReportRes> getChattingReportList(ReportReq.Search searchInfo){
        List<Report> reportList;
        List<ReportRes> reportResList;

        //카테고리를 채팅으로 설정
        searchInfo.setReportCategory(ReportCategory.REPORTCATEGORY0005);

        //날짜 파라미터 변환
        if(searchInfo.getCreatedDateStart()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateStart(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("00:00:00"));
            searchInfo.set_createdDateStart(ldt);
        }
        if(searchInfo.getCreatedDateEnd()!=null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ld = LocalDate.parse(searchInfo.getCreatedDateEnd(), dateFormatter);
            LocalDateTime ldt = LocalDateTime.of(ld, LocalTime.parse("23:59:59"));
            searchInfo.set_createdDateEnd(ldt);
        }

        reportList = reportRepository.findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(searchInfo);
        reportResList = reportList.stream().map(ReportRes::new).collect(Collectors.toList());
        return reportResList;
    }


}
