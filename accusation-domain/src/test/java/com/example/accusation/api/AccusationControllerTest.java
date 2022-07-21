package com.example.accusation.api;

import com.example.accusation.api.dto.*;
import com.example.accusation.domain.*;
import com.example.accusation.service.AccusationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
                .memberId(1L)
                .accusedMember(
                        new AccusedMember(2L, "통통이")
                )
                .party(
                        Party.builder()
                                .partyId(1L)
                                .placeOfDeparture("정자역")
                                .destination("사당역")
                                .startedDateTime("2022-07-18 18:20")
                                .build()
                )
                .accusationContents(
                        new AccusationContents("신고합니다.", "내용")
                )
                .build();
        accusation.setId(1L);
        accusation.setCreatedDateTime(LocalDateTime.now());
    }

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
        AccusationResponse response = AccusationResponse.of(accusation);
        
        doReturn(response)
                .when(accusationService).getAccusation(anyLong(), anyLong());

        ResultActions result = mockMvc.perform(
                get("/mungta/accusations/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", "1")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.party.partyId").value(1))
                .andExpect(jsonPath("$.party.placeOfDeparture").value("정자역"))
                .andExpect(jsonPath("$.party.destination").value("사당역"))
                .andExpect(jsonPath("$.party.startedDateTime").value("2022-07-18 18:20"))
                .andExpect(jsonPath("$.accusedMemberName").value("통통이"))
                .andExpect(jsonPath("$.accusationContents.title").value("신고합니다."))
                .andExpect(jsonPath("$.accusationContents.desc").value("내용"))
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"));
    }

    @DisplayName("신고 내역 리스트 조회")
    @Test
    void getAccusationList() throws Exception {
        AccusationListResponse response = AccusationListResponse.of(List.of(accusation));

        doReturn(response)
                .when(accusationService).getAccusationList(anyLong());

        ResultActions result = mockMvc.perform(
                get("/mungta/accusations")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", "1")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accusations").exists())
                .andExpect(jsonPath("$.accusations.length()").value(1))
                .andExpect(jsonPath("$.accusations[0].id").value(1))
                .andExpect(jsonPath("$.accusations[0].partyId").value(1))
                .andExpect(jsonPath("$.accusations[0].title").value("신고합니다."))
                .andExpect(jsonPath("$.accusations[0].accusationStatus").value("REGISTERED"))
                .andExpect(jsonPath("$.accusations[0].createdDateTime").exists());
    }

    @DisplayName("신고 내용 수정")
    @Test
    void modifyAccusation() throws Exception {
        accusation.getAccusationContents().modify(new AccusationContents("제목 수정", "내용 수정"));
        AccusationResponse response = AccusationResponse.of(accusation);

        doReturn(response)
                .when(accusationService).modifyAccusationContents(anyLong(), any());

        ResultActions result = mockMvc.perform(
                put("/mungta/accusations/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(AccusationContentsRequest.builder()
                                        .title("제목 수정")
                                        .desc("내용 수정")
                                        .build()
                                )
                        )
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.party.partyId").value(1))
                .andExpect(jsonPath("$.party.placeOfDeparture").value("정자역"))
                .andExpect(jsonPath("$.party.destination").value("사당역"))
                .andExpect(jsonPath("$.party.startedDateTime").value("2022-07-18 18:20"))
                .andExpect(jsonPath("$.accusedMemberName").value("통통이"))
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
                delete("/mungta/accusations/1")
        );

        result.andExpect(status().isNoContent());
    }

}
