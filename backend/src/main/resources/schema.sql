-- Eliminar columnas duplicadas
ALTER TABLE Paciente DROP COLUMN fechaNacimiento;
ALTER TABLE Paciente DROP COLUMN obraSocial;
ALTER TABLE Paciente DROP COLUMN tipoPago;

-- Asegurar que las columnas restantes tengan las restricciones correctas
ALTER TABLE Paciente MODIFY fecha_nacimiento DATE NOT NULL;
ALTER TABLE Paciente MODIFY obra_social VARCHAR(50);
ALTER TABLE Paciente MODIFY tipo_pago VARCHAR(20) NOT NULL; 