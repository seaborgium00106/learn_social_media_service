/**
 * Date formatting utilities using date-fns
 */

import { format, formatDistanceToNow, parseISO } from 'date-fns';

/**
 * Format a date string to a readable format
 */
export const formatDate = (dateString: string): string => {
  try {
    const date = parseISO(dateString);
    return format(date, 'MMM dd, yyyy');
  } catch (error) {
    return dateString;
  }
};

/**
 * Format a date string to include time
 */
export const formatDateTime = (dateString: string): string => {
  try {
    const date = parseISO(dateString);
    return format(date, 'MMM dd, yyyy hh:mm a');
  } catch (error) {
    return dateString;
  }
};

/**
 * Format a date string to relative time (e.g., "2 hours ago")
 */
export const formatRelativeTime = (dateString: string): string => {
  try {
    const date = parseISO(dateString);
    return formatDistanceToNow(date, { addSuffix: true });
  } catch (error) {
    return dateString;
  }
};

/**
 * Format date for API requests (YYYY-MM-DD)
 */
export const formatDateForApi = (date: Date): string => {
  return format(date, 'yyyy-MM-dd');
};
