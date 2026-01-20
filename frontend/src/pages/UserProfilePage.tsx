/**
 * User Profile Page - Shows user info, add friend button, post creation, and all user's posts
 * URL: /profile/:userId
 */

import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Card, CardContent, Avatar, Typography } from '@mui/material';
import { Person as PersonIcon } from '@mui/icons-material';
import { useUser } from '../hooks/useUsers';
import { usePostsByUser } from '../hooks/usePosts';
import { LoadingSpinner, ErrorMessage } from '../components/Common';
import { MainLayout } from '../components/Layout';
import { PostList, PostForm } from '../components/Posts';
import { useUIStore } from '../stores/uiStore';

const UserProfilePage: React.FC = () => {
  const navigate = useNavigate();
  const { userId } = useParams<{ userId: string }>();
  const { addNotification } = useUIStore();

  const userIdNum = userId ? parseInt(userId, 10) : null;

  const { data: user, isLoading: userLoading, error: userError } = useUser(userIdNum);
  const { data: posts = [], isLoading: postsLoading } = usePostsByUser(userIdNum);

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
            </Box>
          </Box>
        </CardContent>
      </Card>

      {/* Post Creation Form */}
      <PostForm userId={userIdNum} />

      {/* Posts Section */}
      <Box>
        <Typography variant="h5" gutterBottom>
          {user.username}'s Posts
        </Typography>
        {postsLoading ? (
          <LoadingSpinner />
        ) : posts.length > 0 ? (
          <PostList posts={posts} userName={user.username} />
        ) : (
          <Typography variant="body2" color="text.secondary">
            No posts yet
          </Typography>
        )}
      </Box>
    </MainLayout>
  );
};

export default UserProfilePage;
