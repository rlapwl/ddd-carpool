package com.example.accusation.contents.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccusationContentsRequest {

    private long partyId;
    private long memberId;
    private long accusedMemberId;
    private String title;
    private String desc;

}
