# Sistema de Gestión de Clínica Dental

Un sistema completo para la gestión de una clínica dental, incluyendo citas, pacientes y visitas.

## Estructura del Proyecto

El proyecto está dividido en dos partes principales:

- `frontend/`: La interfaz de usuario construida con React/TypeScript
- `backend/`: El servidor backend

## Requisitos

- Node.js (versión LTS recomendada)
- npm o yarn

## Instalación

1. Clona el repositorio
2. Instala las dependencias del frontend:
   ```bash
   cd frontend
   npm install
   ```
3. Instala las dependencias del backend:
   ```bash
   cd backend
   npm install
   ```

## Configuración

Crea archivos `.env` en las carpetas `frontend` y `backend` con las variables de entorno necesarias.

## Ejecución

1. Inicia el backend:
   ```bash
   cd backend
   npm run dev
   ```
2. En otra terminal, inicia el frontend:
   ```bash
   cd frontend
   npm start
   ```

## Tecnologías Utilizadas

- Frontend: React, TypeScript, Vite
- Backend: Node.js, Express
- Base de datos: PostgreSQL

## Funcionalidades Principales

- Gestión de pacientes
- Programación de citas
- Registro de visitas
- Historial clínico
- Gestión de usuarios y roles

## Licencia

[MIT License](LICENSE)
