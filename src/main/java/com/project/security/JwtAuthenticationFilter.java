package com.project.security;

import com.project.config.KeyGenerator;
import com.project.utils.messages.ExceptionMessages;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            final String login;
            final String token;
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            token = authorizationHeader.substring(7);
            login = jwtService.extractLogin(token, KeyGenerator.getSecretKey());
            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                jwtService.authenticate(login, token, request);
            }
            filterChain.doFilter(request, response);
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(ExceptionMessages.FORBIDDEN);
        }

    }
}
