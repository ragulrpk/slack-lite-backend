package com.ctd.slacklite.homePage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctd.slacklite.homePage.dto.ChatListDTO;
import com.ctd.slacklite.homePage.model.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    @Query("""
        SELECT DISTINCT new com.ctd.slacklite.homePage.dto.ChatListDTO(
            c.id,
            ct.code,
            CASE
                WHEN ct.code = 'DM' THEN u.username
                ELSE c.name
            END,
            c.lastMessageAt
        )
        FROM Channel c
        JOIN ChannelMember cm 
            ON cm.channelId = c.id
        JOIN ChannelType ct 
            ON ct.id = c.channelTypeId

        LEFT JOIN ChannelMember cm2
            ON ct.code = 'DM'
           AND cm2.channelId = c.id
           AND cm2.userId <> :userId

        LEFT JOIN AppUser u 
            ON u.userId = cm2.userId

        WHERE cm.userId = :userId
          AND cm.isActive = true

        ORDER BY c.lastMessageAt DESC
    """)
    List<ChatListDTO> findMyChats(@Param("userId") Long userId);
}
