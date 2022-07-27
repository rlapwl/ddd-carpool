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
public class AccusedMemberRequest {

    @Schema(description = "신고 대상 회원 ID")
    private String id;

    @Schema(description = "신고 대상 회원 이름")
    @NotBlank
    private String name;

    @Schema(description = "신고 대상 회원 Email")
    @NotBlank
    private String emailAddress;

}
