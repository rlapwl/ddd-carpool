package com.example.accusation.partyhistory.api;

import com.example.accusation.partyhistory.api.dto.PartyHistoryListResponse;
import com.example.accusation.partyhistory.service.PartyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/party-history")
public class PartyHistoryController {

    private final PartyHistoryService partyHistoryService;

    @GetMapping
    public ResponseEntity<PartyHistoryListResponse> getPartyHistoryList(@RequestParam long memberId) {
        PartyHistoryListResponse response = partyHistoryService.getPartyHistoryList(memberId);
        return ResponseEntity.ok(response);
    }

}
