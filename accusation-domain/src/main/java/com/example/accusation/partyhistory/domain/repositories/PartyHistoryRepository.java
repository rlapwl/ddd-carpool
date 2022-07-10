package com.example.accusation.partyhistory.domain.repositories;

import com.example.accusation.partyhistory.domain.PartyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PartyHistoryRepository extends JpaRepository<PartyHistory, Long> {

    @Query(value = "SELECT p FROM Member m JOIN m.partyHistory p WHERE m.memberId = :memberId " +
        "AND p.createdAt BETWEEN :startDatetime AND :endDatetime")
    List<PartyHistory> findByIdAndCreatedAtBetween(@Param("memberId") long memberId,
                                             @Param("startDatetime") LocalDateTime startDatetime,
                                             @Param("endDatetime") LocalDateTime endDatetime);

}
