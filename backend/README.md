# Sistema de Gestión de Clínica Dental

Este es un sistema de gestión para una clínica dental desarrollado con Spring Boot.

## Requisitos

- Java 17 o superior
- MySQL 8.0
- Maven 3.8.x

## Configuración de la Base de Datos

1. Crear una base de datos MySQL llamada `clinica_odontologica`
2. Modificar las credenciales de acceso en el archivo `src/main/resources/application.properties` si es necesario

## Instalación

1. Clonar el repositorio
2. Ejecutar `mvn clean install`
3. Ejecutar la aplicación con `mvn spring-boot:run`

## Endpoints

### Autenticación
- POST `/api/auth/login` - Iniciar sesión
- POST `/api/auth/register` - Registrarse

### Pacientes
- GET `/api/pacientes` - Obtener todos los pacientes
- GET `/api/pacientes/{id}` - Obtener paciente por ID
- POST `/api/pacientes` - Crear nuevo paciente
- PUT `/api/pacientes/{id}` - Actualizar paciente
- DELETE `/api/pacientes/{id}` - Eliminar paciente
- GET `/api/pacientes/search` - Buscar pacientes por nombre, apellidos, DNI, teléfono o mutua

### Visitas
- GET `/api/visitas` - Obtener todas las visitas
- GET `/api/visitas/{id}` - Obtener visita por ID
- POST `/api/visitas` - Crear nueva visita
- PUT `/api/visitas/{id}` - Actualizar visita
- DELETE `/api/visitas/{id}` - Eliminar visita
- GET `/api/visitas/by-paciente/{pacienteId}` - Obtener visitas por paciente
- GET `/api/visitas/by-odontologo/{odontologoId}` - Obtener visitas por odontólogo
- GET `/api/visitas/by-date` - Obtener visitas por rango de fechas

### Odontólogos
- GET `/api/odontologos` - Obtener todos los odontólogos
- GET `/api/odontologos/{id}` - Obtener odontólogo por ID
- POST `/api/odontologos` - Crear nuevo odontólogo
- PUT `/api/odontologos/{id}` - Actualizar odontólogo
- DELETE `/api/odontologos/{id}` - Eliminar odontólogo
- GET `/api/odontologos/by-matricula` - Obtener odontólogo por matrícula

### Tratamientos
- GET `/api/tratamientos` - Obtener todos los tratamientos
- GET `/api/tratamientos/{id}` - Obtener tratamiento por ID
- POST `/api/tratamientos` - Crear nuevo tratamiento
- PUT `/api/tratamientos/{id}` - Actualizar tratamiento
- DELETE `/api/tratamientos/{id}` - Eliminar tratamiento
- GET `/api/tratamientos/search` - Buscar tratamientos por nombre

## Seguridad

La aplicación utiliza JWT para la autenticación. Todos los endpoints protegidos requieren un token JWT en el header `Authorization` con el formato `Bearer <token>`.

## Tecnologías Utilizadas

- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Lombok
- Maven
