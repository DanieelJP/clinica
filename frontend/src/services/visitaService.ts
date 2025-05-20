import api from './api';
import { Visita, EstadoVisita } from '../types/models';

export const visitaService = {
  getAll: async (): Promise<Visita[]> => {
    const response = await api.get<Visita[]>('/visitas');
    return response.data;
  },

  getById: async (id: number): Promise<Visita> => {
    const response = await api.get<Visita>(`/visitas/${id}`);
    return response.data;
  },

  create: async (visita: Visita): Promise<Visita> => {
    const response = await api.post<Visita>('/visitas', visita);
    return response.data;
  },

  update: async (id: number, visita: Visita): Promise<Visita> => {
    const response = await api.put<Visita>(`/visitas/${id}`, visita);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/visitas/${id}`);
  },

  cambiarEstado: async (id: number, estado: EstadoVisita): Promise<Visita> => {
    const response = await api.patch<Visita>(`/visitas/${id}/estado`, { estado });
    return response.data;
  },

  getByPaciente: async (dniPaciente: string): Promise<Visita[]> => {
    const response = await api.get<Visita[]>(`/visitas/paciente/${dniPaciente}`);
    return response.data;
  },

  getByOdontologo: async (idOdontologo: number): Promise<Visita[]> => {
    const response = await api.get<Visita[]>(`/visitas/odontologo/${idOdontologo}`);
    return response.data;
  },

  getByFecha: async (fecha: string): Promise<Visita[]> => {
    console.log(`Solicitando visitas para la fecha: ${fecha}`);
    try {
      // Verificar que el token exista antes de hacer la solicitud
      const token = localStorage.getItem('token');
      if (!token) {
        console.warn('No se encontró token JWT para la solicitud');
      } else {
        console.log('Token encontrado:', token.substring(0, 20) + '...');
      }
      
    const response = await api.get<Visita[]>(`/visitas/fecha/${fecha}`);
      console.log('Respuesta de visitas por fecha:', response.data);
    return response.data;
    } catch (error) {
      console.error('Error al obtener visitas por fecha:', error);
      throw error;
    }
  }
};

export default visitaService;
