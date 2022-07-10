package com.example.accusation.member.domain;

import com.example.accusation.partyhistory.domain.PartyHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBERS")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "MEMBER_NAME", nullable = false)
    private String memberName;

    @Column(name = "MEMBER_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_HISTORY_ID")
    private PartyHistory partyHistory;

    @Builder
    public Member(Long memberId, String memberName, MemberRole memberRole, PartyHistory partyHistory) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberRole = memberRole;
        this.partyHistory = partyHistory;
    }

}
