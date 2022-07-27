package com.example.accusation.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PartyInfo {

    @Column(name = "PARTY_ID", nullable = false)
    private Long partyId;

    @Column(name = "PLACE_OF_DEPARTURE", nullable = false)
    private String placeOfDeparture;

    @Column(name = "DESTINATION", nullable = false)
    private String destination;

    @Column(name = "PARTY_STARTED_DATE_TIME", nullable = false)
    private String startedDateTime;

    @Builder
    public PartyInfo(Long partyId, String placeOfDeparture, String destination, String startedDateTime) {
        this.partyId = partyId;
        this.placeOfDeparture = placeOfDeparture;
        this.destination = destination;
        this.startedDateTime = startedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartyInfo partyInfo = (PartyInfo) o;
        return Objects.equals(partyId, partyInfo.partyId)
                && Objects.equals(placeOfDeparture, partyInfo.placeOfDeparture)
                && Objects.equals(destination, partyInfo.destination)
                && Objects.equals(startedDateTime, partyInfo.startedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, placeOfDeparture, destination, startedDateTime);
    }

}
