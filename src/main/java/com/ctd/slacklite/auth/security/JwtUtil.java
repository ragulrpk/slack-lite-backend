package com.ctd.slacklite.auth.security;

import java.security.Key;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET =
            "slack_lite_secret_key_123456789012345678901234567890";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateToken(Long userId, String username, List<String> permissionList) {

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("permissionList", permissionList)   // 👈 ADD ROLES HERE
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Claims validateToken(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token is expired", e);

        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("JWT token is unsupported", e);

        } catch (MalformedJwtException e) {
            throw new RuntimeException("JWT token is malformed", e);

        } catch (SignatureException e) {
            throw new RuntimeException("JWT signature validation failed", e);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT token is invalid", e);
        }
    }

}
