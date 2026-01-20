/**
 * 404 Not Found Page
 */

import React from 'react';
import { Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { MainLayout } from '../components/Layout';
import { EmptyState } from '../components/Common';

const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <MainLayout>
      <Box textAlign="center" py={10}>
        <EmptyState
          title="404 - Page Not Found"
          message="Sorry, the page you're looking for doesn't exist."
          actionLabel="Go Home"
          onAction={() => navigate('/')}
        />
      </Box>
    </MainLayout>
  );
};

export default NotFoundPage;
