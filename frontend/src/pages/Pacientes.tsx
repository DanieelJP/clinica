import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  TextField,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  CircularProgress,
  Snackbar,
  Alert
} from '@mui/material';
import { Add, Edit, Delete, Search, PersonAdd } from '@mui/icons-material';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { Paciente, TipoPago } from '../types/models';
import pacienteService from '../services/pacienteService';
import { format } from 'date-fns';

const Pacientes: React.FC = () => {
  const [pacientes, setPacientes] = useState<Paciente[]>([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [currentPaciente, setCurrentPaciente] = useState<Paciente | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  const validationSchema = Yup.object({
    dni: Yup.string().required('El DNI es obligatorio'),
    nombre: Yup.string().required('El nombre es obligatorio'),
    apellidos: Yup.string().required('Los apellidos son obligatorios'),
    fechaNacimiento: Yup.date().required('La fecha de nacimiento es obligatoria'),
    telefono: Yup.string().required('El teléfono es obligatorio'),
    tipoPago: Yup.string().required('El tipo de pago es obligatorio'),
    mutua: Yup.string().when('tipoPago', {
      is: TipoPago.MUTUA,
      then: (schema) => schema.required('La mutua es obligatoria para este tipo de pago'),
      otherwise: (schema) => schema.optional()
    }),
    obraSocial: Yup.string().optional()
  });

  useEffect(() => {
    fetchPacientes();
  }, []);

  const fetchPacientes = async () => {
    try {
      setLoading(true);
      const data = await pacienteService.getAll();
      setPacientes(data);
    } catch (error) {
      console.error('Error al obtener pacientes:', error);
      setSnackbar({
        open: true,
        message: 'Error al cargar los pacientes',
        severity: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (paciente: Paciente | null = null) => {
    setCurrentPaciente(paciente);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setCurrentPaciente(null);
  };

  const handleSearch = async () => {
    if (searchTerm.trim() === '') {
      fetchPacientes();
      return;
    }
    
    try {
      setLoading(true);
      const data = await pacienteService.search(searchTerm);
      setPacientes(data);
    } catch (error) {
      console.error('Error al buscar pacientes:', error);
      setSnackbar({
        open: true,
        message: 'Error al buscar pacientes',
        severity: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (dni: string) => {
    if (window.confirm('¿Está seguro de que desea eliminar este paciente?')) {
      try {
        await pacienteService.delete(dni);
        setPacientes(pacientes.filter(p => p.dni !== dni));
        setSnackbar({
          open: true,
          message: 'Paciente eliminado correctamente',
          severity: 'success'
        });
      } catch (error) {
        console.error('Error al eliminar paciente:', error);
        setSnackbar({
          open: true,
          message: 'Error al eliminar el paciente',
          severity: 'error'
        });
      }
    }
  };

  const handleSubmit = async (values: Paciente) => {
    try {
      if (currentPaciente) {
        // Actualizar paciente existente
        await pacienteService.update(values.dni, values);
        setPacientes(pacientes.map(p => p.dni === values.dni ? values : p));
        setSnackbar({
          open: true,
          message: 'Paciente actualizado correctamente',
          severity: 'success'
        });
      } else {
        // Crear nuevo paciente
        const newPaciente = await pacienteService.create(values);
        setPacientes([...pacientes, newPaciente]);
        setSnackbar({
          open: true,
          message: 'Paciente creado correctamente',
          severity: 'success'
        });
      }
      handleCloseDialog();
    } catch (error) {
      console.error('Error al guardar paciente:', error);
      setSnackbar({
        open: true,
        message: 'Error al guardar el paciente',
        severity: 'error'
      });
    }
  };

  const initialValues: Paciente = currentPaciente || {
    dni: '',
    nombre: '',
    apellidos: '',
    fechaNacimiento: '',
    telefono: '',
    obraSocial: '',
    mutua: '',
    tipoPago: TipoPago.PARTICULAR
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Paper sx={{ p: 2 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h5" component="h2">
            Gestión de Pacientes
          </Typography>
          <Button
            variant="contained"
            color="primary"
            startIcon={<Add />}
            onClick={() => handleOpenDialog()}
          >
            Nuevo Paciente
          </Button>
        </Box>

        <Box sx={{ display: 'flex', mb: 2 }}>
          <TextField
            label="Buscar paciente"
            variant="outlined"
            size="small"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            sx={{ mr: 1, flexGrow: 1 }}
          />
          <Button
            variant="outlined"
            startIcon={<Search />}
            onClick={handleSearch}
          >
            Buscar
          </Button>
        </Box>

        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
            <CircularProgress />
          </Box>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>DNI</TableCell>
                  <TableCell>Nombre</TableCell>
                  <TableCell>Apellidos</TableCell>
                  <TableCell>Fecha Nacimiento</TableCell>
                  <TableCell>Teléfono</TableCell>
                  <TableCell>Tipo Pago</TableCell>
                  <TableCell>Acciones</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {pacientes.length > 0 ? (
                  pacientes.map((paciente) => (
                    <TableRow key={paciente.dni}>
                      <TableCell>{paciente.dni}</TableCell>
                      <TableCell>{paciente.nombre}</TableCell>
                      <TableCell>{paciente.apellidos}</TableCell>
                      <TableCell>
                        {paciente.fechaNacimiento ? format(new Date(paciente.fechaNacimiento), 'dd/MM/yyyy') : ''}
                      </TableCell>
                      <TableCell>{paciente.telefono}</TableCell>
                      <TableCell>{paciente.tipoPago}</TableCell>
                      <TableCell>
                        <IconButton color="primary" onClick={() => handleOpenDialog(paciente)}>
                          <Edit />
                        </IconButton>
                        <IconButton color="error" onClick={() => handleDelete(paciente.dni)}>
                          <Delete />
                        </IconButton>
                        <IconButton color="secondary" onClick={() => {/* TODO: Implementar añadir responsable */}}>
                          <PersonAdd />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={7} align="center">
                      No hay pacientes registrados
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      {/* Dialog para crear/editar paciente */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {currentPaciente ? 'Editar Paciente' : 'Nuevo Paciente'}
        </DialogTitle>
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          {({ values, errors, touched, handleChange, handleBlur, isSubmitting, setFieldValue }) => (
            <Form>
              <DialogContent>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 2 }}>
                    <Box sx={{ flex: 1 }}>
                      <TextField
                        fullWidth
                        name="dni"
                        label="DNI"
                        value={values.dni}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.dni && Boolean(errors.dni)}
                        helperText={touched.dni && errors.dni}
                        disabled={Boolean(currentPaciente)} // No permitir cambiar el DNI si es edición
                      />
                    </Box>
                    <Box sx={{ flex: 1 }}>
                      <TextField
                        fullWidth
                        name="nombre"
                        label="Nombre"
                        value={values.nombre}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.nombre && Boolean(errors.nombre)}
                        helperText={touched.nombre && errors.nombre}
                      />
                    </Box>
                  </Box>
                  
                  <TextField
                    fullWidth
                    name="apellidos"
                    label="Apellidos"
                    value={values.apellidos}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    error={touched.apellidos && Boolean(errors.apellidos)}
                    helperText={touched.apellidos && errors.apellidos}
                  />
                  
                  <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 2 }}>
                    <Box sx={{ flex: 1 }}>
                      <TextField
                        fullWidth
                        name="fechaNacimiento"
                        label="Fecha de Nacimiento"
                        type="date"
                        value={values.fechaNacimiento}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.fechaNacimiento && Boolean(errors.fechaNacimiento)}
                        helperText={touched.fechaNacimiento && errors.fechaNacimiento}
                        InputLabelProps={{ shrink: true }}
                      />
                    </Box>
                    <Box sx={{ flex: 1 }}>
                      <TextField
                        fullWidth
                        name="telefono"
                        label="Teléfono"
                        value={values.telefono}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.telefono && Boolean(errors.telefono)}
                        helperText={touched.telefono && errors.telefono}
                      />
                    </Box>
                  </Box>
                  
                  <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 2 }}>
                    <Box sx={{ flex: 1 }}>
                      <FormControl fullWidth>
                        <InputLabel id="tipo-pago-label">Tipo de Pago</InputLabel>
                        <Select
                          labelId="tipo-pago-label"
                          name="tipoPago"
                          value={values.tipoPago}
                          label="Tipo de Pago"
                          onChange={handleChange}
                        >
                          <MenuItem value={TipoPago.PARTICULAR}>Particular</MenuItem>
                          <MenuItem value={TipoPago.MUTUA}>Mutua</MenuItem>
                        </Select>
                      </FormControl>
                    </Box>
                    <Box sx={{ flex: 1 }}>
                      <TextField
                        fullWidth
                        name="obraSocial"
                        label="Obra Social"
                        value={values.obraSocial || ''}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.obraSocial && Boolean(errors.obraSocial)}
                        helperText={touched.obraSocial && errors.obraSocial}
                      />
                    </Box>
                  </Box>
                  
                  {values.tipoPago === TipoPago.MUTUA && (
                    <TextField
                      fullWidth
                      name="mutua"
                      label="Mutua"
                      value={values.mutua || ''}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.mutua && Boolean(errors.mutua)}
                      helperText={touched.mutua && errors.mutua}
                      required
                    />
                  )}
                </Box>
              </DialogContent>
              <DialogActions>
                <Button onClick={handleCloseDialog}>Cancelar</Button>
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? <CircularProgress size={24} /> : 'Guardar'}
                </Button>
              </DialogActions>
            </Form>
          )}
        </Formik>
      </Dialog>

      {/* Snackbar para notificaciones */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert
          onClose={() => setSnackbar({ ...snackbar, open: false })}
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default Pacientes;
