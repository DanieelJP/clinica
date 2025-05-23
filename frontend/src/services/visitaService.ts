import axios from 'axios';
import { Visita, VisitFormData, EstadoVisita } from '../types/models';

const API_URL = 'http://localhost:8080/api';

export const visitaService = {
    async createVisit(visitData: VisitFormData): Promise<Visita> {
        try {
            const response = await axios.post(`${API_URL}/visitas`, visitData);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al crear la visita');
        }
    },

    async updateVisit(id: number, visitData: Partial<VisitFormData & { estado?: EstadoVisita }>): Promise<Visita> {
        try {
            const response = await axios.put(`${API_URL}/visitas/${id}`, visitData);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al actualizar la visita');
        }
    },

    async updateVisitStatus(id: number, estado: EstadoVisita): Promise<Visita> {
        try {
            const response = await axios.put(`${API_URL}/visitas/${id}/estado?estado=${estado}`);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al actualizar el estado de la visita');
        }
    },

    async getVisitsByDentist(dentistId: number): Promise<Visita[]> {
        try {
            const response = await axios.get(`${API_URL}/visitas/odontologo/${dentistId}`);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al obtener las visitas del odontólogo');
        }
    },

    async getVisitsByPatient(dni: string): Promise<Visita[]> {
        try {
            const response = await axios.get(`${API_URL}/visitas/paciente/${dni}`);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al obtener las visitas del paciente');
        }
    },

    async getVisitsByDate(date: string): Promise<Visita[]> {
        try {
            const response = await axios.get(`${API_URL}/visitas/fecha/${date}`);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al obtener las visitas por fecha');
        }
    },

    async getVisitById(id: number): Promise<Visita> {
        try {
            const response = await axios.get(`${API_URL}/visitas/${id}`);
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al obtener la visita');
        }
    },

    async deleteVisit(id: number): Promise<void> {
        try {
            await axios.delete(`${API_URL}/visitas/${id}`);
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data);
            }
            throw new Error('Error al eliminar la visita');
        }
    }
};
