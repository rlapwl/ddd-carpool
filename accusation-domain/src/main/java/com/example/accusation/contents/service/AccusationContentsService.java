package com.example.accusation.contents.service;

import com.example.accusation.contents.api.dto.AccusationContentsRequest;
import com.example.accusation.contents.api.dto.AccusationContentsResponse;
import com.example.accusation.contents.api.dto.ContentsResponse;
import com.example.accusation.contents.domain.AccusationContents;
import com.example.accusation.contents.domain.Contents;
import com.example.accusation.contents.domain.repositories.AccusationContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationContentsService {

    private final AccusationContentsRepository accusationContentsRepository;

    @Transactional
    public long addContents(AccusationContentsRequest request) {
        AccusationContents accusationContents = AccusationContents.builder()
                .partyId(request.getPartyId())
                .memberId(request.getMemberId())
                .accusedMemberId(request.getAccusedMemberId())
                .contents(new Contents(request.getTitle(), request.getDesc()))
                .build();

        return accusationContentsRepository.save(accusationContents).getId();
    }

    public AccusationContentsResponse getContents(long id) {
        // 회원이 쓴글인지 확인
        AccusationContents accusationContents = accusationContentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 신고글입니다."));

        return AccusationContentsResponse.builder()
                .id(accusationContents.getId())
                .contents(
                        ContentsResponse.of(accusationContents.getContents())
                )
                .createdAt(
                        accusationContents.getCreatedAt().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build();
    }

}
