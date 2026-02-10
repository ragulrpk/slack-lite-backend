package com.ctd.slacklite.auth.security;

import lombok.Getter;

import java.util.List;

@Getter
public class CustomerUserDetails {

    private final Long userId;
    private final String username;
    private final List<String> permissionList;

    public CustomerUserDetails(Long userId, String username, List<String> permissionList) {
        this.userId = userId;
        this.username = username;
        this.permissionList = permissionList;
    }
}
