package com.example.accusation.member.domain.repositories;

import com.example.accusation.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
