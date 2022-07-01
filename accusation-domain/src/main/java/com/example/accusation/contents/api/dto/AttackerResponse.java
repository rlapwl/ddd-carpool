package com.example.accusation.contents.api.dto;

import com.example.accusation.common.UserRole;
import com.example.accusation.contents.domain.Attacker;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttackerResponse {

    private long attackerId;
    private UserRole attackerRole;

    public static AttackerResponse of(Attacker attacker) {
        return new AttackerResponse(attacker.getUserId(), attacker.getUserRole());
    }

}
