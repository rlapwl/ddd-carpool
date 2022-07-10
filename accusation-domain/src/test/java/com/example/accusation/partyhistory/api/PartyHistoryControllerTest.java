package com.example.accusation.partyhistory.api;

import com.example.accusation.partyhistory.api.dto.MemberResponse;
import com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse;
import com.example.accusation.partyhistory.service.PartyHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse.PartyHistoryResponse;

@WebMvcTest(PartyHistoryController.class)
class PartyHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartyHistoryService partyHistoryService;

    @DisplayName("회원이 최근 3일동안 이용한 카풀시스템 내역(출발지, 도착지, 함께 이용한 회원 정보 등)을 조회한다.")
    @Test
    void getPartyHistoryList() throws Exception {

        List<MemberResponse> memberResponseList = List.of(new MemberResponse(2L, "member2"));

        PartyHistoryListResponse response = new PartyHistoryListResponse(
                List.of(
                        PartyHistoryResponse.builder()
                                .partyId(1L)
                                .startPlace("정자역")
                                .endPlace("사당역")
                                .createdAt("2022-07-05 18:30")
                                .memberList(memberResponseList)
                                .build()
                )
        );

        doReturn(response)
                .when(partyHistoryService).getPartyHistoryList(anyLong());

        ResultActions result = mockMvc.perform(
                get("/party-history")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", "1")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.partyHistoryList").exists())
                .andExpect(jsonPath("$.partyHistoryList.length()").value(1))
                .andExpect(jsonPath("$.partyHistoryList[0].partyId").value(1L))
                .andExpect(jsonPath("$.partyHistoryList[0].startPlace").value("정자역"))
                .andExpect(jsonPath("$.partyHistoryList[0].endPlace").value("사당역"))
                .andExpect(jsonPath("$.partyHistoryList[0].createdAt").value("2022-07-05 18:30"))
                .andExpect(jsonPath("$.partyHistoryList[0].memberList.length()").value(1))
                .andExpect(jsonPath("$.partyHistoryList[0].memberList[0].id").value(2L))
                .andExpect(jsonPath("$.partyHistoryList[0].memberList[0].name").value("member2"));

    }

}
