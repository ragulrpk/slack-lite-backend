package com.ctd.slacklite.homePage.repository;

import com.ctd.slacklite.homePage.dto.ChatListProjection;
import com.ctd.slacklite.homePage.dto.SearchProjection;
import com.ctd.slacklite.homePage.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    // Native query for fetching user's chats
    @Query(value = """
            
                       SELECT DISTINCT
                c.channel_id,
                ct.code AS channelType,
                CASE
                    WHEN ct.code = 'DM' THEN u.username
                    ELSE c.name
                END AS displayName,
                c.last_message_at
            FROM slack_lite_db.channel c
            
            JOIN slack_lite_db.channel_member cm
                ON cm.channel_id = c.channel_id
                AND cm.user_id = :userId
                AND cm.is_active = true
            
            JOIN slack_lite_db.channel_type ct
                ON ct.channel_type_id = c.channel_type_id
            
            -- For DM: get the other user
            LEFT JOIN slack_lite_db.channel_member cm2
                ON ct.code = 'DM'
                AND cm2.channel_id = c.channel_id
                AND cm2.user_id <> :userId
            
            LEFT JOIN slack_lite_db.app_user u
                ON u.user_id = cm2.user_id
            
            ORDER BY c.last_message_at DESC;
            
            """, nativeQuery = true)
    List<ChatListProjection> findMyChats(@Param("userId") Long userId);

    @Query(value = """
                SELECT
                    sub.channel_id       AS channelId,
                    sub.channelType      AS type,
                    sub.displayName      AS name,
                    TRUE                 AS joined
                FROM (
                    SELECT DISTINCT
                        c.channel_id,
                        ct.code AS channelType,
                        CASE
                            WHEN ct.code = 'DM' THEN u.username
                            ELSE c.name
                        END AS displayName,
                        c.last_message_at
                    FROM slack_lite_db.channel c
            
                    JOIN slack_lite_db.channel_member cm
                        ON cm.channel_id = c.channel_id
                        AND cm.user_id = :userId
                        AND cm.is_active = true
            
                    JOIN slack_lite_db.channel_type ct
                        ON ct.channel_type_id = c.channel_type_id
            
                    LEFT JOIN slack_lite_db.channel_member cm2
                        ON ct.code = 'DM'
                        AND cm2.channel_id = c.channel_id
                        AND cm2.user_id <> :userId
            
                    LEFT JOIN slack_lite_db.app_user u
                        ON u.user_id = cm2.user_id
            
                    WHERE
                        (:search IS NULL OR :search = ''
                         OR LOWER(
                              COALESCE(
                                  CASE
                                      WHEN ct.code = 'DM' THEN u.username
                                      ELSE c.name
                                  END, ''
                              )
                         ) LIKE LOWER(CONCAT('%', :search, '%'))
                        )
                ) sub
            
                ORDER BY
                    CASE
                        WHEN LOWER(sub.displayName) LIKE LOWER(CONCAT(:search, '%'))
                        THEN 1
                        ELSE 2
                    END,
                    LOWER(sub.displayName),
                    sub.last_message_at DESC
            """, nativeQuery = true)
    List<SearchProjection> searchAllChats(
            @Param("userId") Long userId,
            @Param("search") String search
    );

}
