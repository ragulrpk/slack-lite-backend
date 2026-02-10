package com.ctd.slacklite.auth.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_user_image", schema = "slack_lite_db")
@Data
public class AppUserImage {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "image", nullable = false, columnDefinition = "BYTEA")
    private byte[] image;

    @Column(name = "content_type")
    private String contentType;

}

