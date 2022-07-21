package com.example.accusation.service;

import com.example.accusation.api.dto.AccusationContentsRequest;
import com.example.accusation.api.dto.AccusationRequest;
import com.example.accusation.api.dto.PartyRequest;
import com.example.accusation.domain.Accusation;
import com.example.accusation.domain.AccusationContents;
import com.example.accusation.domain.AccusedMember;
import com.example.accusation.domain.Party;
import com.example.accusation.domain.repositories.AccusationRepository;
import com.example.accusation.domain.repositories.PartyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(value = MockitoExtension.class)
class AccusationServiceTest {

    @InjectMocks
    @Spy
    private AccusationService accusationService;

    @Mock
    private AccusationRepository accusationRepository;

    @Mock
    private PartyRepository partyRepository;

    @DisplayName("신고 등록")
    @Test
    void addAccusation() {
        AccusationRequest request = AccusationRequest.builder()
                .party(
                        PartyRequest.builder()
                                .partyId(1L)
                                .placeOfDeparture("정자역")
                                .destination("사당역")
                                .startedDateTime("2022-07-18 18:20")
                                .build()
                )
                .memberId(1L)
                .accusedMemberId(2L)
                .accusedMemberName("통통이")
                .accusationContents(
                        AccusationContentsRequest.builder()
                                .title("신고합니다.")
                                .desc("내용")
                                .build()
                )
                .build();
        
        Party party = Party.builder()
                .partyId(1L)
                .placeOfDeparture("정자역")
                .destination("사당역")
                .startedDateTime("2022-07-18 18:20")
                .build();

        Accusation accusation = Accusation.builder()
                .memberId(1L)
                .accusedMember(new AccusedMember(2L, "통통이"))
                .party(party)
                .accusationContents(new AccusationContents("신고합니다.", "제목"))
                .build();
        accusation.setId(1L);

        given(partyRepository.findByPartyId(anyLong())).willReturn(Optional.ofNullable(party));
        given(accusationRepository.save(any())).willReturn(accusation);

        // when
        long response = accusationService.addAccusation(request);

        // then
        assertThat(response).isEqualTo(1L);
//        List<PartyHistoryResponse> partyHistoryResponseList = response.getPartyHistoryList();
//        assertThat(partyHistoryResponseList.size()).isEqualTo(1);
//
//        List<MemberResponse> memberResponseList = partyHistoryResponseList.get(0).getMemberList();
//        assertThat(memberResponseList.size()).isEqualTo(1);
//        assertThat(memberResponseList.get(0).getId()).isEqualTo(2L);
//        assertThat(memberResponseList.get(0).getName()).isEqualTo("member2");
    }

}
