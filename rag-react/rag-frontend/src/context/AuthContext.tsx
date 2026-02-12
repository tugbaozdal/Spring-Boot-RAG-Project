import { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import apiClient from '../services/axios';

// User type tanımı
export interface User {
  id: string;
  username: string;
  email: string;
  [key: string]: unknown;
}

// Login request type
interface LoginRequest {
  email: string;
  password: string;
}

// Login response type
interface LoginResponse {
  jwtToken: string;
  user?: User;
}

// AuthContext type
interface AuthContextType {
  user: User | null;
  token: string | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
}

// Context oluştur
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// AuthProvider props
interface AuthProviderProps {
  children: ReactNode;
}

// AuthProvider component
export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // localStorage'dan token'ı yükle
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      setToken(storedToken);
      // Token varsa kullanıcı bilgilerini al
      fetchCurrentUser(storedToken);
    } else {
      setIsLoading(false);
    }
  }, []);

  // Mevcut kullanıcı bilgilerini al
  const fetchCurrentUser = async (authToken: string) => {
    try {
      const response = await apiClient.get('/user/current', {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      setUser(response.data);
    } catch (error) {
      console.error('Failed to fetch current user:', error);
      // Token geçersizse temizle
      localStorage.removeItem('token');
      setToken(null);
    } finally {
      setIsLoading(false);
    }
  };

  // Login fonksiyonu
  const login = async (credentials: LoginRequest) => {
    try {
      const response = await apiClient.post<LoginResponse>('/auth/login', credentials);
      const { jwtToken, user: userData } = response.data;

      // Token'ı localStorage'a kaydet
      localStorage.setItem('token', jwtToken);
      setToken(jwtToken);

      // Kullanıcı bilgisi response'da varsa kullan, yoksa tekrar fetch et
      if (userData) {
        setUser(userData);
      } else {
        await fetchCurrentUser(jwtToken);
      }
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  };

  // Logout fonksiyonu
  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  const value: AuthContextType = {
    user,
    token,
    isLoading,
    isAuthenticated: !!token && !!user,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// useAuth hook
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
