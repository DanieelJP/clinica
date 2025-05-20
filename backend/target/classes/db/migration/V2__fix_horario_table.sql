-- Eliminar columnas duplicadas
ALTER TABLE Horario DROP COLUMN dia;
ALTER TABLE Horario DROP COLUMN hora_inicio;
ALTER TABLE Horario DROP COLUMN hora_fin;

-- Renombrar columnas para seguir el estándar
ALTER TABLE Horario CHANGE horaInicio hora_inicio TIME(6) NOT NULL;
ALTER TABLE Horario CHANGE horaFin hora_fin TIME(6) NOT NULL; 