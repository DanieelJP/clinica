import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Box,
  Typography, 
  Paper,
  Card,
  CardContent,
  CardHeader,
  List,
  ListItem,
  ListItemText,
  Divider,
  CircularProgress,
  Stack
} from '@mui/material';
import { useAuth } from '../context/AuthContext';
import { TipoUsuario } from '../types/models';
import { visitaService } from '../services/visitaService';
import pacienteService from '../services/pacienteService';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [visitasHoy, setVisitasHoy] = useState<any[]>([]);
  const [proximasVisitas, setProximasVisitas] = useState<any[]>([]);
  const [totalPacientes, setTotalPacientes] = useState<number>(0);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Obtener la fecha de hoy en formato ISO
        const today = new Date().toISOString().split('T')[0];
        
        // Cargar visitas según el tipo de usuario
        let visitasResponse: any[] = [];
        if (user?.tipo === TipoUsuario.ODONTOLOGO) {
          visitasResponse = await visitaService.getVisitsByDentist(user.id);
        } else {
          visitasResponse = await visitaService.getVisitsByDate(today);
        }
        
        // Filtrar visitas de hoy y próximas
        const now = new Date();
        const hoy = visitasResponse.filter(visita => {
          const visitaDate = new Date(visita.fechaHora);
          return visitaDate.toDateString() === now.toDateString();
        });
        
        const proximas = visitasResponse.filter(visita => {
          const visitaDate = new Date(visita.fechaHora);
          return visitaDate > now;
        }).sort((a, b) => new Date(a.fechaHora).getTime() - new Date(b.fechaHora).getTime())
          .slice(0, 5); // Mostrar solo las 5 próximas visitas
        
        setVisitasHoy(hoy);
        setProximasVisitas(proximas);
        
        // Si es administrativo, obtener también el total de pacientes
        if (user?.tipo === TipoUsuario.ADMINISTRATIVO) {
          const pacientesResponse = await pacienteService.getAll();
          setTotalPacientes(pacientesResponse.length);
        }
        
      } catch (err) {
        console.error('Error al cargar los datos del dashboard:', err);
        setError('Error al cargar los datos. Por favor, inténtelo de nuevo más tarde.');
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchData();
    }
  }, [user]);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Container>
        <Box sx={{ mt: 4 }}>
          <Typography color="error" variant="h6">{error}</Typography>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Stack spacing={3}>
        {/* Encabezado */}
        <Paper sx={{ p: 2 }}>
          <Typography component="h1" variant="h5">
            Bienvenido, {user?.username}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            {user?.tipo === TipoUsuario.ADMINISTRATIVO ? 'Panel Administrativo' : 'Panel de Odontólogo'}
          </Typography>
        </Paper>

        {/* Sección de tarjetas */}
        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: 2 }}>
          {/* Visitas de hoy */}
          <Card sx={{ flex: 1 }}>
            <CardHeader title="Visitas de Hoy" />
            <CardContent>
              {visitasHoy.length > 0 ? (
                <List>
                  {visitasHoy.map((visita, index) => (
                    <React.Fragment key={visita.id}>
                      <ListItem>
                        <ListItemText
                          primary={`${format(new Date(visita.fechaHora), 'HH:mm')} - ${visita.paciente?.nombre || 'N/A'} ${visita.paciente?.apellidos || ''}`}
                          secondary={`Motivo: ${visita.motivo} | Estado: ${visita.estado}`}
                        />
                      </ListItem>
                      {index < visitasHoy.length - 1 && <Divider />}
                    </React.Fragment>
                  ))}
                </List>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  No hay visitas programadas para hoy.
                </Typography>
              )}
            </CardContent>
          </Card>

          {/* Próximas visitas */}
          <Card sx={{ flex: 1 }}>
            <CardHeader title="Próximas Visitas" />
            <CardContent>
              {proximasVisitas.length > 0 ? (
                <List>
                  {proximasVisitas.map((visita, index) => (
                    <React.Fragment key={visita.id}>
                      <ListItem>
                        <ListItemText
                          primary={`${format(new Date(visita.fechaHora), 'dd/MM/yyyy HH:mm')} - ${visita.paciente?.nombre || 'N/A'} ${visita.paciente?.apellidos || ''}`}
                          secondary={`Motivo: ${visita.motivo}`}
                        />
                      </ListItem>
                      {index < proximasVisitas.length - 1 && <Divider />}
                    </React.Fragment>
                  ))}
                </List>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  No hay próximas visitas programadas.
                </Typography>
              )}
            </CardContent>
          </Card>
        </Box>

        {/* Estadísticas adicionales para administrativos */}
        {user?.tipo === TipoUsuario.ADMINISTRATIVO && (
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Estadísticas
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 2 }}>
              <Box sx={{ flex: 1, textAlign: 'center', p: 2 }}>
                <Typography variant="h4">{totalPacientes}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Total de Pacientes
                </Typography>
              </Box>
              <Box sx={{ flex: 1, textAlign: 'center', p: 2 }}>
                <Typography variant="h4">{visitasHoy.length}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Visitas Hoy
                </Typography>
              </Box>
              <Box sx={{ flex: 1, textAlign: 'center', p: 2 }}>
                <Typography variant="h4">
                  {format(new Date(), 'EEEE, d MMMM yyyy', { locale: es })}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Fecha Actual
                </Typography>
              </Box>
            </Box>
          </Paper>
        )}
      </Stack>
    </Container>
  );
};

export default Dashboard;
