package com.ctd.slacklite.homePage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "channel_member",schema = "slack_lite_db")
@Getter
@Setter
public class ChannelMember {

    @Id
    private Long id;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "is_active")
    private Boolean isActive;
}
