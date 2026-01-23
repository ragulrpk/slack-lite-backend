package com.ctd.slacklite.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        // Example password to encode
        String rawPassword = "123";  // Replace with any password you want

        // Encode the password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Print encoded password
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);

        // Optional: verify password matches
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("Does raw password match encoded? " + matches);
    }
}
