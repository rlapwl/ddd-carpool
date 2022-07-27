package com.mungta.accusation.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private String memberId;

    @Embedded
    private AccusedMember accusedMember;

    @Embedded
    private PartyInfo partyInfo;

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
    public Accusation(String memberId, AccusedMember accusedMember, PartyInfo partyInfo, AccusationContents accusationContents) {
        this.memberId = memberId;
        this.accusedMember = accusedMember;
        this.partyInfo = partyInfo;
        this.accusationContents = accusationContents;
        this.accusationStatus = AccusationStatus.REGISTERED;
        this.resultComment = "";
    }

    public boolean isNotWriter(String memberId) {
        return !this.memberId.equals(memberId);
    }

    public boolean isNotRegisteredStatus() {
        return this.accusationStatus != AccusationStatus.REGISTERED;
    }

    public String getAccusedMemberName() {
        return this.accusedMember.getName();
    }

    public void modifyAccusationContents(AccusationContents accusationContents) {
        this.accusationContents = accusationContents;
    }

    public void process(AccusationStatus accusationStatus, String resultComment) {
        this.accusationStatus = accusationStatus;
        this.resultComment = StringUtils.stripToEmpty(resultComment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accusation that = (Accusation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
