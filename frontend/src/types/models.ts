/**
 * Core domain models matching backend DTOs
 */

export interface User {
  id: number;
  username: string;
  email: string;
  createdAt?: string;
}

export interface Post {
  id: number;
  text: string;
  userId: number;
  createdAt: string;
  updatedAt?: string;
}

export interface Friendship {
  id: number;
  userId: number;
  friendId: number;
  createdAt: string;
}

export interface TimelinePost {
  id: number;
  text: string;
  userId: number;
  username: string;
  createdAt: string;
  updatedAt?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
