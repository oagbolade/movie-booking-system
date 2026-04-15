package com.platform.auth.security;

import com.platform.auth.model.User;
import com.platform.auth.repository.UserRepository;
import com.platform.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);
            
            try {
                String username = jwtService.extractUsername(token);

                User user = userRepository.findByEmail(username).orElse(null);

                if (user != null && jwtService.isValid(token, user)) {

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    user.getRoles().stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .toList()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                log.error("Error processing JWT token: {}", e.getMessage());
                // Token is invalid, continue without authentication
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error in JwtAuthenticationFilter: {}", e.getMessage(), e);
            filterChain.doFilter(request, response);
        }
    }
}