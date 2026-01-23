package com.ctd.slacklite.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ctd.slacklite.auth.model.AppUser;

public interface AuthRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsernameAndIsActiveTrue(String username);
}
