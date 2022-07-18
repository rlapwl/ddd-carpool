package com.example.accusation.contents.domain.repositories;

import com.example.accusation.contents.domain.Accusation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccusationRepository extends JpaRepository<Accusation, Long> {

}
