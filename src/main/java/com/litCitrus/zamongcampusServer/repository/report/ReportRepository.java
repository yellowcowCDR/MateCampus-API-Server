package com.litCitrus.zamongcampusServer.repository.report;

import com.litCitrus.zamongcampusServer.domain.report.Report;
import com.litCitrus.zamongcampusServer.dto.report.ReportReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE (:#{#searchInfo.reportCategory} is null or r.reportCategory = :#{#searchInfo.reportCategory}) "
            +"and (:#{#searchInfo.reportedUserId} is null or r.reportedUser.loginId = :#{#searchInfo.reportedUserId})"
            +"and (:#{#searchInfo.targetUserId} is null or r.targetUser.loginId = :#{#searchInfo.targetUserId})"
            +"and (:#{#searchInfo.reportContent} is null or r.reportContent LIKE %:#{#searchInfo.reportContent}%)"
            +"and (:#{#searchInfo._createdDateStart} is null or r.createdAt >= :#{#searchInfo._createdDateStart})"
            +"and (:#{#searchInfo._createdDateEnd} is null or r.createdAt <= :#{#searchInfo._createdDateEnd})"
    )
    List<Report> findAllByReportCategoryAndReportedUserIdAndTargetUserIdAndReportContentAndCreatedAtAfterAndCreatedAtBefore(
            @Param("searchInfo")ReportReq.Search searchInfo
    );
}
