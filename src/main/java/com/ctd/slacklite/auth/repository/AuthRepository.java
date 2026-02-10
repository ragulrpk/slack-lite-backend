package com.ctd.slacklite.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ctd.slacklite.auth.model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsernameAndIsActiveTrue(String username);

    @Query(
            value = """
            SELECT p.permission_code
            FROM slack_lite_db.user_role ur
            JOIN slack_lite_db.role r ON ur.role_id = r.role_id
            JOIN slack_lite_db.role_permission rp ON rp.role_id = r.role_id
            JOIN slack_lite_db.permission p ON p.permission_id = rp.permission_id
            WHERE ur.user_id = :userId
        """,
            nativeQuery = true
    )
    List<String> findPermissionBasedOnUserId(@Param("userId") Long userId);
}
