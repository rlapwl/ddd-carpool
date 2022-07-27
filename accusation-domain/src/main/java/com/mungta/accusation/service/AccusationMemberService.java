package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.AccusationMemberListResponse;
import com.mungta.accusation.domain.PartyInfo;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.feign.PartyServiceClient;
import com.mungta.accusation.feign.UserServiceClient;
import com.mungta.accusation.feign.dto.PartyResponse;
import com.mungta.accusation.feign.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationMemberService {

    private final AccusationRepository accusationRepository;

    private final PartyServiceClient partyServiceClient;
    private final UserServiceClient userServiceClient;

    public AccusationMemberListResponse getAccusationMembers(final String memberId, final long partyId) {
        PartyResponse party = partyServiceClient.getParty(partyId);

        List<String> memberIds = party.getMemberIds().stream()
                .filter(id -> !id.equals(memberId))
                .collect(Collectors.toList());
        List<UserResponse> userList = userServiceClient.getUserList(memberIds);

        List<String> preAccusedMemberIdList = getAccusedMemberIdByPartyId(memberId, party);

        return AccusationMemberListResponse.of(party, getMemberResponse(userList, preAccusedMemberIdList));
    }

    private List<String> getAccusedMemberIdByPartyId(String memberId, PartyResponse party) {
        PartyInfo partyInfo = PartyInfo.builder()
                .partyId(party.getPartyId())
                .placeOfDeparture(party.getPlaceOfDeparture())
                .destination(party.getDestination())
                .startedDateTime(party.getStartedDateTime())
                .build();

        return  accusationRepository.findByMemberIdAndPartyInfo(memberId, partyInfo).stream()
                .map(accusation -> accusation.getAccusedMember().getId())
                .collect(Collectors.toList());
    }

    private List<AccusationMemberListResponse.MemberResponse> getMemberResponse(List<UserResponse> userList, List<String> preAccusedMemberIdList) {
        return userList.stream()
                .map(userResponse -> {
                    byte[] byteEnc64 = Base64.encodeBase64(userResponse.getImage());

                    return AccusationMemberListResponse.MemberResponse.builder()
                            .id(userResponse.getId())
                            .name(userResponse.getName())
                            .emailAddress(userResponse.getEmailAddress())
                            .image(new String(byteEnc64, StandardCharsets.UTF_8))
                            .isAccused(preAccusedMemberIdList.contains(userResponse.getId()))
                            .build();
                }).collect(Collectors.toList());
    }

}
