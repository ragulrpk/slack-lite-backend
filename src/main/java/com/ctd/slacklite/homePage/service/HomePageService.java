package com.ctd.slacklite.homePage.service;

import com.ctd.slacklite.homePage.dto.ChatListDTO;
import com.ctd.slacklite.homePage.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomePageService {
    private final ChannelRepository channelRepository;

    public HomePageService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public List<ChatListDTO> getMyChats(Long userId) {
        return channelRepository.findMyChats(userId);
    }

}
