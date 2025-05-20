package com.clinicadental.services;

import com.clinicadental.config.JwtConfig;
import com.clinicadental.models.Usuario;
import com.clinicadental.models.Odontologo;
import com.clinicadental.models.Administrativo;
import com.clinicadental.repositories.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       JwtConfig jwtConfig) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    public String login(String username, String password) {
        logger.info("Intentando iniciar sesión con username: {}", username);
        
        try {
            // Comprobar que el repositorio esté correctamente configurado
            logger.info("Buscando usuario en la base de datos: {}", username);
            boolean userExists = usuarioRepository.existsByUsername(username);
            logger.info("Usuario existe en base de datos? {}", userExists);
            
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
            
            logger.info("Usuario encontrado: {}, id: {}", usuario.getUsername(), usuario.getId());
            logger.info("Contraseña almacenada: {}", usuario.getPassword());
            
            // Para pruebas, aceptamos cualquier contraseña para el usuario admin
            if ("admin".equals(username)) {
                logger.info("Inicio de sesión exitoso para admin");
                return jwtConfig.generateToken(usuario);
            }
        
            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                logger.error("Contraseña inválida para el usuario: {}", username);
                throw new BadCredentialsException("Credenciales inválidas");
            }
        
            logger.info("Login exitoso para el usuario: {}", username);
            return jwtConfig.generateToken(usuario);
        } catch (Exception e) {
            logger.error("Error durante el login: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Usuario register(Usuario usuario) {
        logger.info("Intentando registrar nuevo usuario: {}", usuario.getUsername());
        
        try {
            if (usuarioRepository.existsByUsername(usuario.getUsername())) {
                logger.error("El nombre de usuario ya existe: {}", usuario.getUsername());
                throw new RuntimeException("El nombre de usuario ya existe");
            }
            
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                logger.error("El email ya está registrado: {}", usuario.getEmail());
                throw new RuntimeException("El email ya está registrado");
            }
            
            // Determinar el tipo de usuario y crear la instancia correspondiente
            Usuario nuevoUsuario;
            String tipo = usuario.getTipo();
            
            if ("Odontologo".equalsIgnoreCase(tipo)) {
                if (usuario.getMatricula() == null || usuario.getMatricula().trim().isEmpty() ||
                    usuario.getEspecialidad() == null || usuario.getEspecialidad().trim().isEmpty()) {
                    throw new RuntimeException("Datos de odontólogo incompletos");
                }
                Odontologo odontologo = new Odontologo();
                odontologo.setUsername(usuario.getUsername());
                odontologo.setPassword(passwordEncoder.encode(usuario.getPassword()));
                odontologo.setEmail(usuario.getEmail());
                odontologo.setTipo("Odontologo");
                odontologo.setMatricula(usuario.getMatricula());
                odontologo.setEspecialidad(usuario.getEspecialidad());
                nuevoUsuario = odontologo;
                logger.info("Creando nuevo odontólogo con matrícula: {} y especialidad: {}", 
                    usuario.getMatricula(), usuario.getEspecialidad());
            } else if ("Administrativo".equalsIgnoreCase(tipo)) {
                Administrativo administrativo = new Administrativo();
                administrativo.setUsername(usuario.getUsername());
                administrativo.setPassword(passwordEncoder.encode(usuario.getPassword()));
                administrativo.setEmail(usuario.getEmail());
                administrativo.setTipo("Administrativo");
                administrativo.setDepartamento("Administración");
                nuevoUsuario = administrativo;
            } else {
                throw new RuntimeException("Tipo de usuario no válido. Debe ser 'Odontologo' o 'Administrativo'");
            }
            
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            logger.info("Usuario registrado exitosamente: {} como {}", usuario.getUsername(), tipo);
            return usuarioGuardado;
        } catch (Exception e) {
            logger.error("Error durante el registro: {}", e.getMessage(), e);
            throw e;
        }
    }
}
