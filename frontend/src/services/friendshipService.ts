/**
 * Friendship API service
 */

import api from './api';
import { API_ENDPOINTS } from '../config/apiConfig';
import type { FriendshipResponse, FriendshipRequest } from '../types';

/**
 * Get all friendships for a user
 */
export const getFriendshipsByUser = async (userId: number): Promise<FriendshipResponse[]> => {
  const response = await api.get<FriendshipResponse[]>(API_ENDPOINTS.friendshipsByUser(userId));
  return response.data;
};

/**
 * Add a friend
 */
export const addFriend = async (friendshipData: FriendshipRequest): Promise<FriendshipResponse> => {
  const response = await api.post<FriendshipResponse>(API_ENDPOINTS.friendships, friendshipData);
  return response.data;
};

/**
 * Remove a friend
 */
export const removeFriend = async (userId: number, friendId: number): Promise<void> => {
  await api.delete(API_ENDPOINTS.friendships, {
    params: { userId, friendId },
  });
};

/**
 * Check if two users are friends
 */
export const checkFriendship = async (userId: number, friendId: number): Promise<boolean> => {
  try {
    const response = await api.get<{ isFriend: boolean }>(API_ENDPOINTS.checkFriendship, {
      params: { userId, friendId },
    });
    return response.data.isFriend;
  } catch {
    return false;
  }
};
