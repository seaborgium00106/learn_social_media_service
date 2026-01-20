/**
 * Header Component
 */

import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../stores/authStore';
import { APP_CONFIG } from '../../config/apiConfig';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <AppBar position="sticky">
      <Container maxWidth="lg">
        <Toolbar disableGutters>
          <Typography
            variant="h6"
            sx={{
              mr: 'auto',
              fontWeight: 'bold',
              cursor: 'pointer',
            }}
            onClick={() => navigate('/')}
          >
            {APP_CONFIG.appName}
          </Typography>

          <Box sx={{ display: 'flex', gap: 1 }}>
            {isAuthenticated ? (
              <>
                <Button
                  color="inherit"
                  onClick={() => navigate('/friends')}
                >
                  Friends
                </Button>
                <Button
                  color="inherit"
                  onClick={() => navigate('/search')}
                >
                  Search
                </Button>
                <Button
                  color="inherit"
                  onClick={handleLogout}
                >
                  Logout
                </Button>
              </>
            ) : (
              <Typography variant="body2" sx={{ py: 1 }}>
                Please set your User ID in localStorage to get started
              </Typography>
            )}
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Header;
