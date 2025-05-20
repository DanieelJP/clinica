import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, IconButton, Menu, MenuItem } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { AccountCircle, ExitToApp } from '@mui/icons-material';
import { TipoUsuario } from '../types/models';

const Navigation: React.FC = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
    handleClose();
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          <Link to="/" style={{ textDecoration: 'none', color: 'white' }}>
            Clínica Odontológica
          </Link>
        </Typography>

        {isAuthenticated ? (
          <>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              {/* Enlaces comunes para todos los usuarios */}
              <Button color="inherit" component={Link} to="/pacientes">
                Pacientes
              </Button>
              <Button color="inherit" component={Link} to="/visitas">
                Visitas
              </Button>
              <Button color="inherit" component={Link} to="/horarios">
                Horarios
              </Button>
              
              {/* Enlaces específicos para administrativos */}
              {user?.tipo === TipoUsuario.ADMINISTRATIVO && (
                <>
                  <Button color="inherit" component={Link} to="/odontologos">
                    Odontólogos
                  </Button>
                  <Button color="inherit" component={Link} to="/tratamientos">
                    Tratamientos
                  </Button>
                </>
              )}
              
              {/* Menú de usuario */}
              <IconButton
                size="large"
                aria-label="cuenta del usuario"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleMenu}
                color="inherit"
              >
                <AccountCircle />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
              >
                <MenuItem onClick={() => { handleClose(); navigate('/perfil'); }}>
                  Mi Perfil
                </MenuItem>
                <MenuItem onClick={handleLogout}>
                  <ExitToApp fontSize="small" sx={{ mr: 1 }} />
                  Cerrar Sesión
                </MenuItem>
              </Menu>
            </Box>
          </>
        ) : (
          <Button color="inherit" component={Link} to="/login">
            Iniciar Sesión
          </Button>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navigation;
