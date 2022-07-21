package com.example.accusation.service;

import com.example.accusation.api.dto.*;
import com.example.accusation.domain.Accusation;
import com.example.accusation.domain.AccusationContents;
import com.example.accusation.domain.AccusedMember;
import com.example.accusation.domain.repositories.AccusationRepository;
import com.example.accusation.domain.repositories.PartyRepository;
import com.example.accusation.domain.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationService {

    private final AccusationRepository accusationRepository;
    private final PartyRepository partyRepository;

    @Transactional
    public long addAccusation(final AccusationRequest request) {
        return accusationRepository.save(
                Accusation.builder()
                        .memberId(request.getMemberId())
                        .accusedMember(
                                new AccusedMember(request.getAccusedMemberId(), request.getAccusedMemberName())
                        )
                        .party(
                                getParty(request.getParty())
                        )
                        .accusationContents(
                                new AccusationContents(request.getAccusationContents().getTitle(),
                                        request.getAccusationContents().getDesc())
                        )
                        .build()
        ).getId();
    }

    private Party getParty(PartyRequest partyRequest) {
        return partyRepository.findByPartyId(partyRequest.getPartyId())
                .orElseGet(() ->
                        Party.builder()
                                .partyId(partyRequest.getPartyId())
                                .placeOfDeparture(partyRequest.getPlaceOfDeparture())
                                .destination(partyRequest.getDestination())
                                .startedDateTime(partyRequest.getStartedDateTime())
                                .build()
                );
    }

    public AccusationResponse getAccusation(final long id, final long memberId) {
        Accusation accusation = getAccusationById(id);

        // 회원이 쓴글인지 확인
        validateMember(accusation, memberId);

        return AccusationResponse.of(accusation);
    }

    private Accusation getAccusationById(long id) {
        return accusationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 신고글입니다."));
    }

    private void validateMember(Accusation accusation, long memberId) {
        if (accusation.isNotRegisteredMember(memberId)) {
            throw new IllegalArgumentException("신고를 하지 않은 회원이므로 신고 내용을 볼 권한이 없습니다.");
        }
    }

    public AccusationListResponse getAccusationList(final long memberId) {
        // 관리자인 경우
        //return AccusationListResponse.of(accusationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDateTime")));
        return AccusationListResponse.of(accusationRepository.findByMemberId(memberId));
    }

    @Transactional
    public AccusationResponse modifyAccusationContents(final long id, final AccusationContentsRequest request) {
        Accusation accusation = getAccusationById(id);

        validateStatus(accusation);

        accusation.modifyAccusationContents(
                new AccusationContents(request.getTitle(), request.getDesc())
        );
        return AccusationResponse.of(accusation);
    }

    private void validateStatus(Accusation accusation) {
        if (accusation.isNotRegisteredStatus()) {
            throw new RuntimeException("이미 신고 처리된 상태라 내용을 수정할 수 없습니다.");
        }
    }

    @Transactional
    public void deleteAccusation(final long id) {
        accusationRepository.deleteById(id);
    }

}
