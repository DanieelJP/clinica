import api from './api';
import { Odontologo, Horario } from '../types/models';

export const odontologoService = {
  getAll: async (): Promise<Odontologo[]> => {
    const response = await api.get<Odontologo[]>('/odontologos');
    return response.data;
  },

  getById: async (id: number): Promise<Odontologo> => {
    const response = await api.get<Odontologo>(`/odontologos/${id}`);
    return response.data;
  },

  create: async (odontologo: Odontologo): Promise<Odontologo> => {
    const response = await api.post<Odontologo>('/odontologos', odontologo);
    return response.data;
  },

  update: async (id: number, odontologo: Odontologo): Promise<Odontologo> => {
    const response = await api.put<Odontologo>(`/odontologos/${id}`, odontologo);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/odontologos/${id}`);
  },

  getHorarios: async (id: number): Promise<Horario[]> => {
    const response = await api.get<Horario[]>(`/odontologos/${id}/horarios`);
    return response.data;
  },

  createHorario: async (id: number, horario: Horario): Promise<Horario> => {
    const response = await api.post<Horario>(`/odontologos/${id}/horarios`, horario);
    return response.data;
  },

  updateHorario: async (id: number, horarioId: number, horario: Horario): Promise<Horario> => {
    const response = await api.put<Horario>(`/odontologos/${id}/horarios/${horarioId}`, horario);
    return response.data;
  },

  deleteHorario: async (id: number, horarioId: number): Promise<void> => {
    await api.delete(`/odontologos/${id}/horarios/${horarioId}`);
  }
};

export default odontologoService;
