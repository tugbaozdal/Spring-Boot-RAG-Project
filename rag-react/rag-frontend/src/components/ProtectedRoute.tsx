import { Navigate } from 'react-router-dom';
import type { ReactNode } from 'react';

interface ProtectedRouteProps {
  children: ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  // localStorage'dan JWT token kontrolü
  const token = localStorage.getItem('token');

  // Token yoksa login sayfasına yönlendir
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // Token varsa children'ı render et
  return <>{children}</>;
};

export default ProtectedRoute;
