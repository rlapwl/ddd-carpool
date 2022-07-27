package com.example.accusation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.accusation.constants.AccusationTestSample.*;
import static org.assertj.core.api.Assertions.assertThat;

class AccusationTest {

    private Accusation accusation;

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
    }

    @DisplayName("신고를 등록한 회원이 아닐 경우 true 반환.")
    @Test
    void isNotWriter_true() {
        boolean result = accusation.isNotWriter("2");
        assertThat(result).isEqualTo(true);
    }

    @DisplayName("신고를 등록한 회원일 경우 false 반환.")
    @Test
    void isNotWriter_false() {
        boolean result = accusation.isNotWriter(MEMBER_ID);
        assertThat(result).isEqualTo(false);
    }

    @DisplayName("신고 상태가 REGISTERED 가 아닌 경우 true 반환.")
    @Test
    void isNotRegisteredStatus_true() {
        accusation.process(AccusationStatus.REJECTED, "신고 사유가 적합하지 않음.");

        boolean result = accusation.isNotRegisteredStatus();

        assertThat(result).isEqualTo(true);
    }

    @DisplayName("신고 상태가 REGISTERED 인 경우 false 반환.")
    @Test
    void isNotRegisteredStatus_false() {
        boolean result = accusation.isNotRegisteredStatus();

        assertThat(result).isEqualTo(false);
    }

    @DisplayName("신고 제목, 신고 내용 수정")
    @Test
    void modifyAccusationContents() {
        AccusationContents accusationContents = new AccusationContents("제목 수정", "내용 수정");

        accusation.modifyAccusationContents(accusationContents);

        assertThat(accusation.getAccusationContents()).isEqualTo(accusationContents);
    }

    @DisplayName("신고 상태 변경하면서 코멘트 내용도 추가.")
    @Test
    void process() {
        accusation.process(AccusationStatus.REJECTED, "신고 사유가 적합하지 않음.");

        assertThat(accusation.getAccusationStatus()).isEqualTo(AccusationStatus.REJECTED);
        assertThat(accusation.getResultComment()).isEqualTo("신고 사유가 적합하지 않음.");
    }

}