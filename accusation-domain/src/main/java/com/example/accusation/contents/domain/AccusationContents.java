package com.example.accusation.contents.domain;

import com.example.accusation.partyhistory.domain.PartyInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ACCUSATION_CONTENTS")
public class AccusationContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PARTY_INFO_ID", nullable = false)
    private PartyInfo partyInfo;

    @Embedded
    private Contents contents;

}
