/**
 * User store using Zustand
 * Manages current user profile information
 */

import { create } from 'zustand';
import type { User } from '../types';

interface UserState {
  currentUser: User | null;
  setCurrentUser: (user: User) => void;
  clearCurrentUser: () => void;
}

export const useUserStore = create<UserState>((set) => ({
  currentUser: null,

  setCurrentUser: (user: User) =>
    set({ currentUser: user }),

  clearCurrentUser: () =>
    set({ currentUser: null }),
}));
