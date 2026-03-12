package com.ctd.slacklite.homePage.dto;

import java.time.LocalDateTime;

public interface ChatListProjection {

    Long getChannelId();
    String getChannelType();
    String getDisplayName();
    LocalDateTime getLastMessageAt();

}
