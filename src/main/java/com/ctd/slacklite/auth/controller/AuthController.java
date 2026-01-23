package com.ctd.slacklite.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ctd.slacklite.auth.dto.LoginRequestDTO;
import com.ctd.slacklite.auth.dto.LoginResponseDTO;
import com.ctd.slacklite.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {
        System.out.println("LoginRequestDTO: " + request.getUsername() +" "+ request.getPassword());
        return ResponseEntity.ok(authService.login(request));
    }
}
