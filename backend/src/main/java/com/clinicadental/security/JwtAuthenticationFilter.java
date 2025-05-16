package com.clinicadental.security;

import com.clinicadental.config.JwtConfig;
import com.clinicadental.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtConfig jwtConfig;
    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, AuthenticationService authenticationService) {
        this.jwtConfig = jwtConfig;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        final String requestURI = request.getRequestURI();
        logger.info("Processing request: {} {}", request.getMethod(), requestURI);
        
        try {
            final String authorizationHeader = request.getHeader("Authorization");
    
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.info("No Authorization header or invalid format for: {}", requestURI);
                chain.doFilter(request, response);
                return;
            }
    
            final String jwt = authorizationHeader.substring(7);
            logger.info("JWT token found for request: {}", requestURI);
            
            final String username = jwtConfig.extractUsername(jwt);
    
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.info("Username extracted from token: {}", username);
                try {
                    UserDetails userDetails = authenticationService.loadUserByUsername(username);
    
                    if (jwtConfig.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("Successfully authenticated user: {}", username);
                    } else {
                        logger.warn("Token validation failed for user: {}", username);
                    }
                } catch (UsernameNotFoundException e) {
                    logger.error("User not found: {}", username);
                }
            } else if (username == null) {
                logger.warn("Could not extract username from token");
            }
        } catch (Exception e) {
            logger.error("Error processing JWT token: {}", e.getMessage());
        }
        
        chain.doFilter(request, response);
    }
}
