import api from './api';
import { Horario } from '../types/models';

export const horarioService = {
    async crearHorario(horario: Horario): Promise<Horario> {
        // Transformar el objeto para que coincida con la estructura de la base de datos
        const horarioData = {
            odontologo_id: horario.odontologo_id,
            dia: horario.dia,
            diaSemana: horario.diaSemana,
            horaInicio: horario.horaInicio,
            horaFin: horario.horaFin,
            disponible: horario.disponible
        };

        console.log('Enviando horario:', horarioData);
        const response = await api.post('/horarios', horarioData);
        return response.data;
    },

    async obtenerHorariosOdontologo(odontologoId: number): Promise<Horario[]> {
        const response = await api.get(`/horarios/odontologo/${odontologoId}`);
        return response.data;
    },

    async verificarDisponibilidad(
        odontologoId: number,
        diaSemana: string,
        hora: string
    ): Promise<boolean> {
        const response = await api.get('/horarios/verificar-disponibilidad', {
            params: { odontologoId, diaSemana, hora }
        });
        return response.data;
    },

    async actualizarDisponibilidad(horarioId: number, disponible: boolean): Promise<void> {
        await api.put(`/horarios/${horarioId}/disponibilidad`, null, {
            params: { disponible }
        });
    },

    async actualizarHorario(id: number, horario: Horario): Promise<Horario> {
        const response = await api.put(`/horarios/${id}`, horario);
        return response.data;
    },

    async eliminarHorario(id: number): Promise<void> {
        await api.delete(`/horarios/${id}`);
    }
}; 