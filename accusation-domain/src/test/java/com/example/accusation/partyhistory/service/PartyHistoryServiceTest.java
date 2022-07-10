package com.example.accusation.partyhistory.service;

import com.example.accusation.member.domain.Member;
import com.example.accusation.member.domain.MemberRole;
import com.example.accusation.partyhistory.api.dto.MemberResponse;
import com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse;
import com.example.accusation.partyhistory.domain.PartyHistory;
import com.example.accusation.partyhistory.domain.repositories.PartyHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse.PartyHistoryResponse;

@ExtendWith(value = MockitoExtension.class)
class PartyHistoryServiceTest {

    @InjectMocks
    @Spy
    private PartyHistoryService partyHistoryService;

    @Mock
    private PartyHistoryRepository partyHistoryRepository;

    @DisplayName("회원이 최근 3일동안 이용한 카풀시스템 내역(출발지, 도착지 등) 및 회원을 제외한 함께 이용한 다른 회원들의 정보를 조회한다.")
    @Test
    void getPartyHistoryList() {
        long memberId = 1L;

        List<Member> memberList = new ArrayList<>();
        memberList.add(Member.builder()
                .memberId(memberId)
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

        PartyHistory partyHistory = PartyHistory.builder()
                .partyId(1L)
                .startPlace("정자역")
                .endPlace("사당역")
                .createdAt(LocalDateTime.now())
                .build();
        partyHistory.getMembers().addAll(memberList);

        given(partyHistoryRepository.findByIdAndCreatedAtBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(List.of(partyHistory));

        // when
        PartyHistoryListResponse response = partyHistoryService.getPartyHistoryList(memberId);

        // then
        List<PartyHistoryResponse> partyHistoryResponseList = response.getPartyHistoryList();
        assertThat(partyHistoryResponseList.size()).isEqualTo(1);

        List<MemberResponse> memberResponseList = partyHistoryResponseList.get(0).getMemberList();
        assertThat(memberResponseList.size()).isEqualTo(1);
        assertThat(memberResponseList.get(0).getId()).isEqualTo(2L);
        assertThat(memberResponseList.get(0).getName()).isEqualTo("member2");
    }

}
