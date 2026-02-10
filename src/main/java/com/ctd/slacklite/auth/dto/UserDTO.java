package com.ctd.slacklite.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String mobile;
    private Boolean isActive;
    private Integer loginCount;

    public UserDTO() {

    }

    // getters and setters
}
