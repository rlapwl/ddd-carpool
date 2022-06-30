package com.example.accusation.contents.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Contents {

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESC")
    private String description;

}
