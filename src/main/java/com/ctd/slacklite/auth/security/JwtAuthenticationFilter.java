package com.ctd.slacklite.auth.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ctd.slacklite.auth.repository.JwtBlacklistRepository;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtBlacklistRepository jwtBlacklistRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                System.out.println("---- JwtAuthenticationFilter ----");
                System.out.println("URI    : " + request.getRequestURI());
                System.out.println("METHOD : " + request.getMethod());
                System.out.println("AUTH   : " + request.getHeader("Authorization"));


                // 1️⃣ Check blacklist
                if (jwtBlacklistRepository.existsByToken(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // 2️⃣ Validate JWT
                Claims claims = JwtUtil.validateToken(token);

                Long userId = claims.get("userId", Long.class);
                String username = claims.getSubject();

                // 3️⃣ Extract permissions from JWT
                List<String> permissionList =
                        claims.get("permissionList", List.class);

                // 4️⃣ Convert permissions → authorities (NO ROLE PREFIX)
                List<SimpleGrantedAuthority> authorities =
                        permissionList.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                System.out.println("userId: " + userId);
                System.out.println("username: " + username);
                System.out.println("authorities: ");
                for(int i = 0 ; i<authorities.size() ; i++){
                    System.out.println(authorities.get(i));
                }

                // 5️⃣ Create principal
                CustomerUserDetails customerUserDetails = new CustomerUserDetails(userId, username, permissionList);

                // 6️⃣ Set authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                customerUserDetails,
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (Exception e) {
                System.out.println("JWT ERROR: " + e.getMessage());
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        System.out.println("Passing to next filter");
        filterChain.doFilter(request, response);
    }
}
