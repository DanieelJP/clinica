-- Datos de prueba para la base de datos

-- Insertar un usuario admin si no existe
INSERT IGNORE INTO Usuario (username, password, email, tipo, nombre, apellidos) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@clinica.com', 'ADMINISTRATIVO', 'Admin', 'System');

-- Insertar odontólogos si no existen
INSERT IGNORE INTO Odontologo (matricula, especialidad, nombre, apellidos) VALUES
('MAT-001', 'Ortodoncia', 'Juan', 'Dentista'),
('MAT-002', 'Endodoncia', 'Maria', 'Especialista');

-- Insertar tratamientos si no existen
INSERT IGNORE INTO Tratamiento (nombre, precio, descripcion, duracion_minutos) VALUES
('Limpieza Dental', 50.00, 'Limpieza profesional de los dientes', 30),
('Sellado de Fisuras', 80.00, 'Aplicación de sellador en las muelas', 20),
('Blanqueamiento Dental', 150.00, 'Tratamiento de blanqueamiento profesional', 45),
('Empaste', 70.00, 'Empaste dental para caries', 45),
('Extracción', 90.00, 'Extracción dental', 60);

-- Insertar pacientes si no existen
INSERT IGNORE INTO Paciente (nombre, apellidos, fecha_nacimiento, telefono, cp, dni, mutua, tipo_pago) VALUES
('Juan', 'Pérez', '1985-05-15', '654321098', '28001', '12345678Z', 'ASISA', 'TARJETA'),
('María', 'García', '1990-03-20', '678901234', '28002', '87654321X', 'SANITAS', 'EFECTIVO');

-- Insertar visitas futuras para pruebas
INSERT INTO Visita (paciente_id, odontologo_id, tratamiento_id, fecha_hora, motivo, observaciones, estado) VALUES
(1, 1, 1, '2025-05-15 10:00:00', 'Revisión anual', 'Primera visita', 'PROGRAMADA'),
(1, 1, 2, '2025-05-16 11:30:00', 'Limpieza', 'Seguimiento', 'PROGRAMADA'),
(2, 2, 3, '2025-05-15 16:00:00', 'Dolor de muelas', 'Urgencia', 'PROGRAMADA');
