import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
    Stack,
    MenuItem,
    FormControl,
    InputLabel,
    Select,
    SelectChangeEvent
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { es } from 'date-fns/locale';
import { Visita, VisitFormData, EstadoVisita } from '../types/models';

interface EditVisitDialogProps {
    open: boolean;
    onClose: () => void;
    onSave: (visitData: VisitFormData & { estado?: EstadoVisita }) => Promise<void>;
    visit: Visita | null;
}

const EditVisitDialog: React.FC<EditVisitDialogProps> = ({
    open,
    onClose,
    onSave,
    visit
}) => {
    const [formData, setFormData] = useState<VisitFormData & { estado?: EstadoVisita }>({
        odontologo_id: 0,
        paciente_dni: '',
        fechaHora: '',
        motivo: '',
        observaciones: '',
        tratamiento_id: undefined,
        estado: EstadoVisita.PROGRAMADA
    });
    const [selectedDate, setSelectedDate] = useState<Date | null>(null);

    useEffect(() => {
        if (visit) {
            setFormData({
                odontologo_id: visit.odontologo_id,
                paciente_dni: visit.paciente_dni,
                fechaHora: visit.fechaHora,
                motivo: visit.motivo,
                observaciones: visit.observaciones || '',
                tratamiento_id: visit.tratamiento_id,
                estado: visit.estado
            });
            setSelectedDate(new Date(visit.fechaHora));
        }
    }, [visit]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleEstadoChange = (e: SelectChangeEvent<EstadoVisita>) => {
        setFormData(prev => ({
            ...prev,
            estado: e.target.value as EstadoVisita
        }));
    };

    const handleSubmit = async () => {
        if (!selectedDate) return;

        const visitData: VisitFormData & { estado?: EstadoVisita } = {
            ...formData,
            fechaHora: selectedDate.toISOString()
        };

        await onSave(visitData);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
            <DialogTitle>Editar Visita</DialogTitle>
            <DialogContent>
                <Stack spacing={2} sx={{ mt: 1 }}>
                    <TextField
                        fullWidth
                        label="DNI del Paciente"
                        name="paciente_dni"
                        value={formData.paciente_dni}
                        onChange={handleInputChange}
                        required
                    />
                    <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={es}>
                        <DateTimePicker
                            label="Fecha y Hora"
                            value={selectedDate}
                            onChange={(newValue) => setSelectedDate(newValue)}
                            sx={{ width: '100%' }}
                        />
                    </LocalizationProvider>
                    <TextField
                        fullWidth
                        label="Motivo"
                        name="motivo"
                        value={formData.motivo}
                        onChange={handleInputChange}
                        required
                        multiline
                        rows={2}
                    />
                    <TextField
                        fullWidth
                        label="Observaciones"
                        name="observaciones"
                        value={formData.observaciones}
                        onChange={handleInputChange}
                        multiline
                        rows={3}
                    />
                    <FormControl fullWidth>
                        <InputLabel>Estado</InputLabel>
                        <Select
                            value={formData.estado || EstadoVisita.PROGRAMADA}
                            onChange={handleEstadoChange}
                            label="Estado"
                        >
                            {Object.values(EstadoVisita).map((estado) => (
                                <MenuItem key={estado} value={estado}>
                                    {estado}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Stack>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancelar</Button>
                <Button onClick={handleSubmit} variant="contained" color="primary">
                    Guardar Cambios
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default EditVisitDialog; 