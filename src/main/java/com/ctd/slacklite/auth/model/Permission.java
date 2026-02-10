package com.ctd.slacklite.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_code", nullable = false, unique = true)
    private String code;

    @Column(name = "permission_description")
    private String description;
}
