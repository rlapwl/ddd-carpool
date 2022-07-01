package com.example.accusation.contents.api.dto;

import com.example.accusation.common.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccusationContentsResponse {

    private long id;
    private long accusedUserId;
    private long attackerId;
    private UserRole attackerRole;
    private String title;
    private String desc;
    private LocalDateTime createdAt;

}
