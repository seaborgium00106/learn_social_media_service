/**
 * Main Entry Point
 */

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

// Set default user ID for development (can be changed in browser console)
// localStorage.setItem('auth-storage', JSON.stringify({ state: { currentUserId: 1, isAuthenticated: true }, version: 0 }));

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
