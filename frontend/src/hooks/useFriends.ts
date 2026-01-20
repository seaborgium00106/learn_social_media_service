/**
 * Custom hook for friendship data fetching and mutations using React Query
 */

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  getFriendshipsByUser,
  addFriend,
  removeFriend,
  checkFriendship,
} from '../services/friendshipService';
import { QUERY_KEYS } from '../utils/constants';
import type { FriendshipRequest } from '../types';

/**
 * Fetch friends for a user
 */
export const useFriends = (userId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.FRIENDSHIPS, userId],
    queryFn: () => (userId ? getFriendshipsByUser(userId) : Promise.resolve([])),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 10,
  });
};

/**
 * Check if two users are friends
 */
export const useCheckFriendship = (userId: number | null, friendId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.FRIENDSHIPS, 'check', userId, friendId],
    queryFn: () =>
      userId && friendId ? checkFriendship(userId, friendId) : Promise.resolve(false),
    enabled: !!userId && !!friendId,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 10,
  });
};

/**
 * Mutation to add a friend
 */
export const useAddFriend = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (friendshipData: FriendshipRequest) => addFriend(friendshipData),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.FRIENDSHIPS],
      });
    },
  });
};

/**
 * Mutation to remove a friend
 */
export const useRemoveFriend = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ userId, friendId }: { userId: number; friendId: number }) =>
      removeFriend(userId, friendId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.FRIENDSHIPS],
      });
    },
  });
};
