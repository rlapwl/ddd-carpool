package com.example.accusation.partyhistory.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PARTY_INFOS")
public class PartyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PARTY_ID", nullable = false)
    private Long partyId;

    @Column(name = "PLACE_OF_DEPARTURE", nullable = false)
    private String placeOfDeparture;

    @Column(name = "DESTINATION", nullable = false)
    private String destination;

    @Column(name = "STARTED_DATE_TIME", nullable = false)
    private LocalDateTime startedDateTime;
    
}
