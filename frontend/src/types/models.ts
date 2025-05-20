// Tipos de usuario
export enum TipoUsuario {
  ADMINISTRATIVO = 'ADMINISTRATIVO',
  ODONTOLOGO = 'ODONTOLOGO'
}

// Tipo para Usuario
export interface Usuario {
  id: number;
  username: string;
  email: string;
  tipo: TipoUsuario;
}

// Tipo para Administrativo
export interface Administrativo extends Usuario {
  departamento: string;
}

// Tipo para Odontólogo
export interface Odontologo extends Usuario {
  matricula: string;
  especialidad: string;
}

// Enumeración para los días de la semana
export enum DiaSemana {
  LUNES = 'LUNES',
  MARTES = 'MARTES',
  MIERCOLES = 'MIERCOLES',
  JUEVES = 'JUEVES',
  VIERNES = 'VIERNES'
}

// Tipo para Horario
export interface Horario {
  id: number;
  odontologo: Odontologo;
  dia: DiaSemana;
  horaInicio: string; // Formato HH:MM
  horaFin: string;    // Formato HH:MM
}

// Enumeración para el tipo de pago
export enum TipoPago {
  PARTICULAR = 'PARTICULAR',
  MUTUA = 'MUTUA'
}

// Tipo para Paciente
export interface Paciente {
  dni: string;
  nombre: string;
  apellidos: string;
  fechaNacimiento: string; // Formato YYYY-MM-DD
  telefono: string;
  obraSocial?: string;
  mutua?: string;
  tipoPago: TipoPago;
}

// Enumeración para el parentesco
export enum Parentesco {
  PADRE = 'PADRE',
  MADRE = 'MADRE',
  TUTOR = 'TUTOR',
  HERMANO = 'HERMANO',
  OTRO = 'OTRO'
}

// Tipo para Responsable
export interface Responsable {
  id: number;
  nombre: string;
  apellidos: string;
  dni: string;
  telefono: string;
  parentesco: Parentesco;
  paciente?: Paciente;
}

// Tipo para Tratamiento
export interface Tratamiento {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
}

// Enumeración para el estado de la visita
export enum EstadoVisita {
  PROGRAMADA = 'PROGRAMADA',
  COMPLETADA = 'COMPLETADA',
  CANCELADA = 'CANCELADA'
}

// Tipo para Visita
export interface Visita {
  id: number;
  paciente: Paciente;
  odontologo: Odontologo;
  tratamiento?: Tratamiento;
  fechaHora: string; // Formato ISO
  motivo: string;
  observaciones?: string;
  estado: EstadoVisita;
}

// Tipos para la autenticación
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  tipo: 'Administrativo' | 'Odontologo';
  matricula?: string;
  especialidad?: string;
  departamento?: string;
}

export interface AuthResponse {
  token: string;
  usuario: Usuario;
}
