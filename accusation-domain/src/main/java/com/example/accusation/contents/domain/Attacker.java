package com.example.accusation.contents.domain;

import com.example.accusation.common.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Attacker {

    @Column(name = "ATTACKER_ID", nullable = false)
    private Long userId;

    @Column(name = "ATTACKER_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

}
