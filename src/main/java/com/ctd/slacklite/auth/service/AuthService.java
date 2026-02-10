package com.ctd.slacklite.auth.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ctd.slacklite.auth.dto.UserAccessDto;
import com.ctd.slacklite.auth.dto.UserDTO;
import com.ctd.slacklite.auth.mapper.UserMapper;
import com.ctd.slacklite.auth.model.JwtBlacklist;
import com.ctd.slacklite.auth.repository.JwtBlacklistRepository;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctd.slacklite.auth.dto.LoginRequestDTO;
import com.ctd.slacklite.auth.dto.LoginResponseDTO;
import com.ctd.slacklite.auth.model.AppUser;
import com.ctd.slacklite.auth.repository.AuthRepository;
import com.ctd.slacklite.auth.security.JwtUtil;

import com.ctd.slacklite.util.PasswordEncoderUtil;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private JwtBlacklistRepository jwtBlacklistRepository;

    private final AuthRepository authRepository;

    public LoginResponseDTO login(@Valid LoginRequestDTO request) {

        String username = request.getUsername();
        String rawPassword = request.getPassword();

        // 1️⃣ Fetch user
        AppUser user = authRepository
                .findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // 2️⃣ Validate password
        if (!PasswordEncoderUtil.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3️⃣ Fetch roles ONLY after validation
        List<String> permissionList = authRepository.findPermissionBasedOnUserId(user.getUserId());

        // 2. Convert the List to a Stream
        //Stream<String> permissionStream = permissionList.stream();

        // 3. Use the stream (e.g., print elements)
        //System.out.print("Stream elements: ");
        //permissionStream.forEach(System.out::println);

        // 4️⃣ Map to DTO
        UserAccessDto userAccess = new UserAccessDto(
                user.getUserId(),
                user.getUsername(),
                permissionList
        );

        // 5️⃣ Generate JWT
        String token =  JwtUtil.generateToken(
                userAccess.getUserId(),
                userAccess.getUsername(),
                userAccess.getPermissionList()
        );

        return LoginResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .token(token)
                .build();
    }

    /**
     * Blacklist the token for logout
     * @param token JWT string
     */
    public void logout(String token) {
        // Check if token is already blacklisted
        if (jwtBlacklistRepository.existsByToken(token)) {
            return; // already blacklisted
        }

        // Parse token to get expiration
        Claims claims = JwtUtil.validateToken(token);
        LocalDateTime expiry = LocalDateTime.ofInstant(
                claims.getExpiration().toInstant(),
                java.time.ZoneId.systemDefault()
        );

        // Save in blacklist table
        JwtBlacklist blacklisted = new JwtBlacklist();
        blacklisted.setToken(token);
        blacklisted.setExpiryTime(expiry);

        jwtBlacklistRepository.save(blacklisted);
    }

    public UserDTO getAppUserDetails(Long userId) {

        AppUser appUser = authRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        System.out.println("AppUser: "+ appUser);

        return UserMapper.toUserDTO(appUser);
    }
}
