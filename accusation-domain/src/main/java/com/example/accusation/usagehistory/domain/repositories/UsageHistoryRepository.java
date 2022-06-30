package com.example.accusation.usagehistory.domain.repositories;

import com.example.accusation.usagehistory.domain.UsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Long> {

}
