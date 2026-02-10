package com.ctd.slacklite.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        System.out.println("LoginRequestDTO: " + request.getUsername() + " " + request.getPassword());
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('LOGOUT')")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {

        System.out.println("LogoutRequest: ");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
        String token = authHeader.substring(7); // remove "Bearer "
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }

}
