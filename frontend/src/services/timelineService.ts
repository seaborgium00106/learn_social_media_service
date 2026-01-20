/**
 * Timeline API service
 */

import api from './api';
import { API_ENDPOINTS } from '../config/apiConfig';
import type { TimelinePostResponse, TimelineFilterParams, PaginatedResponse } from '../types';

/**
 * Get user's timeline (all posts from friends)
 */
export const getTimeline = async (userId: number): Promise<TimelinePostResponse[]> => {
  const response = await api.get<TimelinePostResponse[]>(API_ENDPOINTS.timeline(userId));
  return response.data;
};

/**
 * Get filtered timeline with pagination and date range
 */
export const getTimelineFiltered = async (
  userId: number,
  params: TimelineFilterParams
): Promise<PaginatedResponse<TimelinePostResponse>> => {
  const response = await api.get<PaginatedResponse<TimelinePostResponse>>(
    API_ENDPOINTS.timelineFiltered(userId),
    { params }
  );
  return response.data;
};

/**
 * Get count of timeline posts
 */
export const getTimelineCount = async (userId: number): Promise<number> => {
  const response = await api.get<{ count: number }>(API_ENDPOINTS.timelineCount(userId));
  return response.data.count;
};
