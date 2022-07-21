package com.example.accusation.api.dto;

import com.example.accusation.domain.Party;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartyResponse {

    @Schema(description = "파티 ID")
    private long partyId;

    @Schema(description = "출발지")
    private String placeOfDeparture;

    @Schema(description = "도착지")
    private String destination;

    @Schema(description = "파티 시작했던 시간")
    private String startedDateTime;

    public static PartyResponse of(Party party) {
        return PartyResponse.builder()
                .partyId(party.getPartyId())
                .placeOfDeparture(party.getPlaceOfDeparture())
                .destination(party.getDestination())
                .startedDateTime(party.getStartedDateTime())
                .build();
    }

}
