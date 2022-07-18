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

    @ManyToOne
    @JoinColumn(name = "PARTY_INFO_ID")
    private PartyInfo partyInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PARTY_HISTORY_ID")
    private final List<Member> members = new ArrayList<>();

    public List<Member> getMembersExceptBy(long memberId) {
        return members.stream()
                .filter(member -> member.getMemberId() != memberId)
                .collect(Collectors.toList());
    }

}
