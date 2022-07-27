package com.example.accusation.api.dto.admin;

import com.example.accusation.api.dto.AccusationContentsResponse;
import com.example.accusation.api.dto.PartyInfoResponse;
import com.example.accusation.domain.Accusation;
import com.example.accusation.domain.AccusationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminAccusationResponse {

    @Schema(description = "신고 ID")
    private long id;

    @Schema(description = "신고 대상 회원 이름")
    private String accusedMemberName;

    private PartyInfoResponse partyInfo;

    private AccusationContentsResponse accusationContents;

    @Schema(description = "신고 상태")
    private AccusationStatus accusationStatus;

    @Schema(description = "관리자 코멘트")
    private String resultComment;

    public static AdminAccusationResponse of(Accusation accusation) {
        return AdminAccusationResponse.builder()
                .id(accusation.getId())
                .accusedMemberName(accusation.getAccusedMemberName())
                .partyInfo(
                        PartyInfoResponse.of(accusation.getPartyInfo())
                )
                .accusationContents(
                        AccusationContentsResponse.of(accusation.getAccusationContents())
                )
                .accusationStatus(accusation.getAccusationStatus())
                .resultComment(accusation.getResultComment())
                .build();
    }

}
