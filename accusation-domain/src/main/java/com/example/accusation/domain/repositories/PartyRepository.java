package com.example.accusation.domain.repositories;

import com.example.accusation.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findByPartyId(long partyId);

}
