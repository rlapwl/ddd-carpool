package com.example.accusation.api.dto;

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
public class AccusationRequest {

    private PartyRequest party;

    @Schema(description = "신고 등록한 회원 ID")
    private long memberId;

    @Schema(description = "신고 대상 회원 ID")
    private long accusedMemberId;

    @Schema(description = "신고 대상 회원 이름")
    @NotBlank
    private String accusedMemberName;

    private AccusationContentsRequest accusationContents;

}
