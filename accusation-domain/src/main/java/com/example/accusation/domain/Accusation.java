package com.example.accusation.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ACCUSATIONS")
public class Accusation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Embedded
    private AccusedMember accusedMember;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @Embedded
    private AccusationContents accusationContents;

    @Column(name = "ACCUSATION_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccusationStatus accusationStatus;

    @Column(name = "RESULT_COMMENT")
    private String resultComment;

    @CreatedDate
    @Column(name = "CREATED_DATE_TIME")
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE_TIME")
    private LocalDateTime modifiedDateTime;

    @Builder
    public Accusation(Long memberId, AccusedMember accusedMember, Party party, AccusationContents accusationContents) {
        this.memberId = memberId;
        this.accusedMember = accusedMember;
        this.party = party;
        this.accusationContents = accusationContents;
        this.accusationStatus = AccusationStatus.REGISTERED;
    }

    @PrePersist
    public void prePersist() {
        this.resultComment = (this.resultComment == null) ? "" : this.resultComment;
    }

    public boolean isNotRegisteredMember(long memberId) {
        return this.memberId != memberId;
    }

    public boolean isNotRegisteredStatus() {
        return this.accusationStatus != AccusationStatus.REGISTERED;
    }

    public String getAccusedMemberName() {
        return this.accusedMember.getName();
    }

    public void modifyAccusationContents(AccusationContents accusationContents) {
        this.accusationContents.modify(accusationContents);
    }

    public void process(AccusationStatus accusationStatus, String resultComment) {
        this.accusationStatus = accusationStatus;
        this.resultComment = StringUtils.stripToEmpty(resultComment);
    }

}
