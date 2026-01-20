/**
 * Friends List Page
 */

import React from 'react';
import { Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useFriends, useRemoveFriend } from '../hooks/useFriends';
import { LoadingSpinner, ErrorMessage } from '../components/Common';
import { MainLayout } from '../components/Layout';
import { FriendsList } from '../components/Friends';
import { useUIStore } from '../stores/uiStore';
import { MESSAGES } from '../utils/constants';

const FriendsListPage: React.FC = () => {
  const navigate = useNavigate();
  const { currentUserId } = useAuthStore();
  const { addNotification } = useUIStore();
  const { data: friendships = [], isLoading, error } = useFriends(currentUserId);
  const removeFriendMutation = useRemoveFriend();

  const handleRemoveFriend = async (friendId: number) => {
    if (!currentUserId) return;

    try {
      await removeFriendMutation.mutateAsync({ userId: currentUserId, friendId });
      addNotification(MESSAGES.FRIEND_REMOVED, 'success');
    } catch {
      addNotification(MESSAGES.ERROR_GENERIC, 'error');
    }
  };

  if (!currentUserId) {
    return (
      <MainLayout>
        <ErrorMessage message="Please log in to view your friends" />
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <Box mb={3} display="flex" justifyContent="space-between" alignItems="center">
        <h1>My Friends</h1>
        <Button
          variant="contained"
          onClick={() => navigate('/search')}
        >
          Add Friends
        </Button>
      </Box>

      {isLoading ? (
        <LoadingSpinner />
      ) : error ? (
        <ErrorMessage message={error.message} />
      ) : (
        <FriendsList
          friendships={friendships}
          onRemoveFriend={handleRemoveFriend}
        />
      )}
    </MainLayout>
  );
};

export default FriendsListPage;
