package com.clinicadental.controllers;

import com.clinicadental.dto.AuthResponse;
import com.clinicadental.dto.LoginRequest;
import com.clinicadental.models.Odontologo;
import com.clinicadental.models.Administrativo;
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
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowCredentials = "true")
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
        try {
            logger.info("Intentando iniciar sesión con: {}", loginRequest.getUsername());
            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            String tipoUsuario = usuario.getClass().getSimpleName();
            logger.info("Tipo de usuario detectado: {}", tipoUsuario);

            if (usuario instanceof Odontologo odontologo) {
                usuario.setTipo("ODONTOLOGO");
                usuario.setMatricula(odontologo.getMatricula());
                usuario.setEspecialidad(odontologo.getEspecialidad());
                // Limpiar las colecciones para evitar bucles de serialización
                odontologo.setHorarios(null);
                odontologo.setVisitas(null);
                logger.info("Usuario es odontólogo con matrícula: {} y especialidad: {}", 
                    odontologo.getMatricula(), odontologo.getEspecialidad());
            } else if (usuario instanceof Administrativo) {
                usuario.setTipo("ADMINISTRATIVO");
                logger.info("Usuario es administrativo");
            } else {
                logger.error("Tipo de usuario desconocido: {}", tipoUsuario);
                throw new RuntimeException("Tipo de usuario desconocido");
            }

            AuthResponse response = new AuthResponse(token, usuario);
            logger.info("Login exitoso para: {} como {}", loginRequest.getUsername(), usuario.getTipo());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en el login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        logger.info("Recibida solicitud de registro para usuario: {} de tipo: {}", 
            usuario.getUsername(), usuario.getTipo());
        logger.info("Datos recibidos - Matrícula: {}, Especialidad: {}", 
            usuario.getMatricula(), usuario.getEspecialidad());
        
        try {
            // Validar campos específicos según el tipo de usuario
            if ("Odontologo".equalsIgnoreCase(usuario.getTipo())) {
                if (usuario.getMatricula() == null || usuario.getMatricula().trim().isEmpty()) {
                    logger.error("Matrícula no proporcionada para odontólogo");
                    return ResponseEntity.badRequest().body("La matrícula es obligatoria para odontólogos");
                }
                if (usuario.getEspecialidad() == null || usuario.getEspecialidad().trim().isEmpty()) {
                    logger.error("Especialidad no proporcionada para odontólogo");
                    return ResponseEntity.badRequest().body("La especialidad es obligatoria para odontólogos");
                }
            }
            
            Usuario registeredUser = authService.register(usuario);
            logger.info("Registro exitoso para: {} como {}", usuario.getUsername(), usuario.getTipo());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            logger.error("Error en el registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
