import React, { createContext, useContext, useState, useEffect } from 'react';
import AuthService from '../lib/authService';

// Create Auth Context
const AuthContext = createContext();

// Custom hook to use auth context
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

// Auth Provider Component
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  // Initialize auth state on mount
  useEffect(() => {
    const initializeAuth = () => {
      const savedToken = AuthService.getToken();
      const savedUser = AuthService.getUser();
      
      if (savedToken && savedUser) {
        setToken(savedToken);
        setUser(savedUser);
      }
      
      setLoading(false);
    };

    initializeAuth();
  }, []);

  // Login function
  const login = async (email, password) => {
    setLoading(true);
    try {
      const result = await AuthService.login(email, password);
      
      if (result.success) {
        setUser(result.user);
        setToken(result.token);
        return { success: true };
      }
      
      return { success: false, message: result.message };
    } catch (error) {
      return { success: false, message: 'Login failed' };
    } finally {
      setLoading(false);
    }
  };

  // Register function
  const register = async (userData) => {
    setLoading(true);
    try {
      const result = await AuthService.register(userData);
      
      if (result.success) {
        setUser(result.user);
        setToken(result.token);
        return { success: true };
      }
      
      return { success: false, message: result.message };
    } catch (error) {
      return { success: false, message: 'Registration failed' };
    } finally {
      setLoading(false);
    }
  };

  // Logout function
  const logout = async () => {
    setLoading(true);
    try {
      await AuthService.logoutUser();
    } finally {
      setUser(null);
      setToken(null);
      setLoading(false);
    }
  };

  // Check if user is authenticated
  const isAuthenticated = () => {
    return !!(user && token);
  };

  // Check if user is admin
  const isAdmin = () => {
    return user && user.role === 'SUPER_USER';
  };

  // Forgot password function
  const forgotPassword = async (email) => {
    return await AuthService.forgotPassword(email);
  };

  // Reset password function
  const resetPassword = async (token, newPassword) => {
    return await AuthService.resetPassword(token, newPassword);
  };

  const value = {
    user,
    token,
    loading,
    login,
    register,
    logout,
    isAuthenticated,
    isAdmin,
    forgotPassword,
    resetPassword
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;