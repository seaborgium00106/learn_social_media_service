/**
 * UI store using Zustand
 * Manages UI state like modals, notifications, filters
 */

import { create } from 'zustand';

interface Notification {
  id: string;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
}

interface UIState {
  // Modal states
  isPostModalOpen: boolean;
  isUserSearchModalOpen: boolean;
  
  // Notifications
  notifications: Notification[];
  
  // Timeline filters
  timelineFromDate: string | null;
  timelineToDate: string | null;
  
  // Actions
  openPostModal: () => void;
  closePostModal: () => void;
  openUserSearchModal: () => void;
  closeUserSearchModal: () => void;
  
  addNotification: (message: string, type: Notification['type']) => void;
  removeNotification: (id: string) => void;
  
  setTimelineFilter: (fromDate: string | null, toDate: string | null) => void;
  clearTimelineFilter: () => void;
}

export const useUIStore = create<UIState>((set) => ({
  // Initial states
  isPostModalOpen: false,
  isUserSearchModalOpen: false,
  notifications: [],
  timelineFromDate: null,
  timelineToDate: null,

  // Modal actions
  openPostModal: () => set({ isPostModalOpen: true }),
  closePostModal: () => set({ isPostModalOpen: false }),
  openUserSearchModal: () => set({ isUserSearchModalOpen: true }),
  closeUserSearchModal: () => set({ isUserSearchModalOpen: false }),

  // Notification actions
  addNotification: (message, type) =>
    set((state) => ({
      notifications: [
        ...state.notifications,
        { id: Date.now().toString(), message, type },
      ],
    })),

  removeNotification: (id) =>
    set((state) => ({
      notifications: state.notifications.filter((n) => n.id !== id),
    })),

  // Timeline filter actions
  setTimelineFilter: (fromDate, toDate) =>
    set({ timelineFromDate: fromDate, timelineToDate: toDate }),

  clearTimelineFilter: () =>
    set({ timelineFromDate: null, timelineToDate: null }),
}));
