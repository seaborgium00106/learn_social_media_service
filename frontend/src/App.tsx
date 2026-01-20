/**
 * Main App Component with Routing
 */

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ThemeProvider, CssBaseline } from '@mui/material';
import theme from './styles/theme';
import './styles/globals.css';

// Pages
import HomePage from './pages/HomePage';
import FriendsListPage from './pages/FriendsListPage';
import UserProfilePage from './pages/UserProfilePage';
import TimelineUserPage from './pages/TimelineUserPage';
import SearchUsersPage from './pages/SearchUsersPage';
import NotFoundPage from './pages/NotFoundPage';

// Create React Query client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 1000 * 60 * 5, // 5 minutes
    },
  },
});

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Router>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/friends" element={<FriendsListPage />} />
            <Route path="/profile/:userId" element={<UserProfilePage />} />
            <Route path="/user/:userId" element={<TimelineUserPage />} />
            <Route path="/search" element={<SearchUsersPage />} />
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </Router>
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;
