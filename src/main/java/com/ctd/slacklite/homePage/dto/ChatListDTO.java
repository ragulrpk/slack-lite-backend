package com.ctd.slacklite.homePage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatListDTO {
    private Long channelId;
    private String channelType;
    private String displayName;
    private LocalDateTime lastMessageAt;

}
