package com.litCitrus.zamongcampusServer.repository.report;

import com.litCitrus.zamongcampusServer.domain.report.ReportUser;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {

    int countAllByUser(User user);
    boolean existsByReporterAndUser(User reporter, User user);
}
