package com.example.accusation.contents.api.dto;

import com.example.accusation.partyhistory.api.dto.MemberResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccusationContentsResponse {

    private long id;
    private String startPlace;
    private String endPlace;
    private String usageTime;
    private MemberResponse accusedMember;
    private ContentsResponse contents;
    private String createdAt;

}
