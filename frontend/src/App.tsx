import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import './App.css';

// Contexto
import { AuthProvider } from './context/AuthContext';

// Componentes
import Navigation from './components/Navigation';
import ProtectedRoute from './components/ProtectedRoute';

// Páginas
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Pacientes from './pages/Pacientes';
import Horarios from './pages/Horarios';
import Visitas from './pages/Visitas';

// Definir tema
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#9c27b0',
    },
  },
});

function App() {
  return (
    <Router>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <div className="App">
            <Navigation />
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              
              {/* Rutas protegidas */}
              <Route element={<ProtectedRoute />}>
                <Route path="/" element={<Dashboard />} />
                <Route path="/pacientes" element={<Pacientes />} />
                <Route path="/horarios" element={<Horarios />} />
                <Route path="/visitas" element={<Visitas />} />
                {/* Aquí se añadirán más rutas protegidas */}
              </Route>
              
              {/* Ruta por defecto - Redirigir a Dashboard */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </div>
        </AuthProvider>
      </ThemeProvider>
    </Router>
  );
}

export default App;
