package com.example.accusation.domain.repositories;

import com.example.accusation.domain.Accusation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccusationRepository extends JpaRepository<Accusation, Long> {

    List<Accusation> findByMemberId(String memberId);

}
