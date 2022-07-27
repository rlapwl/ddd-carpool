package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.AccusationMemberListResponse;
import com.mungta.accusation.service.AccusationMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ACCUSATION MEMBER", description = "신고 대상 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mungta/accusation/party-members")
public class AccusationMemberController {

    private final AccusationMemberService accusationMemberService;

    @GetMapping
    public ResponseEntity<AccusationMemberListResponse> getAccusationMembers(@RequestParam String memberId,
                                                                             @RequestParam long partyId) {
        AccusationMemberListResponse response = accusationMemberService.getAccusationMembers(memberId, partyId);
        return ResponseEntity.ok(response);
    }

}
