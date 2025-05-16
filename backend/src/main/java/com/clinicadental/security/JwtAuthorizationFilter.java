package com.clinicadental.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    
    private static final String[] PUBLIC_PATHS = {
        "/api/auth/register",
        "/api/auth/login"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        String path = request.getRequestURI();
        logger.debug("Filtrando solicitud para: {}", path);
        
        // Si es una ruta pública, permitir el acceso sin comprobar tokens
        boolean isPublicPath = false;
        for (String publicPath : PUBLIC_PATHS) {
            if (path.contains(publicPath)) {
                isPublicPath = true;
                break;
            }
        }
        
        if (isPublicPath) {
            logger.debug("Ruta pública detectada: {}", path);
            chain.doFilter(request, response);
            return;
        }
        
        // Para otras rutas, continuar con la cadena de filtros
        logger.debug("Procesando solicitud protegida: {}", path);
        chain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String publicPath : PUBLIC_PATHS) {
            if (path.contains(publicPath)) {
                return true;
            }
        }
        return false;
    }
}
