package com.example.accusation.contents.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ACCUSATION_CONTENTS")
public class AccusationContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PARTY_ID", nullable = false)
    private Long partyId;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "ACCUSED_MEMBER_ID", nullable = false)
    private Long accusedMemberId;

    @Embedded
    private Contents contents;

    @Column(name = "ACCUSATION_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccusationStatus accusationStatus;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Builder
    public AccusationContents(Long partyId, Long memberId, Long accusedMemberId, Contents contents) {
        this.partyId = partyId;
        this.memberId = memberId;
        this.accusedMemberId = accusedMemberId;
        this.contents = contents;
        this.accusationStatus = AccusationStatus.REGISTERED;
    }

}
