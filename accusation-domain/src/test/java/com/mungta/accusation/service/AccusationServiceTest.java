package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.*;
import com.mungta.accusation.domain.*;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.service.exception.NotFoundAccusationException;
import com.mungta.accusation.service.exception.NotModifiedAccusationException;
import com.mungta.accusation.service.exception.NotWriterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.mungta.accusation.api.dto.AccusationListResponse.AccusationInfoResponse;
import static com.mungta.accusation.constants.AccusationTestSample.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(value = MockitoExtension.class)
class AccusationServiceTest {

    @InjectMocks
    @Spy
    private AccusationService accusationService;

    @Mock
    private AccusationRepository accusationRepository;

    private Accusation accusation;

    private final LocalDateTime nowDateTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        accusation = Accusation.builder()
                .memberId(MEMBER_ID)
                .accusedMember(
                        AccusedMember.builder()
                                .id(ACCUSED_MEMBER_ID)
                                .name(ACCUSED_MEMBER_NAME)
                                .emailAddress(ACCUSED_MEMBER_EMAIL)
                                .build()
                )
                .partyInfo(
                        PartyInfo.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()
                )
                .accusationContents(
                        new AccusationContents(CONTENTS_TITLE, CONTENTS_DESC)
                )
                .build();
        accusation.setId(ACCUSATION_ID);
        accusation.setCreatedDateTime(nowDateTime);
    }

    @DisplayName("?????? ?????? ??????.")
    @Test
    void addAccusation() {
        given(accusationRepository.save(any())).willReturn(accusation);

        long response = accusationService.addAccusation(ACCUSATION_REQUEST);

        assertThat(response).isEqualTo(ACCUSATION_ID);
    }

    @DisplayName("?????? ?????? ??????.")
    @Test
    void getAccusation() {
        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        AccusationResponse response = accusationService.getAccusation(ACCUSATION_ID, MEMBER_ID);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(ACCUSATION_ID),
                () -> assertThat(response.getAccusedMemberName()).isEqualTo(ACCUSED_MEMBER_NAME),
                () -> assertThat(response.getPartyInfo()).isEqualTo(
                        PartyInfoResponse.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()),
                () -> assertThat(response.getAccusationContents()).isEqualTo(
                        AccusationContentsResponse.builder()
                                .title(CONTENTS_TITLE)
                                .desc(CONTENTS_DESC)
                                .build()),
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED)
        );
    }

    @DisplayName("??????????????? ?????? id??? accusation ????????? ?????? ??? ?????? ?????? NotFoundAccusationException ??????.")
    @Test
    void getAccusation_not_found_by_id() {
        given(accusationRepository.findById(anyLong()))
                .willThrow(new NotFoundAccusationException("???????????? ?????? ??????????????????."));

        assertThatThrownBy(() -> accusationService.getAccusation(2L, MEMBER_ID))
                .isInstanceOf(NotFoundAccusationException.class);
    }

    @DisplayName("??????(accusation) ????????? ?????? ID??? ??????????????? ?????? ?????? ID??? ???????????? ?????? ?????? NotWriterException ??????.")
    @Test
    void getAccusation_not_writer_by_memberId() {
        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        assertThatThrownBy(() -> accusationService.getAccusation(ACCUSATION_ID, "2"))
                .isInstanceOf(NotWriterException.class);
    }

    @DisplayName("?????? ?????? ????????? ?????? ??????.")
    @Test
    void getAccusationList() {
        given(accusationRepository.findByMemberId(anyString())).willReturn(List.of(accusation));

        AccusationListResponse response = accusationService.getAccusationList(MEMBER_ID);
        List<AccusationInfoResponse> responseList = response.getAccusations();

        assertAll(
                () -> assertThat(responseList.size()).isEqualTo(1),
                () -> assertThat(responseList.get(0)).isEqualTo(
                        AccusationInfoResponse.builder()
                                .id(ACCUSATION_ID)
                                .partyId(PARTY_ID)
                                .title(CONTENTS_TITLE)
                                .accusationStatus(AccusationStatus.REGISTERED)
                                .createdDateTime(nowDateTime.format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                ))
                                .build()
                )
        );
    }

    @DisplayName("?????? memberId??? ????????? ????????? ????????? ?????? ?????? ??? ???????????? ??????.")
    @Test
    void getAccusationList_size_zero() {
        given(accusationRepository.findByMemberId(anyString())).willReturn(List.of());

        AccusationListResponse response = accusationService.getAccusationList("2");

        assertThat(response.getAccusations().size()).isEqualTo(0);
    }

    @DisplayName("?????? ?????? ?????? ??????.")
    @Test
    void modifyAccusationContents() {
        AccusationContentsRequest request = new AccusationContentsRequest("?????? ??????", "?????? ??????");

        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        AccusationResponse response = accusationService.modifyAccusationContents(ACCUSATION_ID, request);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(ACCUSATION_ID),
                () -> assertThat(response.getAccusedMemberName()).isEqualTo(ACCUSED_MEMBER_NAME),
                () -> assertThat(response.getPartyInfo()).isEqualTo(
                        PartyInfoResponse.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()),
                () -> assertThat(response.getAccusationContents()).isEqualTo(
                        AccusationContentsResponse.builder()
                                .title(request.getTitle())
                                .desc(request.getDesc())
                                .build()),
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED)
        );
    }

    @DisplayName("???????????? ?????? ????????? ??? ??????(?????? or ??????)??? ?????? ?????? ?????? ????????? NotModifiedAccusationException ??????.")
    @Test
    void modifyAccusationContents_not_registered_status() {
        AccusationContentsRequest request = new AccusationContentsRequest("?????? ??????", "?????? ??????");
        accusation.process(AccusationStatus.REJECTED, "");

        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        assertThatThrownBy(() -> accusationService.modifyAccusationContents(ACCUSATION_ID, request))
                .isInstanceOf(NotModifiedAccusationException.class);
    }
    
}
