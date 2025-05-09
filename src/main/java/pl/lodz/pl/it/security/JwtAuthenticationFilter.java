package pl.lodz.pl.it.security;

import pl.lodz.pl.it.config.KeyGenerator;
import pl.lodz.pl.it.utils.messages.ExceptionMessages;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String login;
        final String token;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                token = authorizationHeader.substring(7);
                login = JwtService.extractLogin(token, KeyGenerator.getSecretKey());
                if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    jwtService.authenticate(login, token, request);
                }
            } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write(ExceptionMessages.FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
