package com.mungta.accusation.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyInfoRequest {

    @Schema(description = "파티 ID")
    private long partyId;

    @Schema(description = "출발지")
    @NotBlank
    private String placeOfDeparture;

    @Schema(description = "도착지")
    @NotBlank
    private String destination;

    @Schema(description = "파티 시작했던 시간")
    @NotBlank
    private String startedDateTime;

}
