/**
 * Post API service
 */

import api from './api';
import { API_ENDPOINTS } from '../config/apiConfig';
import type { PostResponse, PostRequest } from '../types';

/**
 * Create a new post
 */
export const createPost = async (postData: PostRequest): Promise<PostResponse> => {
  const response = await api.post<PostResponse>(API_ENDPOINTS.posts, postData);
  return response.data;
};

/**
 * Get post by ID
 */
export const getPostById = async (postId: number): Promise<PostResponse> => {
  const response = await api.get<PostResponse>(API_ENDPOINTS.postById(postId));
  return response.data;
};

/**
 * Get all posts by a specific user
 */
export const getPostsByUser = async (userId: number): Promise<PostResponse[]> => {
  const response = await api.get<PostResponse[]>(API_ENDPOINTS.postsByUser(userId));
  return response.data;
};

/**
 * Update a post
 */
export const updatePost = async (postId: number, postData: Partial<PostRequest>): Promise<PostResponse> => {
  const response = await api.put<PostResponse>(API_ENDPOINTS.postById(postId), postData);
  return response.data;
};

/**
 * Delete a post
 */
export const deletePost = async (postId: number): Promise<void> => {
  await api.delete(API_ENDPOINTS.postById(postId));
};
