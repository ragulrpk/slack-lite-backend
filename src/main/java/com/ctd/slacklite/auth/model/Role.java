package com.ctd.slacklite.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role", schema = "slack_lite_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
}
