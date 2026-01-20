/**
 * Custom hook for post data fetching and mutations using React Query
 */

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  getPostsByUser,
  createPost,
  updatePost,
  deletePost,
} from '../services/postService';
import { QUERY_KEYS } from '../utils/constants';
import type { PostRequest } from '../types';

/**
 * Fetch posts by user
 */
export const usePostsByUser = (userId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.POSTS, userId],
    queryFn: () => (userId ? getPostsByUser(userId) : Promise.resolve([])),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 10,
  });
};

/**
 * Mutation to create a new post
 */
export const useCreatePost = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (postData: PostRequest) => createPost(postData),
    onSuccess: () => {
      // Invalidate related queries
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.POSTS],
      });
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.TIMELINE],
      });
    },
  });
};

/**
 * Mutation to update a post
 */
export const useUpdatePost = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ postId, postData }: { postId: number; postData: Partial<PostRequest> }) =>
      updatePost(postId, postData),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.POSTS],
      });
    },
  });
};

/**
 * Mutation to delete a post
 */
export const useDeletePost = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (postId: number) => deletePost(postId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.POSTS],
      });
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.TIMELINE],
      });
    },
  });
};
