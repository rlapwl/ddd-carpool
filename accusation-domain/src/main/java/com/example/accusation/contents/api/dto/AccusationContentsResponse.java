package com.example.accusation.contents.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccusationContentsResponse {

    private long id;
    private long accusedUserId;
    private AttackerResponse attackerResponse;
    private ContentsResponse contentsResponse;
    private String createdAt;

}
