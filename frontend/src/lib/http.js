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

// Flexible helper supporting:
// - http(url, { method, data, headers, params })
// - http(url, data)
// - http(url, data, { headers })
const http = (url, arg1 = {}, arg2 = undefined) => {
  // Case A: second arg is an options object (has data/method/headers)
  if (arg1 && (arg1.data !== undefined || arg1.method !== undefined || arg1.headers !== undefined || arg1.params !== undefined)) {
    const { method = 'GET', data = undefined, headers = {}, params = undefined } = arg1
    return axiosInstance({ url, method, data, headers, params })
  }
  // Case B: second arg is the raw data body, optional third arg for options
  const data = arg1
  const opts = arg2 || {}
  const method = opts.method || 'GET'
  const headers = opts.headers || {}
  const params = opts.params
  return axiosInstance({ url, method, data, headers, params })
}

// Main functions to handle different types of endpoints
const get = (url, opts = {}) => http(url, { ...opts })
const post = (url, dataOrOpts = {}, maybeOpts = undefined) => {
  // Support post(url, { data }) and post(url, data)
  if (dataOrOpts && (dataOrOpts.data !== undefined || dataOrOpts.headers !== undefined || dataOrOpts.method !== undefined)) {
    return http(url, { method: 'POST', ...dataOrOpts })
  }
  return http(url, dataOrOpts, { method: 'POST', ...(maybeOpts || {}) })
}
const put = (url, dataOrOpts = {}, maybeOpts = undefined) => {
  if (dataOrOpts && (dataOrOpts.data !== undefined || dataOrOpts.headers !== undefined || dataOrOpts.method !== undefined)) {
    return http(url, { method: 'PUT', ...dataOrOpts })
  }
  return http(url, dataOrOpts, { method: 'PUT', ...(maybeOpts || {}) })
}
const deleteData = (url, dataOrOpts = {}, maybeOpts = undefined) => {
  if (dataOrOpts && (dataOrOpts.data !== undefined || dataOrOpts.headers !== undefined || dataOrOpts.method !== undefined)) {
    return http(url, { method: 'DELETE', ...dataOrOpts })
  }
  return http(url, dataOrOpts, { method: 'DELETE', ...(maybeOpts || {}) })
}

const methods = {
  get,
  post,
  put,
  delete: deleteData,
  axiosInstance, // Export the instance for direct use if needed
}

export default methods