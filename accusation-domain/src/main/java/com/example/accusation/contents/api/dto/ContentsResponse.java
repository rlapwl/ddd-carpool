package com.example.accusation.contents.api.dto;

import com.example.accusation.contents.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContentsResponse {

    private String title;
    private String desc;

    public static ContentsResponse of(Contents contents) {
        return new ContentsResponse(contents.getTitle(), contents.getDescription());
    }

}
