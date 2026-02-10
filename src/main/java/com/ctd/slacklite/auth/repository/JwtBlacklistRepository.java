package com.ctd.slacklite.auth.repository;

import com.ctd.slacklite.auth.model.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {

    boolean existsByToken(String token);
}
