package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.AccusationContentsRequest;
import com.mungta.accusation.api.dto.AccusationListResponse;
import com.mungta.accusation.api.dto.AccusationResponse;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusedMember;
import com.mungta.accusation.domain.PartyInfo;
import com.mungta.accusation.service.AccusationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungta.accusation.domain.AccusationContents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.mungta.accusation.constants.AccusationTestSample.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccusationController.class)
class AccusationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccusationService accusationService;

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
        accusation.setId(ACCUSATION_ID);
        accusation.setCreatedDateTime(LocalDateTime.now());
    }

    @DisplayName("신고 등록")
    @Test
    void addAccusation() throws Exception {

        doReturn(ACCUSATION_ID)
                .when(accusationService).addAccusation(any());

        ResultActions result = mockMvc.perform(
                post("/mungta/accusations")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ACCUSATION_REQUEST))
        );

        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/mungta/accusations/" + ACCUSATION_ID));
    }

    @DisplayName("신고 조회")
    @Test
    void getAccusation() throws Exception {
        AccusationResponse response = AccusationResponse.of(accusation);
        
        doReturn(response)
                .when(accusationService).getAccusation(anyLong(), anyString());

        ResultActions result = mockMvc.perform(
                get("/mungta/accusations/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", MEMBER_ID)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusedMemberName").value(ACCUSED_MEMBER_NAME))
                .andExpect(jsonPath("$.partyInfo.partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.partyInfo.placeOfDeparture").value(PLACE_OF_DEPARTURE))
                .andExpect(jsonPath("$.partyInfo.destination").value(DESTINATION))
                .andExpect(jsonPath("$.partyInfo.startedDateTime").value(STARTED_DATE_TIME))
                .andExpect(jsonPath("$.accusationContents.title").value(CONTENTS_TITLE))
                .andExpect(jsonPath("$.accusationContents.desc").value(CONTENTS_DESC))
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"));
    }

    @DisplayName("신고 내역 리스트 조회")
    @Test
    void getAccusationList() throws Exception {
        AccusationListResponse response = AccusationListResponse.of(List.of(accusation));

        doReturn(response)
                .when(accusationService).getAccusationList(anyString());

        ResultActions result = mockMvc.perform(
                get("/mungta/accusations")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", MEMBER_ID)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accusations").exists())
                .andExpect(jsonPath("$.accusations.length()").value(1))
                .andExpect(jsonPath("$.accusations[0].id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusations[0].partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.accusations[0].title").value(CONTENTS_TITLE))
                .andExpect(jsonPath("$.accusations[0].accusationStatus").value("REGISTERED"))
                .andExpect(jsonPath("$.accusations[0].createdDateTime").exists());
    }

    @DisplayName("신고 내용 수정")
    @Test
    void modifyAccusation() throws Exception {
        accusation.modifyAccusationContents(new AccusationContents("제목 수정", "내용 수정"));
        AccusationResponse response = AccusationResponse.of(accusation);

        doReturn(response)
                .when(accusationService).modifyAccusationContents(anyLong(), any());

        ResultActions result = mockMvc.perform(
                put("/mungta/accusations/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(
                                        AccusationContentsRequest.builder()
                                                .title("제목 수정")
                                                .desc("내용 수정")
                                                .build()
                                )
                        )
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusedMemberName").value(ACCUSED_MEMBER_NAME))
                .andExpect(jsonPath("$.partyInfo.partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.partyInfo.placeOfDeparture").value(PLACE_OF_DEPARTURE))
                .andExpect(jsonPath("$.partyInfo.destination").value(DESTINATION))
                .andExpect(jsonPath("$.partyInfo.startedDateTime").value(STARTED_DATE_TIME))
                .andExpect(jsonPath("$.accusationContents.title").value("제목 수정"))
                .andExpect(jsonPath("$.accusationContents.desc").value("내용 수정"))
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"));
    }

    @DisplayName("신고 삭제")
    @Test
    void deleteAccusation() throws Exception {
        doNothing()
                .when(accusationService).deleteAccusation(anyLong());

        ResultActions result = mockMvc.perform(
                delete("/mungta/accusations/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

}
