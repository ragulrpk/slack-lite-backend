package com.ctd.slacklite.homePage.controller;

import com.ctd.slacklite.auth.security.CustomerUserDetails;
import com.ctd.slacklite.homePage.dto.ChatListDTO;
import com.ctd.slacklite.homePage.dto.SearchResponseDTO;
import com.ctd.slacklite.homePage.service.HomePageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<ChatListDTO>> getMyChats(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        System.out.println("HomePage Controller User Id: " + userDetails.getUserId());
        List<ChatListDTO> chats=homePageService.getMyChats(userDetails.getUserId());
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchResponseDTO>> searchUser(Authentication authentication,
                                                              @RequestParam String name ){

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        List<SearchResponseDTO> names=homePageService.searchName(name,userDetails.getUserId());
        for(SearchResponseDTO searchResponseDTO:names){
            System.out.println("HomePage Controller User Name: " + searchResponseDTO.getUsername());
        }
        return ResponseEntity.ok(names);
    }

}
