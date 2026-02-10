package com.ctd.slacklite.user.service;

import com.ctd.slacklite.user.model.AppUserImage;
import com.ctd.slacklite.user.repository.AppUserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AppUserImageService {

    private final AppUserImageRepository imageRepository;;

    public void saveUploadImage(Long userId, MultipartFile file) {
        try {
            System.out.println("Saving image to DB");
            AppUserImage image = new AppUserImage();
            image.setUserId(userId);
            image.setImage(file.getBytes());
            image.setContentType(file.getContentType());

            imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image");
        }
    }

    public AppUserImage getAppUserPhoto(Long userId) {
        return imageRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Profile image not found"));
    }

}

