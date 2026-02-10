package com.ctd.slacklite.homePage.repository;

import com.ctd.slacklite.homePage.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    // Native query for fetching user's chats
    @Query(
            value = """
            SELECT c.id AS channel_id,
                   ct.code AS channel_type,
                   CASE 
                       WHEN ct.code = 'DM' THEN u.username
                       ELSE c.name
                   END AS name,
                   c.last_message_at AS last_message_at
            FROM slack_lite_db.channel c
            JOIN slack_lite_db.channel_member cm ON cm.channel_id = c.id
            JOIN slack_lite_db.channel_types ct ON ct.id = c.channel_type_id
            LEFT JOIN slack_lite_db.channel_member cm2
                 ON ct.code = 'DM'
                 AND cm2.channel_id = c.id
                 AND cm2.user_id <> :userId
                 LEFT JOIN slack_lite_db.app_user u ON u.user_id = cm2.user_id
                   WHERE cm.user_id = :userId
              AND cm.is_active = true
            ORDER BY c.last_message_at DESC
            """,
            nativeQuery = true
    )
    List<Object[]> findMyChats(@Param("userId") Long userId);

    @Query(
            value = """
SELECT channel_id, type, name, joined
FROM (
    -- 1. Existing DMs (already chatted)
    SELECT
        'DM' AS type,
        c.id AS channel_id,
        u.username AS name,
        true AS joined,
        1 AS type_order
    FROM slack_lite_db.channel c
    JOIN slack_lite_db.channel_types ct ON ct.id = c.channel_type_id
    JOIN slack_lite_db.channel_member cm ON cm.channel_id = c.id AND cm.user_id = :loggedInUserId
    JOIN slack_lite_db.channel_member cm2 ON cm2.channel_id = c.id AND cm2.user_id <> :loggedInUserId
    JOIN slack_lite_db.app_user u ON u.user_id = cm2.user_id
    WHERE ct.code = 'DM'
      AND LOWER(u.username) LIKE CONCAT(:keyword, '%')

    UNION ALL

    -- 2. Groups joined
    SELECT
        'GROUP' AS type,
        c.id AS channel_id,
        c.name AS name,
        true AS joined,
        2 AS type_order
    FROM slack_lite_db.channel c
    JOIN slack_lite_db.channel_types ct ON ct.id = c.channel_type_id
    JOIN slack_lite_db.channel_member cm ON cm.channel_id = c.id AND cm.user_id = :loggedInUserId
    WHERE ct.code IN ('PUBLIC', 'PRIVATE')
      AND LOWER(c.name) LIKE CONCAT(:keyword, '%')

    UNION ALL
    
    -- 3. New DMs (not started yet with this user)
                        SELECT
                            'DM' AS type,
                            NULL AS channel_id,
                            u.username AS name,
                            false AS joined,
                            1 AS type_order
                        FROM slack_lite_db.app_user u
                        WHERE u.user_id <> :loggedInUserId
                          AND LOWER(u.username) LIKE CONCAT(:keyword, '%')
                          AND u.user_id NOT IN (
                              SELECT CASE
                                         WHEN cm.user_id = :loggedInUserId THEN cm2.user_id
                                         ELSE cm.user_id
                                     END AS other_user_id
                              FROM slack_lite_db.channel c
                              JOIN slack_lite_db.channel_types ct ON ct.id = c.channel_type_id
                              JOIN slack_lite_db.channel_member cm ON cm.channel_id = c.id
                              JOIN slack_lite_db.channel_member cm2 ON cm2.channel_id = c.id
                              WHERE ct.code = 'DM'
                                AND (cm.user_id = :loggedInUserId OR cm2.user_id = :loggedInUserId)
                                AND cm.user_id <> cm2.user_id
                          )
                    
    UNION ALL

    -- 4. New groups (not joined yet)
    SELECT
        'GROUP' AS type,
        c.id AS channel_id,
        c.name AS name,
        false AS joined,
        2 AS type_order
    FROM slack_lite_db.channel c
    JOIN slack_lite_db.channel_types ct ON ct.id = c.channel_type_id
    LEFT JOIN slack_lite_db.channel_member cm ON cm.channel_id = c.id AND cm.user_id = :loggedInUserId
    WHERE ct.code IN ('PUBLIC','PRIVATE')
      AND LOWER(c.name) LIKE CONCAT(:keyword, '%')
      AND cm.user_id IS NULL
) AS unioned_chats
ORDER BY
    type_order ASC,  -- DM first
    joined DESC,     -- joined first, then not joined
    name ASC         -- alphabetical
""",
            nativeQuery = true
    )
    List<Object[]> searchAllChats(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("keyword") String keyword
    );

}
