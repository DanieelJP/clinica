import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Box,
  Grid,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Switch,
  CircularProgress,
  Alert,
  Snackbar
} from '@mui/material';
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';
import { horarioService } from '../services/horarioService';
import { DiaSemana, TipoUsuario, Horario } from '../types/models';

interface FormValues {
  id?: number;
  diaSemana: DiaSemana;
  horaInicio: string;
  horaFin: string;
  disponible: boolean;
}

const Horarios: React.FC = () => {
  const { user } = useAuth();
  const [horarios, setHorarios] = useState<Horario[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [currentHorario, setCurrentHorario] = useState<FormValues>({
    diaSemana: DiaSemana.LUNES,
    horaInicio: '09:00',
    horaFin: '10:00',
    disponible: true
  });
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({
    open: false,
    message: '',
    severity: 'success'
  });

  useEffect(() => {
    if (user?.tipo === TipoUsuario.ODONTOLOGO) {
      loadHorarios();
    }
  }, [user]);

  const loadHorarios = async () => {
    try {
      setLoading(true);
      const data = await horarioService.obtenerHorariosOdontologo(user?.id || 0);
      setHorarios(data);
      setError(null);
    } catch (err) {
      setError('Error al cargar los horarios');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (horario?: Horario) => {
    if (horario) {
      setCurrentHorario({
        id: horario.id,
        diaSemana: horario.diaSemana,
        horaInicio: horario.horaInicio,
        horaFin: horario.horaFin,
        disponible: horario.disponible
      });
    } else {
      setCurrentHorario({
        diaSemana: DiaSemana.LUNES,
        horaInicio: '09:00',
        horaFin: '10:00',
        disponible: true
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const horarioData: Horario = {
        odontologo: {
          id: user?.id || 0
        },
        dia: currentHorario.diaSemana.toString(),
        diaSemana: currentHorario.diaSemana,
        horaInicio: currentHorario.horaInicio,
        horaFin: currentHorario.horaFin,
        disponible: currentHorario.disponible
      };

      console.log('Enviando horario:', horarioData);
      await horarioService.crearHorario(horarioData);
      setOpenDialog(false);
      loadHorarios();
      setSnackbar({
        open: true,
        message: 'Horario creado exitosamente',
        severity: 'success'
      });
    } catch (error) {
      console.error('Error al crear horario:', error);
      setSnackbar({
        open: true,
        message: 'Error al crear el horario',
        severity: 'error'
      });
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('¿Está seguro de eliminar este horario?')) {
      try {
        await horarioService.actualizarDisponibilidad(id, false);
        loadHorarios();
        setSnackbar({
          open: true,
          message: 'Horario eliminado exitosamente',
          severity: 'success'
        });
      } catch (err) {
        console.error('Error:', err);
        setSnackbar({
          open: true,
          message: 'Error al eliminar el horario',
          severity: 'error'
        });
      }
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  if (user?.tipo !== TipoUsuario.ODONTOLOGO) {
    return (
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Alert severity="error">
          Solo los odontólogos pueden gestionar sus horarios.
        </Alert>
      </Container>
    );
  }

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Paper sx={{ p: 2 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography component="h1" variant="h5">
            Gestión de Horarios
          </Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => handleOpenDialog()}
          >
            Nuevo Horario
          </Button>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Día</TableCell>
                <TableCell>Hora Inicio</TableCell>
                <TableCell>Hora Fin</TableCell>
                <TableCell>Disponible</TableCell>
                <TableCell>Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {horarios.map((horario) => (
                <TableRow key={horario.id}>
                  <TableCell>{horario.diaSemana}</TableCell>
                  <TableCell>{horario.horaInicio}</TableCell>
                  <TableCell>{horario.horaFin}</TableCell>
                  <TableCell>
                    <Switch
                      checked={horario.disponible}
                      onChange={() => handleOpenDialog(horario)}
                    />
                  </TableCell>
                  <TableCell>
                    <IconButton
                      color="primary"
                      onClick={() => handleOpenDialog(horario)}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      color="error"
                      onClick={() => handleDelete(horario.id!)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>
          {currentHorario.id ? 'Editar Horario' : 'Nuevo Horario'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ pt: 2 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <InputLabel>Día de la Semana</InputLabel>
              <Select
                value={currentHorario.diaSemana}
                label="Día de la Semana"
                onChange={(e) => setCurrentHorario({
                  ...currentHorario,
                  diaSemana: e.target.value as DiaSemana
                })}
              >
                {Object.values(DiaSemana).map((dia) => (
                  <MenuItem key={dia} value={dia}>
                    {dia}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <TextField
              fullWidth
              label="Hora Inicio"
              type="time"
              value={currentHorario.horaInicio}
              onChange={(e) => setCurrentHorario({
                ...currentHorario,
                horaInicio: e.target.value
              })}
              InputLabelProps={{ shrink: true }}
              sx={{ mb: 2 }}
            />

            <TextField
              fullWidth
              label="Hora Fin"
              type="time"
              value={currentHorario.horaFin}
              onChange={(e) => setCurrentHorario({
                ...currentHorario,
                horaFin: e.target.value
              })}
              InputLabelProps={{ shrink: true }}
              sx={{ mb: 2 }}
            />

            <FormControl fullWidth>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography>Disponible</Typography>
                <Switch
                  checked={currentHorario.disponible}
                  onChange={(e) => setCurrentHorario({
                    ...currentHorario,
                    disponible: e.target.checked
                  })}
                />
              </Box>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained">
            Guardar
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert onClose={handleCloseSnackbar} severity={snackbar.severity}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default Horarios; 