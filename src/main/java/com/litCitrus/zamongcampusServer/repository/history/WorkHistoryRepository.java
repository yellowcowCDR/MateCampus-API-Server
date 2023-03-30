package com.litCitrus.zamongcampusServer.repository.history;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Long> {
}
