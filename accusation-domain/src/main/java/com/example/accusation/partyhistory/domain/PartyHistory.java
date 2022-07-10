package com.example.accusation.partyhistory.domain;

import com.example.accusation.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PARTY_HISTORIES")
public class PartyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PARTY_ID", nullable = false)
    private Long partyId;

    @Column(name = "START_PLACE", nullable = false)
    private String startPlace;

    @Column(name = "END_PLACE", nullable = false)
    private String endPlace;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "partyHistory")
    private final List<Member> members = new ArrayList<>();

    @Builder
    public PartyHistory(Long partyId, String startPlace, String endPlace, LocalDateTime createdAt) {
        this.partyId = partyId;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.createdAt = createdAt;
    }

    public List<Member> getMembersExceptBy(long memberId) {
        return members.stream()
                .filter(member -> member.getMemberId() != memberId)
                .collect(Collectors.toList());
    }

}
