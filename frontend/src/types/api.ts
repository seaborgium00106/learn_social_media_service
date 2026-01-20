/**
 * API request and response types
 */

// User API types
export interface UserRequest {
  username: string;
  email: string;
}

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  createdAt?: string;
}

// Post API types
export interface PostRequest {
  text: string;
  userId: number;
}

export interface PostResponse {
  id: number;
  text: string;
  userId: number;
  createdAt: string;
  updatedAt?: string;
}

// Friendship API types
export interface FriendshipRequest {
  userId: number;
  friendId: number;
}

export interface FriendshipResponse {
  id: number;
  userId: number;
  username: string;
  friendId: number;
  friendUsername: string;
  createdAt: string;
}

// Timeline API types
export interface TimelinePostResponse {
  id: number;
  text: string;
  userId: number;
  username: string;
  createdAt: string;
  updatedAt?: string;
}

export interface TimelineFilterParams {
  page?: number;
  size?: number;
  fromDate?: string;
  toDate?: string;
}

// Error response
export interface ApiError {
  message: string;
  status: number;
  timestamp?: string;
}
