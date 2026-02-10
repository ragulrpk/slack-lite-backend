package com.ctd.slacklite.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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

