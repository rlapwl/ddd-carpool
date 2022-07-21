package com.example.accusation.api.dto;

import com.example.accusation.domain.Accusation;
import com.example.accusation.domain.AccusationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccusationResponse {

    @Schema(description = "신고 ID")
    private long id;

    private PartyResponse party;

    @Schema(description = "신고 대상 회원 이름")
    private String accusedMemberName;

    private AccusationContentsResponse accusationContents;

    @Schema(description = "신고 상태")
    private AccusationStatus accusationStatus;

    public static AccusationResponse of(Accusation accusation) {
        return AccusationResponse.builder()
                .id(accusation.getId())
                .party(
                        PartyResponse.of(accusation.getParty())
                )
                .accusedMemberName(accusation.getAccusedMemberName())
                .accusationContents(
                        AccusationContentsResponse.of(accusation.getAccusationContents())
                )
                .accusationStatus(accusation.getAccusationStatus())
                .build();
    }

}
