package com.ctd.slacklite.auth.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessDto {

    private Long userId;
    private String username;
    private List<String> permissionList;
}

