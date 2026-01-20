/**
 * Application constants
 */

export const ROUTES = {
  HOME: '/',
  PROFILE: '/profile/:userId',
  FRIENDS: '/friends',
  SEARCH_USERS: '/search',
  NOT_FOUND: '*',
} as const;

export const QUERY_KEYS = {
  TIMELINE: 'timeline',
  POSTS: 'posts',
  USERS: 'users',
  FRIENDSHIPS: 'friendships',
  TIMELINE_COUNT: 'timelineCount',
} as const;

export const MESSAGES = {
  POST_CREATED: 'Post created successfully!',
  POST_DELETED: 'Post deleted successfully!',
  FRIEND_ADDED: 'Friend added successfully!',
  FRIEND_REMOVED: 'Friend removed successfully!',
  ERROR_GENERIC: 'An error occurred. Please try again.',
  ERROR_NETWORK: 'Network error. Please check your connection.',
  LOADING: 'Loading...',
  NO_POSTS: 'No posts to display.',
  NO_FRIENDS: 'No friends yet. Start by adding some!',
} as const;

export const VALIDATION = {
  POST_MIN_LENGTH: 1,
  POST_MAX_LENGTH: 1000,
  NAME_MIN_LENGTH: 2,
  NAME_MAX_LENGTH: 100,
  EMAIL_PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
} as const;

// Re-export APP_CONFIG from apiConfig
export { APP_CONFIG } from '../config/apiConfig';
