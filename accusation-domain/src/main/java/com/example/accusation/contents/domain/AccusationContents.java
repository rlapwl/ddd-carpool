package com.example.accusation.contents.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "ACCUSATION_CONTENTS")
public class AccusationContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCUSED_USER_ID", nullable = false)
    private Long accusedUserId;

    @Embedded
    private Attacker attacker;

    @Embedded
    private Contents contents;

    @Column(name = "ACCUSATION_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccusationStatus accusationStatus;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Builder
    public AccusationContents(Long accusedUserId, Attacker attacker, Contents contents, AccusationStatus accusationStatus) {
        this.accusedUserId = accusedUserId;
        this.attacker = attacker;
        this.contents = contents;
        this.accusationStatus = accusationStatus;
    }

}
