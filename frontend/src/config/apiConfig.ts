/**
 * API Configuration
 */

export const API_CONFIG = {
  // Updated to use NGINX load balancer on port 80
  // Set VITE_API_BASE_URL=http://localhost:9091 to bypass load balancer
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost',
  timeout: Number(import.meta.env.VITE_API_TIMEOUT) || 10000,
  headers: {
    'Content-Type': 'application/json',
  },
};

export const APP_CONFIG = {
  appName: import.meta.env.VITE_APP_NAME || 'Social Network',
  defaultPageSize: Number(import.meta.env.VITE_DEFAULT_PAGE_SIZE) || 10,
};

export const API_ENDPOINTS = {
  // User endpoints
  users: '/api/v1/users',
  userById: (id: number) => `/api/v1/users/${id}`,
  
  // Post endpoints
  posts: '/api/v1/posts',
  postById: (id: number) => `/api/v1/posts/${id}`,
  postsByUser: (userId: number) => `/api/v1/posts/user/${userId}`,
  
  // Friendship endpoints
  friendships: '/api/v1/friendships',
  friendshipsByUser: (userId: number) => `/api/v1/friendships/user/${userId}`,
  checkFriendship: '/api/v1/friendships/check',
  
  // Timeline endpoints
  timeline: (userId: number) => `/api/v1/timeline/user/${userId}`,
  timelineFiltered: (userId: number) => `/api/v1/timeline/user/${userId}/filtered`,
  timelineCount: (userId: number) => `/api/v1/timeline/user/${userId}/count`,
};
