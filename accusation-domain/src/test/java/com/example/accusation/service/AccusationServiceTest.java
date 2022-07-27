package com.example.accusation.service;

import com.example.accusation.api.dto.*;
import com.example.accusation.domain.*;
import com.example.accusation.domain.repositories.AccusationRepository;
import com.example.accusation.service.exception.NotFoundAccusationException;
import com.example.accusation.service.exception.NotModifiedAccusationException;
import com.example.accusation.service.exception.NotWriterException;
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

import static com.example.accusation.api.dto.AccusationListResponse.AccusationInfoResponse;
import static com.example.accusation.constants.AccusationTestSample.*;
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

    @DisplayName("신고 등록 성공.")
    @Test
    void addAccusation() {
        given(accusationRepository.save(any())).willReturn(accusation);

        long response = accusationService.addAccusation(ACCUSATION_REQUEST);

        assertThat(response).isEqualTo(ACCUSATION_ID);
    }

    @DisplayName("신고 조회 성공.")
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

    @DisplayName("파라미터로 받은 id로 accusation 객체를 찾을 수 없는 경우 NotFoundAccusationException 발생.")
    @Test
    void getAccusation_not_found_by_id() {
        given(accusationRepository.findById(anyLong()))
                .willThrow(new NotFoundAccusationException("등록되지 않은 신고글입니다."));

        assertThatThrownBy(() -> accusationService.getAccusation(2L, MEMBER_ID))
                .isInstanceOf(NotFoundAccusationException.class);
    }

    @DisplayName("신고(accusation) 등록한 회원 ID와 파라미터로 받은 회원 ID가 일치하지 않는 경우 NotWriterException 발생.")
    @Test
    void getAccusation_not_writer_by_memberId() {
        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        assertThatThrownBy(() -> accusationService.getAccusation(ACCUSATION_ID, "2"))
                .isInstanceOf(NotWriterException.class);
    }

    @DisplayName("신고 내역 리스트 조회 성공.")
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

    @DisplayName("해당 memberId로 등록한 신고가 하나도 없는 경우 빈 리스트를 반환.")
    @Test
    void getAccusationList_size_zero() {
        given(accusationRepository.findByMemberId(anyString())).willReturn(List.of());

        AccusationListResponse response = accusationService.getAccusationList("2");

        assertThat(response.getAccusations().size()).isEqualTo(0);
    }

    @DisplayName("신고 내용 수정 성공.")
    @Test
    void modifyAccusationContents() {
        AccusationContentsRequest request = new AccusationContentsRequest("제목 수정", "내용 수정");

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

    @DisplayName("관리자에 의해 처리가 된 상태(완료 or 반려)일 경우 신고 내용 수정시 NotModifiedAccusationException 발생.")
    @Test
    void modifyAccusationContents_not_registered_status() {
        AccusationContentsRequest request = new AccusationContentsRequest("제목 수정", "내용 수정");
        accusation.process(AccusationStatus.REJECTED, "");

        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        assertThatThrownBy(() -> accusationService.modifyAccusationContents(ACCUSATION_ID, request))
                .isInstanceOf(NotModifiedAccusationException.class);
    }
    
}
