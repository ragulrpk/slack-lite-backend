package com.ctd.slacklite.homePage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponseDTO {
    private String type;
    private Long  channelId;
    private String username;
    private boolean existing;
}
