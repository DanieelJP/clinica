import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { CircularProgress, Box } from '@mui/material';

interface ProtectedRouteProps {
  requiredRole?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ requiredRole }) => {
  const { isAuthenticated, user, loading } = useAuth();

  // Mostrar un spinner mientras se verifica la autenticación
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  // Si el usuario no está autenticado, redirigir al login
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Si se requiere un rol específico y el usuario no lo tiene, redirigir a una página de acceso denegado
  if (requiredRole && user && !requiredRole.includes(user.tipo)) {
    return <Navigate to="/acceso-denegado" replace />;
  }

  // Si el usuario está autenticado y tiene el rol requerido (o no se requiere rol), mostrar el contenido
  return <Outlet />;
};

export default ProtectedRoute;
