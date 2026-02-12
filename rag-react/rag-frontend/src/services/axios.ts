import axios from 'axios';
import type { AxiosError, InternalAxiosRequestConfig } from 'axios';

// Axios instance oluştur
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - JWT token'ı header'a ekle
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token');
    
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - 401 hatalarını handle et
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Token'ı localStorage'dan temizle
      localStorage.removeItem('token');
      
      // Login sayfasına yönlendir (isteğe bağlı)
      // window.location.href = '/login';
      
      // Veya bir hata mesajı göster
      console.error('Unauthorized: Token expired or invalid');
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;
