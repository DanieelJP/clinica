import api from './api';
import { Tratamiento } from '../types/models';

export const tratamientoService = {
  getAll: async (): Promise<Tratamiento[]> => {
    const response = await api.get<Tratamiento[]>('/tratamientos');
    return response.data;
  },

  getById: async (id: number): Promise<Tratamiento> => {
    const response = await api.get<Tratamiento>(`/tratamientos/${id}`);
    return response.data;
  },

  create: async (tratamiento: Tratamiento): Promise<Tratamiento> => {
    const response = await api.post<Tratamiento>('/tratamientos', tratamiento);
    return response.data;
  },

  update: async (id: number, tratamiento: Tratamiento): Promise<Tratamiento> => {
    const response = await api.put<Tratamiento>(`/tratamientos/${id}`, tratamiento);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/tratamientos/${id}`);
  },

  search: async (nombre: string): Promise<Tratamiento[]> => {
    const response = await api.get<Tratamiento[]>(`/tratamientos/search?nombre=${nombre}`);
    return response.data;
  }
};

export default tratamientoService;
