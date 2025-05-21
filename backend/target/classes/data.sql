-- Eliminar la restricción de clave foránea de la tabla Visita
ALTER TABLE Visita DROP FOREIGN KEY Visita_ibfk_4;

-- Modificar la columna estado para que sea un enum
ALTER TABLE Visita MODIFY COLUMN estado ENUM('PROGRAMADA', 'CONFIRMADA', 'REALIZADA', 'CANCELADA', 'NO_ASISTIO') NOT NULL DEFAULT 'PROGRAMADA'; 