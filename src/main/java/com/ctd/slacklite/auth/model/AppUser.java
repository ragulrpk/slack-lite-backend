package com.ctd.slacklite.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user", schema = "slack_lite_db")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, length = 150)
    private String email;

    @Column(unique = true, length = 20)
    private String mobile;

    @Column(name = "jwt_token")
    private String jwtToken;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "login_count")
    private Integer loginCount = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_dt", nullable = false)
    private LocalDate createdDt;

    @Column(name = "created_time", nullable = false)
    private LocalTime createdTime;

    @Column(name = "updated_dt")
    private LocalDate updatedDt;

    @Column(name = "updated_time")
    private LocalTime updatedTime;

    // getters and setters

}
