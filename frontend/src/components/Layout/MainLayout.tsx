/**
 * Main Layout Wrapper Component
 */

import React, { useState } from 'react';
import { Box, Container } from '@mui/material';
import Header from './Header';
import Sidebar from './Sidebar';
import { NotificationSnackbar } from '../Common';

interface MainLayoutProps {
  children: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Header />
      <Sidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <Container
        maxWidth="lg"
        sx={{
          flex: 1,
          py: 3,
        }}
      >
        {children}
      </Container>
      <NotificationSnackbar />
    </Box>
  );
};

export default MainLayout;
