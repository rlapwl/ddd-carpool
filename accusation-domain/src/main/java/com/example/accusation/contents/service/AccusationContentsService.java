package com.example.accusation.contents.service;

import com.example.accusation.contents.api.dto.AccusationContentsRequest;
import com.example.accusation.contents.api.dto.AccusationContentsResponse;
import com.example.accusation.contents.api.dto.AttackerResponse;
import com.example.accusation.contents.api.dto.ContentsResponse;
import com.example.accusation.contents.domain.AccusationContents;
import com.example.accusation.contents.domain.Attacker;
import com.example.accusation.contents.domain.Contents;
import com.example.accusation.contents.domain.repositories.AccusationContentsRepository;
import com.example.accusation.usagehistory.service.UsageHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

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

    public AccusationContentsResponse getContents(long id) {
        AccusationContents accusationContents = accusationContentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 신고글입니다."));

        return AccusationContentsResponse.builder()
                .id(accusationContents.getId())
                .accusedUserId(accusationContents.getAccusedUserId())
                .attackerResponse(
                        AttackerResponse.of(accusationContents.getAttacker())
                )
                .contentsResponse(
                        ContentsResponse.of(accusationContents.getContents())
                )
                .createdAt(
                        accusationContents.getCreatedAt().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build();
    }

}
