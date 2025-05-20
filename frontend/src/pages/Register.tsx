import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import {
  Box,
  Button,
  Container,
  FormControl,
  FormHelperText,
  Grid as MuiGrid,
  MenuItem,
  Paper,
  Select,
  TextField,
  Typography,
  Alert,
} from '@mui/material';
import { RegisterRequest } from '../types/models';
import authService from '../services/authService';

// Crear un componente Grid compatible con la versión actual de Material-UI
const Grid = (props: any) => <MuiGrid {...props} />;

// Esquema de validación
const RegisterSchema = Yup.object().shape({
  username: Yup.string()
    .min(3, 'El nombre de usuario debe tener al menos 3 caracteres')
    .max(50, 'El nombre de usuario debe tener como máximo 50 caracteres')
    .required('El nombre de usuario es obligatorio'),
  password: Yup.string()
    .min(6, 'La contraseña debe tener al menos 6 caracteres')
    .required('La contraseña es obligatoria'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password')], 'Las contraseñas deben coincidir')
    .required('Confirmar contraseña es obligatorio'),
  email: Yup.string()
    .email('Email inválido')
    .required('El email es obligatorio'),
  tipo: Yup.string()
    .oneOf(['Administrativo', 'Odontologo'], 'Tipo de usuario inválido')
    .required('El tipo de usuario es obligatorio'),
  matricula: Yup.string()
    .when('tipo', {
      is: 'Odontologo',
      then: (schema) => schema.required('La matrícula es obligatoria'),
      otherwise: (schema) => schema.notRequired(),
    }),
  especialidad: Yup.string()
    .when('tipo', {
      is: 'Odontologo',
      then: (schema) => schema.required('La especialidad es obligatoria'),
      otherwise: (schema) => schema.notRequired(),
    }),
});

const Register: React.FC = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);

  const initialValues = {
    username: '',
    password: '',
    confirmPassword: '',
    email: '',
    tipo: 'Administrativo',
    matricula: '',
    especialidad: '',
  };

  const handleSubmit = async (values: any) => {
    try {
      setError(null);
      
      // Eliminar confirmPassword antes de enviar al servidor
      const { confirmPassword, ...registerData } = values;
      
      await authService.register(registerData);
      setSuccess(true);
      
      // Redireccionar al login después de 2 segundos
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err: any) {
      console.error('Error al registrar usuario:', err);
      setError(err.response?.data?.message || 'Error al registrar usuario');
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          Registro de Usuario
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 2 }}>
            Usuario registrado correctamente. Redirigiendo al login...
          </Alert>
        )}

        <Formik
          initialValues={initialValues}
          validationSchema={RegisterSchema}
          onSubmit={handleSubmit}
        >
          {({ values, errors, touched, handleChange, isSubmitting }) => (
            <Form>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <Field
                    as={TextField}
                    fullWidth
                    name="username"
                    label="Nombre de usuario"
                    variant="outlined"
                    error={touched.username && Boolean(errors.username)}
                    helperText={touched.username && errors.username}
                  />
                </Grid>

                <Grid item xs={12}>
                  <Field
                    as={TextField}
                    fullWidth
                    name="email"
                    label="Email"
                    variant="outlined"
                    error={touched.email && Boolean(errors.email)}
                    helperText={touched.email && errors.email}
                  />
                </Grid>

                <Grid item xs={12}>
                  <Field
                    as={TextField}
                    fullWidth
                    name="password"
                    label="Contraseña"
                    type="password"
                    variant="outlined"
                    error={touched.password && Boolean(errors.password)}
                    helperText={touched.password && errors.password}
                  />
                </Grid>

                <Grid item xs={12}>
                  <Field
                    as={TextField}
                    fullWidth
                    name="confirmPassword"
                    label="Confirmar contraseña"
                    type="password"
                    variant="outlined"
                    error={touched.confirmPassword && Boolean(errors.confirmPassword)}
                    helperText={touched.confirmPassword && errors.confirmPassword}
                  />
                </Grid>

                <Grid item xs={12}>
                  <FormControl fullWidth error={touched.tipo && Boolean(errors.tipo)}>
                    <Field
                      as={Select}
                      name="tipo"
                      label="Tipo de usuario"
                      displayEmpty
                      value={values.tipo}
                      onChange={handleChange}
                    >
                      <MenuItem value="Administrativo">Administrativo</MenuItem>
                      <MenuItem value="Odontologo">Odontólogo</MenuItem>
                    </Field>
                    <ErrorMessage name="tipo" component={FormHelperText} />
                  </FormControl>
                </Grid>

                {values.tipo === 'Odontologo' && (
                  <>
                    <Grid item xs={12}>
                      <Field
                        as={TextField}
                        fullWidth
                        name="matricula"
                        label="Matrícula"
                        variant="outlined"
                        error={touched.matricula && Boolean(errors.matricula)}
                        helperText={touched.matricula && errors.matricula}
                      />
                    </Grid>

                    <Grid item xs={12}>
                      <Field
                        as={TextField}
                        fullWidth
                        name="especialidad"
                        label="Especialidad"
                        variant="outlined"
                        error={touched.especialidad && Boolean(errors.especialidad)}
                        helperText={touched.especialidad && errors.especialidad}
                      />
                    </Grid>
                  </>
                )}

                <Grid item xs={12}>
                  <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    disabled={isSubmitting}
                  >
                    {isSubmitting ? 'Registrando...' : 'Registrar'}
                  </Button>
                </Grid>
              </Grid>
            </Form>
          )}
        </Formik>
      </Paper>
    </Container>
  );
};

export default Register;
