/**
 * Home Page / Timeline View
 */

import React, { useState } from 'react';
import { Box, Pagination, Stack } from '@mui/material';
import { useAuthStore } from '../stores/authStore';
import { useTimelineFiltered } from '../hooks/useTimeline';
import { LoadingSpinner, ErrorMessage } from '../components/Common';
import { MainLayout } from '../components/Layout';
import { PostForm } from '../components/Posts';
import { Timeline } from '../components/Timeline';
import { APP_CONFIG } from '../config/apiConfig';

const HomePage: React.FC = () => {
  const { currentUserId } = useAuthStore();
  const [currentPage, setCurrentPage] = useState(0);

  const { data, isLoading, error } = useTimelineFiltered(
    currentUserId,
    currentPage,
    APP_CONFIG.defaultPageSize
  );

  const handlePageChange = (_: React.ChangeEvent<unknown>, page: number) => {
    setCurrentPage(page - 1);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  if (!currentUserId) {
    return (
      <MainLayout>
        <Box textAlign="center" py={5}>
          <ErrorMessage message="Please set your user ID in the browser console to get started" />
        </Box>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <PostForm />
      
      {isLoading ? (
        <LoadingSpinner />
      ) : error ? (
        <ErrorMessage message={error.message} />
      ) : (
        <>
          <Timeline
            posts={data?.content || []}
            isLoading={isLoading}
            error={error}
          />

          {data && data.totalPages > 1 && (
            <Stack spacing={2} alignItems="center" mt={4}>
              <Pagination
                count={data.totalPages}
                page={currentPage + 1}
                onChange={handlePageChange}
                color="primary"
              />
            </Stack>
          )}
        </>
      )}
    </MainLayout>
  );
};

export default HomePage;
