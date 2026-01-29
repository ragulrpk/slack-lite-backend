package com.ctd.slacklite.homePage.controller;

import com.ctd.slacklite.auth.security.CustomUserPrincipal;
import com.ctd.slacklite.homePage.dto.ChatListDTO;
import com.ctd.slacklite.homePage.service.HomePageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class HomePageController {

    private final HomePageService homePageService;

    public HomePageController(HomePageService homePageService){

        this.homePageService=homePageService;
    }
    @GetMapping("/getMyChats")
    public ResponseEntity<List<ChatListDTO>> getLoggedInUserChats(
            @AuthenticationPrincipal CustomUserPrincipal user) {

        List<ChatListDTO> chats=homePageService.getMyChats(user.getUserId());
        return ResponseEntity.ok(chats);
    }

}
