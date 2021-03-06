package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.*;
import com.mungta.accusation.api.dto.*;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationContents;
import com.mungta.accusation.domain.AccusedMember;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.domain.PartyInfo;
import com.mungta.accusation.service.exception.NotFoundAccusationException;
import com.mungta.accusation.service.exception.NotModifiedAccusationException;
import com.mungta.accusation.service.exception.NotWriterException;
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

        // ????????? ???????????? ??????
        validateWriter(accusation, memberId);

        return AccusationResponse.of(accusation);
    }

    private Accusation getAccusationById(long id) {
        return accusationRepository.findById(id)
                .orElseThrow(() -> new NotFoundAccusationException("???????????? ?????? ??????????????????."));
    }

    private void validateWriter(Accusation accusation, String memberId) {
        if (accusation.isNotWriter(memberId)) {
            throw new NotWriterException("????????? ?????? ?????? ??????????????? ?????? ????????? ??? ????????? ????????????.");
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
            throw new NotModifiedAccusationException("?????? ?????? ????????? ????????? ????????? ????????? ??? ????????????.");
        }
    }

    @Transactional
    public void deleteAccusation(final long id) {
        accusationRepository.deleteById(id);
    }

}
