/**
 * Custom hook for user data fetching using React Query
 */

import { useQuery } from '@tanstack/react-query';
import { getUserById, getAllUsers, searchUsers } from '../services/userService';
import { QUERY_KEYS } from '../utils/constants';

/**
 * Fetch all users
 */
export const useAllUsers = () => {
  return useQuery({
    queryKey: [QUERY_KEYS.USERS],
    queryFn: () => getAllUsers(),
    staleTime: 1000 * 60 * 10,
    gcTime: 1000 * 60 * 15,
  });
};

/**
 * Fetch user by ID
 */
export const useUser = (userId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.USERS, userId],
    queryFn: () => (userId ? getUserById(userId) : Promise.resolve(null)),
    enabled: !!userId,
    staleTime: 1000 * 60 * 10,
    gcTime: 1000 * 60 * 15,
  });
};

/**
 * Search users by term
 */
export const useSearchUsers = (searchTerm: string) => {
  return useQuery({
    queryKey: [QUERY_KEYS.USERS, 'search', searchTerm],
    queryFn: () => searchUsers(searchTerm),
    enabled: searchTerm.length > 0,
    staleTime: 1000 * 60 * 10,
    gcTime: 1000 * 60 * 15,
  });
};
