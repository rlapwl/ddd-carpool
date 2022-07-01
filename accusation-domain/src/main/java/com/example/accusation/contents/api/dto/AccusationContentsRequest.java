package com.example.accusation.contents.api.dto;

import com.example.accusation.common.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccusationContentsRequest {

    private long accusedUserId;
    private long attackerId;
    private UserRole attackerRole;
    private String title;
    private String desc;

}
