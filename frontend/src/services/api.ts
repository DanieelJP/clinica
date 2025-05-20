import axios from 'axios';

// Crear una instancia de axios con la URL base
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
  withCredentials: true, // Importante para CORS con credenciales
});

// Interceptor para añadir el token JWT a las peticiones
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    // Asegurarse de que el Content-Type esté establecido para todas las peticiones
    config.headers['Content-Type'] = 'application/json';
    console.log('Enviando solicitud a:', config.url, 'con método:', config.method, 'headers:', config.headers);
    return config;
  },
  (error) => {
    console.error('Error en la solicitud:', error);
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores de respuesta
api.interceptors.response.use(
  (response) => {
    console.log('Respuesta recibida:', response.status, response.data);
    return response;
  },
  (error) => {
    console.error('Error en la respuesta:', error);

    if (error.response) {
      console.error('Status:', error.response.status);
      console.error('Datos:', error.response.data);
      
      // Si el error es 401 (Unauthorized), redirigir al login
      if (error.response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
      
      // Si el error es 403 (Forbidden)
      if (error.response.status === 403) {
        console.error('Acceso prohibido al recurso');
      }
    } else if (error.request) {
      console.error('No se recibió respuesta del servidor');
    } else {
      console.error('Error al configurar la solicitud');
    }
    
    return Promise.reject(error);
  }
);

export default api;
