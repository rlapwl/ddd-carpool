package com.example.accusation.service;

import com.example.accusation.api.dto.AccusationContentsResponse;
import com.example.accusation.api.dto.PartyInfoResponse;
import com.example.accusation.api.dto.admin.AccusationStatusRequest;
import com.example.accusation.api.dto.admin.AdminAccusationListResponse;
import com.example.accusation.api.dto.admin.AdminAccusationResponse;
import com.example.accusation.domain.*;
import com.example.accusation.domain.repositories.AccusationRepository;
import com.example.accusation.service.exception.NotFoundAccusationException;
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

import static com.example.accusation.api.dto.admin.AdminAccusationListResponse.AdminAccusationInfoResponse;
import static com.example.accusation.constants.AccusationTestSample.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(value = MockitoExtension.class)
class AdminAccusationServiceTest {

    @InjectMocks
    @Spy
    private AdminAccusationService adminAccusationService;

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

    @DisplayName("신고 조회 성공.")
    @Test
    void getAccusation() {
        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        AdminAccusationResponse response = adminAccusationService.getAccusation(ACCUSATION_ID);

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
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED),
                () -> assertThat(response.getResultComment()).isEmpty()
        );
    }

    @DisplayName("파라미터로 받은 id로 accusation 객체를 찾을 수 없는 경우 NotFoundAccusationException 발생.")
    @Test
    void getAccusation_not_found_by_id() {
        given(accusationRepository.findById(anyLong()))
                .willThrow(new NotFoundAccusationException("등록되지 않은 신고글입니다."));

        assertThatThrownBy(() -> adminAccusationService.getAccusation(2L))
                .isInstanceOf(NotFoundAccusationException.class);
    }

    @DisplayName("신고 내역 리스트 조회 성공.")
    @Test
    void getAccusationList() {
        given(accusationRepository.findAll()).willReturn(List.of(accusation));

        AdminAccusationListResponse response = adminAccusationService.getAccusationList();
        List<AdminAccusationInfoResponse> responseList = response.getAccusations();

        assertAll(
                () -> assertThat(responseList.size()).isEqualTo(1),
                () -> assertThat(responseList.get(0)).isEqualTo(
                        AdminAccusationInfoResponse.builder()
                                .id(ACCUSATION_ID)
                                .partyId(PARTY_ID)
                                .memberId(MEMBER_ID)
                                .title(CONTENTS_TITLE)
                                .accusationStatus(AccusationStatus.REGISTERED)
                                .createdDateTime(nowDateTime.format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                ))
                                .build()
                )
        );
    }

    @DisplayName("신고 완료 처리 성공.")
    @Test
    void processAccusation_to_completed_status() {
        AccusationStatusRequest request = new AccusationStatusRequest(AccusationStatus.COMPLETED, "코멘트 없음");

        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        AdminAccusationResponse response = adminAccusationService.processAccusation(ACCUSATION_ID, request);

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
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.COMPLETED),
                () -> assertThat(response.getResultComment()).isEqualTo(request.getResultComment())
        );
    }

    @DisplayName("신고 반려 처리 성공.")
    @Test
    void processAccusation_to_rejected_status() {
        AccusationStatusRequest request = new AccusationStatusRequest(AccusationStatus.REJECTED, "신고 사유가 안됨.");

        given(accusationRepository.findById(anyLong())).willReturn(Optional.ofNullable(accusation));

        AdminAccusationResponse response = adminAccusationService.processAccusation(ACCUSATION_ID, request);

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
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REJECTED),
                () -> assertThat(response.getResultComment()).isEqualTo(request.getResultComment())
        );
    }

}
