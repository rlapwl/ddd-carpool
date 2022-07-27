package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.AccusationContentsRequest;
import com.mungta.accusation.api.dto.AccusationListResponse;
import com.mungta.accusation.api.dto.AccusationRequest;
import com.mungta.accusation.api.dto.AccusationResponse;
import com.mungta.accusation.service.AccusationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Tag(name = "ACCUSATION", description = "신고 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mungta/accusations")
public class AccusationController {

    private final AccusationService accusationService;

    @Operation(summary = "신고 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신고 등록 성공")
    })
    @PostMapping
    public ResponseEntity addAccusation(@Valid @RequestBody AccusationRequest request) {
        long id = accusationService.addAccusation(request);
        return ResponseEntity.created(URI.create("/mungta/accusations/" + id)).build();
    }

    @Operation(summary = "신고 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 조회 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccusationResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccusationResponse> getAccusation(@Parameter(description = "신고 ID") @PathVariable long id,
                                                            @RequestParam String memberId) {
        AccusationResponse response = accusationService.getAccusation(id, memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신고 내역 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 내역 조회 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccusationListResponse.class))})
    })
    @GetMapping
    public ResponseEntity<AccusationListResponse> getAccusationList(@RequestParam String memberId) {
        AccusationListResponse response = accusationService.getAccusationList(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신고 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 내용 수정 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccusationResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccusationResponse> modifyAccusation(@Parameter(description = "신고 ID") @PathVariable long id,
                                                               @Valid @RequestBody AccusationContentsRequest request) {
        AccusationResponse response = accusationService.modifyAccusationContents(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신고 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "신고 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccusation(@Parameter(description = "신고 ID") @PathVariable long id) {
        accusationService.deleteAccusation(id);
        return ResponseEntity.noContent().build();
    }

}
