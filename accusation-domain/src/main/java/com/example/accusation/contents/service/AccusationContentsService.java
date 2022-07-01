package com.example.accusation.contents.service;

import com.example.accusation.contents.api.dto.AccusationContentsRequest;
import com.example.accusation.contents.domain.AccusationContents;
import com.example.accusation.contents.domain.Attacker;
import com.example.accusation.contents.domain.Contents;
import com.example.accusation.contents.domain.repositories.AccusationContentsRepository;
import com.example.accusation.usagehistory.service.UsageHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationContentsService {

    private final AccusationContentsRepository accusationContentsRepository;
    private final UsageHistoryService usageHistoryService;

    @Transactional
    public long addContents(AccusationContentsRequest request) {
        validateUser(request.getAccusedUserId());

        AccusationContents accusationContents = AccusationContents.builder()
                .accusedUserId(request.getAccusedUserId())
                .attacker(new Attacker(request.getAttackerId(), request.getAttackerRole()))
                .contents(new Contents(request.getTitle(), request.getDesc()))
                .build();

        return accusationContentsRepository.save(accusationContents).getId();
    }

    private void validateUser(long userId) {
        if (!usageHistoryService.isUsedService(userId)) {
            throw new RuntimeException("서비스 사용 내역이 없으므로 신고를 할 수 없습니다.");
        }
    }

}
