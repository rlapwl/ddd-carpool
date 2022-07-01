package com.example.accusation.contents.api;

import com.example.accusation.contents.api.dto.AccusationContentsRequest;
import com.example.accusation.contents.service.AccusationContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accusation")
public class AccusationContentsController {

    private final AccusationContentsService accusationContentsService;

    @PostMapping
    public ResponseEntity<?> addContents(@RequestBody AccusationContentsRequest request) {
        long id = accusationContentsService.addContents(request);
        return ResponseEntity.created(URI.create("/accusation/" + id)).build();
    }

}
