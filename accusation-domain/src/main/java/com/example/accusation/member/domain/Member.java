package com.example.accusation.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBERS")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DEPARTMENT", nullable = false)
    private String department;

    @Column(name = "PROFILE_IMAGE", nullable = false)
    private String profileImage;

    @Builder
    public Member(Long memberId) {
        this.memberId = memberId;
    }

}
