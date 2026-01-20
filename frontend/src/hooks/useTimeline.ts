/**
 * Custom hook for timeline data fetching using React Query
 */

import { useQuery, useQueryClient } from '@tanstack/react-query';
import { getTimeline, getTimelineFiltered, getTimelineCount } from '../services/timelineService';
import { QUERY_KEYS } from '../utils/constants';
import { APP_CONFIG } from '../config/apiConfig';
import type { TimelineFilterParams } from '../types';

/**
 * Fetch user timeline
 */
export const useTimeline = (userId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.TIMELINE, userId],
    queryFn: () => (userId ? getTimeline(userId) : Promise.resolve([])),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5, // 5 minutes
    gcTime: 1000 * 60 * 10, // 10 minutes (formerly cacheTime)
  });
};

/**
 * Fetch filtered timeline with pagination
 */
export const useTimelineFiltered = (
  userId: number | null,
  page: number = 0,
  size: number = APP_CONFIG.defaultPageSize,
  params?: Omit<TimelineFilterParams, 'page' | 'size'>
) => {
  return useQuery({
    queryKey: [QUERY_KEYS.TIMELINE, userId, page, size, params],
    queryFn: () =>
      userId
        ? getTimelineFiltered(userId, { ...params, page, size })
        : Promise.resolve({ content: [], totalElements: 0, totalPages: 0, size: 0, number: 0, first: true, last: true }),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 10,
  });
};

/**
 * Fetch timeline post count
 */
export const useTimelineCount = (userId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.TIMELINE_COUNT, userId],
    queryFn: () => (userId ? getTimelineCount(userId) : Promise.resolve(0)),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 10,
  });
};

/**
 * Utility hook to invalidate timeline queries
 */
export const useInvalidateTimeline = () => {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries({
      queryKey: [QUERY_KEYS.TIMELINE],
    });
    queryClient.invalidateQueries({
      queryKey: [QUERY_KEYS.TIMELINE_COUNT],
    });
  };
};
