import axios from 'axios'

// When building the client into a static file, we do not need to include the server path as it is returned by it
const domain = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080'

// Create axios instance with default config
const axiosInstance = axios.create({
  baseURL: domain,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
axiosInstance.interceptors.request.use(
  (config) => {
    // Get token from localStorage
    const token = localStorage.getItem('panacea_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // If we get a 401 or 403, the token might be expired
    if (error.response?.status === 401 || error.response?.status === 403) {
      // Clear auth data
      localStorage.removeItem('panacea_token');
      localStorage.removeItem('panacea_user');
      
      // Only redirect to login if we're not already there
      if (window.location.pathname !== '/login' && window.location.pathname !== '/signup') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

const http = (url, { method = 'GET', data = undefined, headers = {} } = {}) => {
  return axiosInstance({
    url,
    method,
    data,
    headers,
  })
}

// Main functions to handle different types of endpoints
const get = (url, opts = {}) => http(url, { ...opts })
const post = (url, opts = {}) => http(url, { method: 'POST', ...opts })
const put = (url, opts = {}) => http(url, { method: 'PUT', ...opts })
const deleteData = (url, opts = {}) => http(url, { method: 'DELETE', ...opts })

const methods = {
  get,
  post,
  put,
  delete: deleteData,
  axiosInstance, // Export the instance for direct use if needed
}

export default methods