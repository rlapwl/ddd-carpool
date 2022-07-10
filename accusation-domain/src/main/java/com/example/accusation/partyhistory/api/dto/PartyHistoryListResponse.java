package com.example.accusation.partyhistory.api.dto;

import com.example.accusation.partyhistory.domain.PartyHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PartyHistoryListResponse {

    private List<PartyHistoryResponse> partyHistoryList;

    @Getter
    @Builder
    public static class PartyHistoryResponse {

        private long partyId;
        private String startPlace;
        private String endPlace;
        private String createdAt;
        private List<MemberResponse> memberList;

        public static PartyHistoryResponse of(PartyHistory partyHistory, List<MemberResponse> memberList) {
            return PartyHistoryResponse.builder()
                    .partyId(partyHistory.getPartyId())
                    .startPlace(partyHistory.getStartPlace())
                    .endPlace(partyHistory.getEndPlace())
                    .createdAt(partyHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .memberList(memberList)
                    .build();
        }

    }

}
