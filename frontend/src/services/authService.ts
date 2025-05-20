import api from './api';
import { AuthResponse, LoginRequest, RegisterRequest, Usuario } from '../types/models';

export const authService = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    console.log('Intentando iniciar sesión con:', credentials.username);
    try {
      // Llamada directa usando fetch para diagnosticar problemas
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify(credentials),
        credentials: 'include'
      });

      console.log('Respuesta del servidor:', response.status);
      
      // Para ver el cuerpo de la respuesta
      const data = await response.json();
      console.log('Datos recibidos:', data);
      
      // Si la respuesta es exitosa
      if (response.ok) {
    // Guardar el token y la información del usuario en localStorage
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(data.usuario));
        return data;
      } else {
        throw new Error(`Error ${response.status}: ${JSON.stringify(data)}`);
      }
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  },

  logout: (): void => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: (): Usuario | null => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      return JSON.parse(userStr);
    }
    return null;
  },

  isAuthenticated: (): boolean => {
    return localStorage.getItem('token') !== null;
  },

  register: async (userData: RegisterRequest): Promise<Usuario> => {
    console.log('Intentando registrar usuario:', userData.username);
    try {
    const response = await api.post<Usuario>('/auth/register', userData);
      console.log('Usuario registrado con éxito:', response.data);
    return response.data;
    } catch (error) {
      console.error('Error en registro:', error);
      throw error;
    }
  }
};

export default authService;
