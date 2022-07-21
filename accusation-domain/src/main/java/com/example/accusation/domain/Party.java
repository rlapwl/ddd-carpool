package com.example.accusation.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PARTIES")
public class Party {

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
    private String startedDateTime;

    @Builder
    public Party(Long partyId, String placeOfDeparture, String destination, String startedDateTime) {
        this.partyId = partyId;
        this.placeOfDeparture = placeOfDeparture;
        this.destination = destination;
        this.startedDateTime = startedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Party partyInfo = (Party) o;
        return id != null && Objects.equals(id, partyInfo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
