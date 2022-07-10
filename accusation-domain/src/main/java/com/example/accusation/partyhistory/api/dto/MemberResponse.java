package com.example.accusation.partyhistory.api.dto;

import com.example.accusation.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private long id;
    private String name;

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getMemberId(), member.getMemberName());
    }

}
