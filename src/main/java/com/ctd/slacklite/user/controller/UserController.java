package com.ctd.slacklite.user.controller;

import com.ctd.slacklite.auth.dto.UserDTO;
import com.ctd.slacklite.user.model.AppUserImage;
import com.ctd.slacklite.auth.security.CustomerUserDetails;
import com.ctd.slacklite.auth.service.AuthService;
import com.ctd.slacklite.user.service.AppUserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final AppUserImageService appUserImageService;

//    To fetch the Profile Details based on the User Id
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getAppUserDetails(Authentication authentication) {

        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(authService.getAppUserDetails(userId));
    }

    @GetMapping("/profile/photo")
    public ResponseEntity<byte[]> getAppUserPhoto(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        AppUserImage image = appUserImageService.getAppUserPhoto(userId);
        if (image == null || image.getImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getContentType())).body(image.getImage());
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> saveUploadImage(@RequestParam("image") MultipartFile image, Authentication authentication) {

        System.out.println("Image: " + image.getOriginalFilename());
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        appUserImageService.saveUploadImage(userDetails.getUserId(), image);
        return ResponseEntity.ok("Profile image uploaded successfully");
    }
}
