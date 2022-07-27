package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.admin.AccusationStatusRequest;
import com.mungta.accusation.api.dto.admin.AdminAccusationListResponse;
import com.mungta.accusation.api.dto.admin.AdminAccusationResponse;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationStatus;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.service.exception.NotFoundAccusationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminAccusationService {

    private final AccusationRepository accusationRepository;

    public AdminAccusationResponse getAccusation(final long id) {
        Accusation accusation = getAccusationById(id);
        return AdminAccusationResponse.of(accusation);
    }

    private Accusation getAccusationById(long id) {
        return accusationRepository.findById(id)
                .orElseThrow(() -> new NotFoundAccusationException("등록되지 않은 신고글입니다."));
    }

    public AdminAccusationListResponse getAccusationList() {
        return AdminAccusationListResponse.of(accusationRepository.findAll());
    }

    @Transactional
    public AdminAccusationResponse processAccusation(final long id, final AccusationStatusRequest request) {
        Accusation accusation = getAccusationById(id);
        accusation.process(request.getAccusationStatus(), request.getResultComment());

        if (accusation.getAccusationStatus() == AccusationStatus.COMPLETED) {
            // 회원 시스템으로 신고당한 사람 ID 전송
            // 신고당한 사람에게 이메일 전송할 건지 검토..
        }

        return AdminAccusationResponse.of(accusation);
    }

}
