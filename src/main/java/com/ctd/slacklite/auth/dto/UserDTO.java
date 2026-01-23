package com.ctd.slacklite.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String mobile;
    private Boolean isActive;
    private Integer loginCount;

    // getters and setters
}
