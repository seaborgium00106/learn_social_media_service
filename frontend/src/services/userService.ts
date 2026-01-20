/**
 * User API service
 */

import api from './api';
import { API_ENDPOINTS } from '../config/apiConfig';
import type { UserResponse } from '../types';

/**
 * Get all users
 */
export const getAllUsers = async (): Promise<UserResponse[]> => {
  const response = await api.get<UserResponse[]>(API_ENDPOINTS.users);
  return response.data;
};

/**
 * Get user by ID
 */
export const getUserById = async (userId: number): Promise<UserResponse> => {
  const response = await api.get<UserResponse>(API_ENDPOINTS.userById(userId));
  return response.data;
};

/**
 * Search users (if backend implements this)
 */
export const searchUsers = async (searchTerm: string): Promise<UserResponse[]> => {
  const response = await api.get<UserResponse[]>(`${API_ENDPOINTS.users}?search=${searchTerm}`);
  return response.data;
};
