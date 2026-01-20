/**
 * Authentication store using Zustand
 * Manages current user authentication state
 */

import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface AuthState {
  currentUserId: number | null;
  isAuthenticated: boolean;
  setCurrentUser: (userId: number) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      currentUserId: null,
      isAuthenticated: false,

      setCurrentUser: (userId: number) =>
        set({ currentUserId: userId, isAuthenticated: true }),

      logout: () =>
        set({ currentUserId: null, isAuthenticated: false }),
    }),
    {
      name: 'auth-storage', // localStorage key
    }
  )
);
