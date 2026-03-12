package com.ctd.slacklite.homePage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "channel_type",schema = "slack_lite_db")
@Getter
@Setter
public class ChannelType {

    @Id
    private Long channelTypeId;
    private String code;
    private String description;

}
