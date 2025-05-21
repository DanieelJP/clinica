import React, { useState, useEffect } from 'react';
import {
    Container,
    Typography,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    MenuItem,
    Stack,
    Snackbar,
    Alert,
    Select,
    FormControl,
    InputLabel,
    SelectChangeEvent,
    Chip
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { es } from 'date-fns/locale';
import { format } from 'date-fns';
import { visitaService } from '../services/visitaService';
import { Visita, VisitFormData, EstadoVisita } from '../types/models';
import { useAuth } from '../context/AuthContext';
import EditVisitDialog from '../components/EditVisitDialog';

const Visitas: React.FC = () => {
    const { user } = useAuth();
    const [visits, setVisits] = useState<Visita[]>([]);
    const [openDialog, setOpenDialog] = useState(false);
    const [openEditDialog, setOpenEditDialog] = useState(false);
    const [selectedVisit, setSelectedVisit] = useState<Visita | null>(null);
    const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
    const [formData, setFormData] = useState<VisitFormData>({
        odontologo_id: user?.id || 0,
        paciente_dni: '',
        fechaHora: '',
        motivo: '',
        observaciones: '',
        tratamiento_id: undefined
    });
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    useEffect(() => {
        loadVisits();
    }, []);

    const loadVisits = async () => {
        try {
            if (user?.tipo === 'ODONTOLOGO') {
                const data = await visitaService.getVisitsByDentist(user.id);
                console.log("Visitas recibidas del backend:", data);
                setVisits(data);
            }
        } catch (error) {
            setError('Error al cargar las visitas');
        }
    };

    const handleOpenDialog = () => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setFormData({
            odontologo_id: user?.id || 0,
            paciente_dni: '',
            fechaHora: '',
            motivo: '',
            observaciones: '',
            tratamiento_id: undefined
        });
    };

    const handleEditClick = (visit: Visita) => {
        setSelectedVisit(visit);
        setOpenEditDialog(true);
    };

    const handleEditClose = () => {
        setSelectedVisit(null);
        setOpenEditDialog(false);
    };

    const handleSubmit = async () => {
        try {
            if (!selectedDate) {
                setError('Por favor seleccione una fecha y hora');
                return;
            }

            const formattedFechaHora = format(selectedDate, "yyyy-MM-dd'T'HH:mm:ss");

            const visitData: VisitFormData = {
                ...formData,
                fechaHora: formattedFechaHora
            };

            await visitaService.createVisit(visitData);
            setSuccess('Visita creada exitosamente');
            handleCloseDialog();
            loadVisits();
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Error al crear la visita');
        }
    };

    const handleEditSubmit = async (visitData: VisitFormData & { estado?: EstadoVisita }) => {
        try {
            if (!selectedVisit) return;
            await visitaService.updateVisit(selectedVisit.id, visitData);
            setSuccess('Visita actualizada exitosamente');
            handleEditClose();
            loadVisits();
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Error al actualizar la visita');
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleEstadoChange = async (visit: Visita, e: SelectChangeEvent<EstadoVisita>) => {
        try {
            await visitaService.updateVisitStatus(visit.id, e.target.value as EstadoVisita);
            setSuccess('Estado actualizado exitosamente');
            loadVisits();
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Error al actualizar el estado');
        }
    };

    const getEstadoColor = (estado: EstadoVisita) => {
        switch (estado) {
            case EstadoVisita.PROGRAMADA:
                return 'primary';
            case EstadoVisita.REALIZADA:
                return 'success';
            case EstadoVisita.CANCELADA:
                return 'error';
            case EstadoVisita.NO_ASISTIO:
                return 'warning';
            default:
                return 'default';
        }
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom>
                Gestión de Visitas
            </Typography>

            <Button
                variant="contained"
                color="primary"
                onClick={handleOpenDialog}
                sx={{ mb: 2 }}
            >
                Nueva Visita
            </Button>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Fecha y Hora</TableCell>
                            <TableCell>Paciente</TableCell>
                            <TableCell>Motivo</TableCell>
                            <TableCell>Estado</TableCell>
                            <TableCell>Acciones</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {visits.map((visit) => (
                            <TableRow key={visit.id}>
                                <TableCell>
                                    {new Date(visit.fechaHora).toLocaleString()}
                                </TableCell>
                                <TableCell>
                                    {visit.paciente ? 
                                        `${visit.paciente.nombre} ${visit.paciente.apellidos}` :
                                        visit.paciente_dni
                                    }
                                </TableCell>
                                <TableCell>{visit.motivo}</TableCell>
                                <TableCell>
                                    <Chip
                                        label={visit.estado}
                                        color={getEstadoColor(visit.estado)}
                                        size="small"
                                    />
                                </TableCell>
                                <TableCell>
                                    <Button
                                        variant="outlined"
                                        size="small"
                                        onClick={() => handleEditClick(visit)}
                                        sx={{ mr: 1 }}
                                    >
                                        Editar
                                    </Button>
                                    <FormControl size="small" sx={{ minWidth: 120 }}>
                                        <Select
                                            value={visit.estado}
                                            onChange={(e) => handleEstadoChange(visit, e as SelectChangeEvent<EstadoVisita>)}
                                            size="small"
                                        >
                                            {Object.values(EstadoVisita).map((estado) => (
                                                <MenuItem key={estado} value={estado}>
                                                    {estado}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
                <DialogTitle>Nueva Visita</DialogTitle>
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
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>Cancelar</Button>
                    <Button onClick={handleSubmit} variant="contained" color="primary">
                        Crear Visita
                    </Button>
                </DialogActions>
            </Dialog>

            <EditVisitDialog
                open={openEditDialog}
                onClose={handleEditClose}
                onSave={handleEditSubmit}
                visit={selectedVisit}
            />

            <Snackbar
                open={!!error}
                autoHideDuration={6000}
                onClose={() => setError(null)}
            >
                <Alert severity="error" onClose={() => setError(null)}>
                    {error}
                </Alert>
            </Snackbar>

            <Snackbar
                open={!!success}
                autoHideDuration={6000}
                onClose={() => setSuccess(null)}
            >
                <Alert severity="success" onClose={() => setSuccess(null)}>
                    {success}
                </Alert>
            </Snackbar>
        </Container>
    );
};

export default Visitas; 