package com.ctd.slacklite.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {

    private Long userId;
    private String username;
    private String token;
}
