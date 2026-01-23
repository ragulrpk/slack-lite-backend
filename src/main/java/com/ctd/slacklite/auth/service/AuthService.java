package com.ctd.slacklite.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ctd.slacklite.auth.dto.LoginRequestDTO;
import com.ctd.slacklite.auth.dto.LoginResponseDTO;
import com.ctd.slacklite.auth.model.AppUser;
import com.ctd.slacklite.auth.repository.AuthRepository;
import com.ctd.slacklite.auth.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {

        AppUser user = authRepository
                .findByUsernameAndIsActiveTrue(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername());

        user.setJwtToken(token);
        user.setLastLoginAt(LocalDateTime.now());
        user.setLoginCount(user.getLoginCount() + 1);

        authRepository.save(user);

        return LoginResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .token(token)
                .build();
    }
}
