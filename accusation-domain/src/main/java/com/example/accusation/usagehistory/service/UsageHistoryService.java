package com.example.accusation.usagehistory.service;

import com.example.accusation.usagehistory.domain.repositories.UsageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UsageHistoryService {

    private final UsageHistoryRepository usageHistoryRepository;

    public boolean isUsedService(long userId) {
        // 신고할때, 서비스를 사용하고 이후 3일 이내로 신고할 수 있게 제한을 둘 것인지..?
        //return usageHistoryRepository.findByUserId(userId).size() > 0;
        return true;
    }

}
