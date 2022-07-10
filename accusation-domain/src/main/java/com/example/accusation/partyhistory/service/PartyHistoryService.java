package com.example.accusation.partyhistory.service;

import com.example.accusation.partyhistory.api.dto.MemberResponse;
import com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse;
import com.example.accusation.partyhistory.domain.PartyHistory;
import com.example.accusation.partyhistory.domain.repositories.PartyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse.PartyHistoryResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartyHistoryService {

    private static final int SUBTRACT_DAYS = 3;

    private final PartyHistoryRepository partyHistoryRepository;

    public boolean isUsedService(long memberId) {
        // 신고할때, 서비스를 사용하고 이후 3일 이내로 신고할 수 있게 제한을 둘 것인지..?
        //return usageHistoryRepository.findByUserId(userId).size() > 0;
        return true;
    }

    public PartyHistoryListResponse getPartyHistoryList(long memberId) {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime startDatetime = LocalDateTime.of(nowDate.minusDays(SUBTRACT_DAYS), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(nowDate, LocalTime.of(23,59,59));

        List<PartyHistory> partyHistoryList = partyHistoryRepository
                .findByIdAndCreatedAtBetween(memberId, startDatetime, endDatetime);

        List<PartyHistoryResponse> partyHistoryResponses = new ArrayList<>();
        for (PartyHistory partyHistory : partyHistoryList) {
            List<MemberResponse> memberResponseList = partyHistory.getMembersExceptBy(memberId).stream()
                    .map(MemberResponse::of)
                    .collect(Collectors.toList());

            partyHistoryResponses.add(PartyHistoryResponse.of(partyHistory, memberResponseList));
        }

        return new PartyHistoryListResponse(partyHistoryResponses);
    }

}
