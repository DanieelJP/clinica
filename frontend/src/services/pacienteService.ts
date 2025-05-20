import api from './api';
import { Paciente } from '../types/models';

export const pacienteService = {
  getAll: async (): Promise<Paciente[]> => {
    const response = await api.get<Paciente[]>('/pacientes');
    return response.data;
  },

  getById: async (dni: string): Promise<Paciente> => {
    const response = await api.get<Paciente>(`/pacientes/${dni}`);
    return response.data;
  },

  create: async (paciente: Paciente): Promise<Paciente> => {
    const response = await api.post<Paciente>('/pacientes', paciente);
    return response.data;
  },

  update: async (dni: string, paciente: Paciente): Promise<Paciente> => {
    const response = await api.put<Paciente>(`/pacientes/${dni}`, paciente);
    return response.data;
  },

  delete: async (dni: string): Promise<void> => {
    await api.delete(`/pacientes/${dni}`);
  },

  search: async (query: string): Promise<Paciente[]> => {
    const response = await api.get<Paciente[]>(`/pacientes/search?query=${query}`);
    return response.data;
  }
};

export default pacienteService;
