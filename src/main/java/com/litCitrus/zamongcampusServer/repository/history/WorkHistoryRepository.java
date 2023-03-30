package com.litCitrus.zamongcampusServer.repository.history;

import com.litCitrus.zamongcampusServer.domain.history.WorkHistory;
import com.litCitrus.zamongcampusServer.domain.history.WorkHistoryType;
import com.litCitrus.zamongcampusServer.dto.history.WorkHistoryResDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Long> {
    @Query(
        "SELECT " +
            "new com.litCitrus.zamongcampusServer.dto.history.WorkHistoryResDto(" +
            "function('date_format', wh.workDate, '%Y-%m-%d'), " +
            "wh.functionType, " +
            "wh.workType, " +
            "count(wh.id)" +
            ")" +
        "FROM WorkHistory wh " +
        "WHERE wh.functionType =:functionType AND wh.workType =:workType "+
        "GROUP BY function('date_format', wh.workDate, '%Y-%m-%d'), wh.functionType, wh.workType "
    )
    List<WorkHistoryResDto> getStasticsList(@Param("workType") WorkHistoryType.WorkType workType, @Param("functionType") WorkHistoryType.FunctionType functionType);

}
