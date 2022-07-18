package com.example.accusation.partyhistory.domain.repositories;

import com.example.accusation.partyhistory.domain.PartyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyInfoRepository extends JpaRepository<PartyInfo, Long> {

}
