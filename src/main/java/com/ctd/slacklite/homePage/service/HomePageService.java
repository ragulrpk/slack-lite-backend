package com.ctd.slacklite.homePage.service;

import com.ctd.slacklite.auth.security.CustomUserPrincipal;
import com.ctd.slacklite.homePage.dto.ChatListDTO;
import com.ctd.slacklite.homePage.dto.SearchResponseDTO;
import com.ctd.slacklite.homePage.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomePageService {
    private final ChannelRepository channelRepository;

    public HomePageService(ChannelRepository channelRepository) {

        this.channelRepository = channelRepository;
    }

    public List<ChatListDTO> getMyChats(Long userId) {

        List<Object[]> results=channelRepository.findMyChats(userId);

        List<ChatListDTO> dto=new ArrayList<>();

        for(Object[] row : results){

            Long channelId = ((Number) row[0]).longValue();
            String channelType = (String) row[1];
            String displayName = (String) row[2];

            LocalDateTime lastMessageAt = null;
            if (row[3] != null) {
                if (row[3] instanceof java.sql.Timestamp ts) {
                    lastMessageAt = ts.toLocalDateTime();
                } else if (row[3] instanceof java.time.LocalDateTime ldt) {
                    lastMessageAt = ldt;
                }
            }

            dto.add(new ChatListDTO(channelId, channelType, displayName, lastMessageAt));
        }

        return dto;
    }

    public List<SearchResponseDTO> searchName(String name , Long loggedInUserId){

        if(name==null || name.trim().isEmpty()){
            throw new RuntimeException("Enter a name to search");
        }
        name=name.trim()
                .toLowerCase()
                .replace(" ","");

        List<Object[]> results=channelRepository.searchAllChats(loggedInUserId,name);

        List<SearchResponseDTO> dto=new ArrayList<>();

        for(Object[] row: results){
            Long channelId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String type = (String) row[1];
            String displayName = (String) row[2];
            Boolean joined = (Boolean) row[3];

            dto.add(new SearchResponseDTO(type, channelId, displayName, joined));
        }

        return dto;
    }

}
