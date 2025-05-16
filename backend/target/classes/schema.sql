-- Crear base de datos
CREATE DATABASE IF NOT EXISTS clinica_odontologica CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clinica_odontologica;

-- Tabla Usuario
CREATE TABLE Usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    tipo VARCHAR(20) NOT NULL,
    roles JSON
);

-- Tabla Odontologo
CREATE TABLE Odontologo (
    usuario_id INT PRIMARY KEY,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    especialidad VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Tabla Paciente
CREATE TABLE Paciente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    cp VARCHAR(10),
    dni VARCHAR(20) NOT NULL UNIQUE,
    mutua VARCHAR(50),
    tipo_pago VARCHAR(50) NOT NULL
);

-- Tabla Tratamiento
CREATE TABLE Tratamiento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    descripcion TEXT NOT NULL,
    duracion_minutos INT NOT NULL
);

-- Tabla Visita
CREATE TABLE Visita (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    odontologo_id INT NOT NULL,
    tratamiento_id INT,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    observaciones TEXT,
    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id),
    FOREIGN KEY (odontologo_id) REFERENCES Usuario(id),
    FOREIGN KEY (tratamiento_id) REFERENCES Tratamiento(id)
);

-- Tabla Responsable
CREATE TABLE Responsable (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    relacion VARCHAR(50) NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id)
);
