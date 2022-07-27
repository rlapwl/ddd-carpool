package com.example.accusation.api.dto;

import com.example.accusation.feign.dto.PartyResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccusationMemberListResponse {

    private long partyId;
    private String placeOfDeparture;
    private String destination;
    private String startedDateTime;
    private List<MemberResponse> members;

    public static AccusationMemberListResponse of(PartyResponse party, List<MemberResponse> members) {
        return AccusationMemberListResponse.builder()
                .partyId(party.getPartyId())
                .placeOfDeparture(party.getPlaceOfDeparture())
                .destination(party.getDestination())
                .startedDateTime(party.getStartedDateTime())
                .members(members)
                .build();
    }

    @Getter
    @Builder
    public static class MemberResponse {
        private String id;
        private String name;
        private String emailAddress;
        private String image;
        private boolean isAccused;
    }

}
