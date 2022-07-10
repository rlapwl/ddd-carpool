package com.example.accusation.contents.api;

import com.example.accusation.contents.api.dto.AccusationContentsRequest;
import com.example.accusation.contents.service.AccusationContentsService;
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
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccusationContentsController.class)
class AccusationContentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccusationContentsService accusationContentsService;

    @DisplayName("신고 내용 등록")
    @Test
    void addContents() throws Exception {
        AccusationContentsRequest request = AccusationContentsRequest.builder()
                .partyId(1L)
                .memberId(1L)
                .accusedMemberId(2L)
                .title("신고합니다.")
                .desc("내용..")
                .build();

        doReturn(1L)
                .when(accusationContentsService).addContents(any());

        ResultActions result = mockMvc.perform(
                post("/accusation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
        );

        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/accusation/1"));

    }

}
