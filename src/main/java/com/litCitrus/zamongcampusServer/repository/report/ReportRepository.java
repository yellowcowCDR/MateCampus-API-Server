package com.litCitrus.zamongcampusServer.repository.report;

import com.litCitrus.zamongcampusServer.domain.report.Report;
import com.litCitrus.zamongcampusServer.domain.report.ReportCategory;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    public List<Report> findAllByReportedUser(User reportedUser);
    public List<Report> findAllByTargetUser(User targetUser);
    public List<Report> findAllByReportCategory(ReportCategory reportCategory);
    public List<Report> findAllByReportedUserAndReportCategory(User reportedUser,ReportCategory reportCategory);
    public List<Report> findAllByTargetUserAndReportCategory(User targetUser, ReportCategory reportCategory);
    public List<Report> findAllByReportedUserAndTargetUserAndReportCategory(User reportedUser, User targetUser, ReportCategory reportCategory);

//    @Query("SELECT r FROM Report r WHERE (:reportCategory is null or r.reportCategory = :reportCategory) "
//            +"and (:reportedUser is null or r.reportedUser = :reportedUser)"
//            +"and (:targetUser is null or r.targetUser = :targetUser)"
//            +"and (:reportContent is null or r.reportContent = :reportContent)"
//    )
//    public List<Report> findAllByReportCategoryAndReportedUserAndTargetUserAndReportContent(
//            ReportCategory reportCategory,
//            User reportedUser,
//            User targetUser,
//            String reportContent
//    );
    @Query("SELECT r FROM Report r WHERE (:#{#searchInfo.reportCategory} is null or r.reportCategory = :#{#searchInfo.reportCategory}) "
            +"and (:#{#searchInfo.reportedUserId} is null or r.reportedUser.loginId = :#{#searchInfo.reportedUserId})"
            +"and (:#{#searchInfo.targetUserId} is null or r.targetUser.loginId = :#{#searchInfo.targetUserId})"
            +"and (:#{#searchInfo.reportContent} is null or r.reportContent LIKE %:#{#searchInfo.reportContent}%)"
            +"and (:#{#searchInfo.createdDateStart} is null or r.createdAt >= :#{#searchInfo.createdDateStart})"
            +"and (:#{#searchInfo.createdDateEnd} is null or r.createdAt <= :#{#searchInfo.createdDateEnd})"
    )
    public List<Report> findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(
           @Param("searchInfo")ReportReq.Search searchInfo
    );

    public List<Report> findByReportedUserAndTargetUser(User reportedUser, User TargetUser);
}
