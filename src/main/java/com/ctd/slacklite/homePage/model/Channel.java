package com.ctd.slacklite.homePage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "channel",schema = "slack_lite_db")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "channel_type_id")
    private Long channelTypeId;

    @Column(name = "group_mode_id")
    private Long groupModeId;

    private String name;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_message_id")
    private Long lastMessageId;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;
}
