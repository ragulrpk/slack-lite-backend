package com.ctd.slacklite.auth.mapper;

import com.ctd.slacklite.auth.dto.UserDTO;
import com.ctd.slacklite.auth.model.AppUser;

public class UserMapper {

    private UserMapper() {
        // prevent object creation
    }

    public static UserDTO toUserDTO(AppUser user) {

        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setIsActive(user.getIsActive());
        dto.setLoginCount(user.getLoginCount());

        return dto;
    }
}
