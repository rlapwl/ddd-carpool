package com.example.accusation.service;

import com.example.accusation.api.dto.*;
import com.example.accusation.domain.Accusation;
import com.example.accusation.domain.AccusationContents;
import com.example.accusation.domain.AccusedMember;
import com.example.accusation.domain.repositories.AccusationRepository;
import com.example.accusation.domain.PartyInfo;
import com.example.accusation.service.exception.NotFoundAccusationException;
import com.example.accusation.service.exception.NotModifiedAccusationException;
import com.example.accusation.service.exception.NotWriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationService {

    private final AccusationRepository accusationRepository;

    @Transactional
    public long addAccusation(final AccusationRequest request) {
        PartyInfoRequest partyInfo = request.getPartyInfo();
        AccusedMemberRequest accusedMember = request.getAccusedMember();

        return accusationRepository.save(
                Accusation.builder()
                        .memberId(request.getMemberId())
                        .partyInfo(
                                PartyInfo.builder()
                                        .partyId(partyInfo.getPartyId())
                                        .placeOfDeparture(partyInfo.getPlaceOfDeparture())
                                        .destination(partyInfo.getDestination())
                                        .startedDateTime(partyInfo.getStartedDateTime())
                                        .build()
                        )
                        .accusedMember(
                                AccusedMember.builder()
                                        .id(accusedMember.getId())
                                        .name(accusedMember.getName())
                                        .emailAddress(accusedMember.getEmailAddress())
                                        .build()
                        )
                        .accusationContents(
                                new AccusationContents(request.getAccusationContents().getTitle(),
                                        request.getAccusationContents().getDesc())
                        )
                        .build()
        ).getId();
    }

    public AccusationResponse getAccusation(final long id, final String memberId) {
        Accusation accusation = getAccusationById(id);

        // 회원이 쓴글인지 확인
        validateWriter(accusation, memberId);

        return AccusationResponse.of(accusation);
    }

    private Accusation getAccusationById(long id) {
        return accusationRepository.findById(id)
                .orElseThrow(() -> new NotFoundAccusationException("등록되지 않은 신고글입니다."));
    }

    private void validateWriter(Accusation accusation, String memberId) {
        if (accusation.isNotWriter(memberId)) {
            throw new NotWriterException("신고를 하지 않은 회원이므로 신고 내용을 볼 권한이 없습니다.");
        }
    }

    public AccusationListResponse getAccusationList(final String memberId) {
        return AccusationListResponse.of(accusationRepository.findByMemberId(memberId));
    }

    @Transactional
    public AccusationResponse modifyAccusationContents(final long id, final AccusationContentsRequest request) {
        Accusation accusation = getAccusationById(id);

        validateStatus(accusation);

        accusation.modifyAccusationContents(new AccusationContents(request.getTitle(), request.getDesc()));
        return AccusationResponse.of(accusation);
    }

    private void validateStatus(Accusation accusation) {
        if (accusation.isNotRegisteredStatus()) {
            throw new NotModifiedAccusationException("이미 신고 처리된 상태라 내용을 수정할 수 없습니다.");
        }
    }

    @Transactional
    public void deleteAccusation(final long id) {
        accusationRepository.deleteById(id);
    }

}
