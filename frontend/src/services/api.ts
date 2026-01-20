/**
 * Axios instance configuration with interceptors
 */

import axios, { AxiosError } from 'axios';
import type { AxiosInstance } from 'axios';
import { API_CONFIG } from '../config/apiConfig';

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: API_CONFIG.baseURL,
  timeout: API_CONFIG.timeout,
  headers: API_CONFIG.headers,
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    // Add any authentication token here if needed
    // const token = localStorage.getItem('authToken');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Handle unauthorized - redirect to login if needed
      console.error('Unauthorized access');
    } else if (error.response?.status === 403) {
      console.error('Forbidden');
    } else if (error.response?.status === 404) {
      console.error('Resource not found');
    } else if (error.message === 'Network Error') {
      console.error('Network error - check backend connection');
    }
    return Promise.reject(error);
  }
);

export default api;
