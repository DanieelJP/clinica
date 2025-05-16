package com.clinicadental.controllers;

import com.clinicadental.dto.AuthResponse;
import com.clinicadental.dto.LoginRequest;
import com.clinicadental.models.Usuario;
import com.clinicadental.repositories.UsuarioRepository;
import com.clinicadental.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Recibida solicitud de login para usuario: {}", loginRequest.getUsername());
        
        try {
            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Establecer valores para campos transientes
            usuario.setTipo("ADMINISTRATIVO");
            
            AuthResponse response = new AuthResponse(token, usuario);
            logger.info("Login exitoso para: {}", loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en el login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        logger.info("Recibida solicitud de registro para usuario: {}", usuario.getUsername());
        
        try {
            Usuario registeredUser = authService.register(usuario);
            logger.info("Registro exitoso para: {}", usuario.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            logger.error("Error en el registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
