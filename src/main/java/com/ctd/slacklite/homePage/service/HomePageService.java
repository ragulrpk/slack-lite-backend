package com.ctd.slacklite.homePage.service;

import com.ctd.slacklite.homePage.dto.ChatListDTO;
import com.ctd.slacklite.homePage.dto.ChatListProjection;
import com.ctd.slacklite.homePage.dto.SearchProjection;
import com.ctd.slacklite.homePage.dto.SearchResponseDTO;
import com.ctd.slacklite.homePage.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomePageService {
    private final ChannelRepository channelRepository;

    public HomePageService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public List<ChatListDTO> getMyChats(Long userId) {

        List<ChatListProjection> results = channelRepository.findMyChats(userId);

        return results.stream()
                .map(p -> new ChatListDTO(
                        p.getChannelId(),
                        p.getChannelType(),
                        p.getDisplayName(),
                        p.getLastMessageAt()
                ))
                .toList();
    }

    public List<SearchResponseDTO> searchName(String name, Long loggedInUserId) {

        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }

        String keyword = name.trim().toLowerCase();

        List<SearchProjection> results =
                channelRepository.searchAllChats(loggedInUserId, keyword);

        return results.stream()
                .map(p -> new SearchResponseDTO(
                        p.getType(),
                        p.getChannelId(),
                        p.getName(),
                        p.getJoined()
                ))
                .toList();
    }

}