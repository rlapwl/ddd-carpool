package com.example.accusation.partyhistory.domain;

import com.example.accusation.member.domain.Member;
import com.example.accusation.member.domain.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PartyHistoryTest {

    private static final long MEMBER_ID = 1L;

    private PartyHistory partyHistory;

    @BeforeEach
    void setUp() {
        List<Member> memberList = new ArrayList<>();
        memberList.add(Member.builder()
                .memberId(MEMBER_ID)
                .memberName("member1")
                .memberRole(MemberRole.DRIVER)
                .build()
        );
        memberList.add(Member.builder()
                .memberId(2L)
                .memberName("member2")
                .memberRole(MemberRole.CARPOOLER)
                .build()
        );

        partyHistory = PartyHistory.builder()
                .partyId(1L)
                .startPlace("정자역")
                .endPlace("사당역")
                .createdAt(LocalDateTime.now())
                .build();
        partyHistory.getMembers().addAll(memberList);
    }

    @DisplayName("자신을 제외한 다른 회원들을 조회한다.")
    @Test
    void getMembersExceptBy() {
        List<Member> members = partyHistory.getMembersExceptBy(MEMBER_ID);

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getMemberId()).isNotEqualTo(MEMBER_ID);
    }

}
