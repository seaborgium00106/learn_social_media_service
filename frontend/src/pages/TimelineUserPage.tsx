/**
 * Timeline User Page - Shows timeline of user's posts and friends' posts
 * URL: /user/:userId
 */

import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Card, CardContent, Avatar, Typography, Pagination } from '@mui/material';
import { Person as PersonIcon } from '@mui/icons-material';
import { useUser } from '../hooks/useUsers';
import { useTimelineFiltered } from '../hooks/useTimeline';
import { LoadingSpinner, ErrorMessage } from '../components/Common';
import { MainLayout } from '../components/Layout';
import { TimelinePost } from '../components/Timeline';
import { APP_CONFIG } from '../config/apiConfig';

const TimelineUserPage: React.FC = () => {
  const navigate = useNavigate();
  const { userId } = useParams<{ userId: string }>();
  const [page, setPage] = useState(0);

  const userIdNum = userId ? parseInt(userId, 10) : null;

  const { data: user, isLoading: userLoading, error: userError } = useUser(userIdNum);
  const {
    data: timelineData,
    isLoading: timelineLoading,
    error: timelineError,
  } = useTimelineFiltered(userIdNum, page, APP_CONFIG.defaultPageSize);

  const handlePageChange = (_event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1);
    window.scrollTo(0, 0);
  };

  if (userLoading) {
    return (
      <MainLayout>
        <LoadingSpinner />
      </MainLayout>
    );
  }

  if (userError || !user) {
    return (
      <MainLayout>
        <ErrorMessage message="User not found" />
      </MainLayout>
    );
  }

  if (!userIdNum) {
    return (
      <MainLayout>
        <ErrorMessage message="Invalid user ID" />
      </MainLayout>
    );
  }

  if (timelineError) {
    return (
      <MainLayout>
        <ErrorMessage message="Failed to load timeline" />
      </MainLayout>
    );
  }

  const posts = timelineData?.content || [];
  const totalPages = timelineData?.totalPages || 0;

  return (
    <MainLayout>
      <Box mb={3}>
        <Button variant="outlined" onClick={() => navigate(-1)}>
          ← Back
        </Button>
      </Box>

      {/* User Info Card */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box display="flex" alignItems="center" gap={3}>
            <Avatar sx={{ width: 80, height: 80 }}>
              <PersonIcon />
            </Avatar>
            <Box flex={1}>
              <Typography variant="h4">{user.username}</Typography>
              <Typography variant="body2" color="text.secondary">
                {user.email}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                Timeline
              </Typography>
            </Box>
          </Box>
        </CardContent>
      </Card>

      {/* Timeline Posts Section */}
      <Box>
        <Typography variant="h5" gutterBottom>
          Timeline
        </Typography>
        {timelineLoading ? (
          <LoadingSpinner />
        ) : posts.length > 0 ? (
          <>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mb: 3 }}>
              {posts.map((post) => (
                <TimelinePost key={post.id} post={post} />
              ))}
            </Box>

            {/* Pagination */}
            {totalPages > 1 && (
              <Box display="flex" justifyContent="center" mt={3}>
                <Pagination
                  count={totalPages}
                  page={page + 1}
                  onChange={handlePageChange}
                  color="primary"
                />
              </Box>
            )}
          </>
        ) : (
          <Typography variant="body2" color="text.secondary">
            No posts in timeline yet
          </Typography>
        )}
      </Box>
    </MainLayout>
  );
};

export default TimelineUserPage;
