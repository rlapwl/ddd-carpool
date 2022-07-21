package com.example.accusation.api;

import com.example.accusation.api.dto.*;
import com.example.accusation.domain.AccusationStatus;
import com.example.accusation.service.AccusationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccusationController.class)
class AccusationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccusationService accusationService;

    @DisplayName("신고 등록")
    @Test
    void addAccusation() throws Exception {
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

        doReturn(1L)
                .when(accusationService).addAccusation(any());

        ResultActions result = mockMvc.perform(
                post("/mungta/accusations")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        );

        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/mungta/accusations/1"));

    }

    @DisplayName("신고 조회")
    @Test
    void getAccusation() throws Exception {
        AccusationResponse response = AccusationResponse.builder()
                .id(1L)
                .party(PartyResponse.builder()
                        .partyId(1L)
                        .placeOfDeparture("정자역")
                        .destination("사당역")
                        .startedDateTime("2022-07-18 18:20")
                        .build()
                )
                .accusedMemberName("통통이")
                .accusationContents(
                        AccusationContentsResponse.builder()
                                .title("신고합니다.")
                                .desc("내용")
                                .build()
                )
                .accusationStatus(AccusationStatus.REGISTERED)
                .build();
        
        doReturn(response)
                .when(accusationService).getAccusation(anyLong(), anyLong());

        ResultActions result = mockMvc.perform(
                get("/mungta/accusations/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", "1")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.party.partyId").value(1))
                .andExpect(jsonPath("$.party.placeOfDeparture").value("정자역"))
                .andExpect(jsonPath("$.party.destination").value("사당역"))
                .andExpect(jsonPath("$.party.startedDateTime").value("2022-07-18 18:20"))
                .andExpect(jsonPath("$.accusedMemberName").value("통통이"))
                .andExpect(jsonPath("$.accusationContents.title").value("신고합니다."))
                .andExpect(jsonPath("$.accusationContents.desc").value("내용"))
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"));

    }

}
