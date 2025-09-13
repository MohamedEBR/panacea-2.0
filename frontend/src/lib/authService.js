// Auth service to handle JWT token operations and API calls
import http from './http';

class AuthService {
  
  static TOKEN_KEY = 'panacea_token';
  static USER_KEY = 'panacea_user';

  // Store token in localStorage
  static setToken(token) {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  // Get token from localStorage
  static getToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Store user info in localStorage
  static setUser(user) {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  // Get user info from localStorage
  static getUser() {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  // Check if user is authenticated
  static isAuthenticated() {
    const token = this.getToken();
    const user = this.getUser();
    return !!(token && user);
  }

  // Check if user is admin (SUPER_USER)
  static isAdmin() {
    const user = this.getUser();
    return user && user.role === 'SUPER_USER';
  }

  // Clear authentication data
  static logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  // Login user
  static async login(email, password) {
    try {
      const response = await http.post('/api/v1/auth/authenticate', {
        data: { email, password }
      });
      
      if (response.data && response.data.token) {
        this.setToken(response.data.token);
        // Decode JWT to get user info (you could also get this from the response)
        const user = {
          email: response.data.email || email,
          name: response.data.name || '',
          role: response.data.role || 'USER'
        };
        this.setUser(user);
        return { success: true, user, token: response.data.token };
      }
      
      return { success: false, message: 'Invalid response format' };
    } catch (error) {
      console.error('Login error:', error);
      const message = error.response?.data?.message || error.response?.data || 'Login failed';
      return { success: false, message };
    }
  }

  // Register user
  static async register(userData) {
    try {
      const response = await http.post('/api/v1/auth/register', {
        data: userData
      });
      
      if (response.data && response.data.token) {
        this.setToken(response.data.token);
        const user = {
          email: userData.email,
          name: userData.name || '',
          role: response.data.role || 'USER'
        };
        this.setUser(user);
        return { success: true, user, token: response.data.token };
      }
      
      return { success: false, message: 'Registration failed' };
    } catch (error) {
      console.error('Registration error:', error);
      const message = error.response?.data?.message || error.response?.data || 'Registration failed';
      return { success: false, message };
    }
  }

  // Get auth headers for API calls
  static getAuthHeaders() {
    const token = this.getToken();
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  // Logout user
  static async logoutUser() {
    try {
      const token = this.getToken();
      if (token) {
        await http.post('/api/v1/auth/logout', {
          headers: { Authorization: `Bearer ${token}` }
        });
      }
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      this.logout();
    }
  }

  // Forgot password
  static async forgotPassword(email) {
    try {
      await http.post('/api/v1/auth/forgot-password', {
        data: { email }
      });
      return { success: true, message: 'Password reset email sent' };
    } catch (error) {
      console.error('Forgot password error:', error);
      const message = error.response?.data?.message || 'Failed to send reset email';
      return { success: false, message };
    }
  }

  // Reset password
  static async resetPassword(token, newPassword) {
    try {
      await http.post('/api/v1/auth/reset-password', {
        data: { token, newPassword }
      });
      return { success: true, message: 'Password reset successfully' };
    } catch (error) {
      console.error('Reset password error:', error);
      const message = error.response?.data?.message || 'Failed to reset password';
      return { success: false, message };
    }
  }
}

export default AuthService;