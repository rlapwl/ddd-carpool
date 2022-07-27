package com.mungta.accusation.feign.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String emailAddress;
    private byte[] image;

}
