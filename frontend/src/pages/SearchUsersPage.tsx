/**
 * Search Users Page
 */

import React, { useState } from 'react';
import { Box, TextField, Button, Typography } from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';
import { useAllUsers } from '../hooks/useUsers';
import { useAddFriend, useFriends } from '../hooks/useFriends';
import { LoadingSpinner, ErrorMessage, EmptyState } from '../components/Common';
import { MainLayout } from '../components/Layout';
import { UserCard } from '../components/Users';
import { useAuthStore } from '../stores/authStore';
import { useUIStore } from '../stores/uiStore';
import { MESSAGES } from '../utils/constants';

const SearchUsersPage: React.FC = () => {
  const { currentUserId } = useAuthStore();
  const { addNotification } = useUIStore();
  const { data: allUsers = [], isLoading, error } = useAllUsers();
  const { data: friendships = [] } = useFriends(currentUserId);
  const addFriendMutation = useAddFriend();
  const [searchTerm, setSearchTerm] = useState('');

  const friendIds = new Set(friendships.map((f) => f.friendId));

  const filteredUsers = allUsers.filter((user) => {
    const lowerSearch = searchTerm.toLowerCase();
    return (
      user.id !== currentUserId &&
      (user.username.toLowerCase().includes(lowerSearch) ||
        user.email.toLowerCase().includes(lowerSearch))
    );
  });

  const handleAddFriend = async (friendId: number) => {
    if (!currentUserId) return;

    try {
      await addFriendMutation.mutateAsync({
        userId: currentUserId,
        friendId,
      });
      addNotification(MESSAGES.FRIEND_ADDED, 'success');
    } catch {
      addNotification(MESSAGES.ERROR_GENERIC, 'error');
    }
  };

  return (
    <MainLayout>
      <Box mb={3}>
        <Typography variant="h5" gutterBottom>
          Find and Add Friends
        </Typography>
        <Box display="flex" gap={1}>
          <TextField
            fullWidth
            placeholder="Search by name or email..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            variant="outlined"
          />
          <Button variant="contained" startIcon={<SearchIcon />}>
            Search
          </Button>
        </Box>
      </Box>

      {isLoading ? (
        <LoadingSpinner />
      ) : error ? (
        <ErrorMessage message={error.message} />
      ) : filteredUsers.length === 0 ? (
        <EmptyState
          title={searchTerm ? 'No users found' : 'No users available'}
          message={searchTerm ? `No results for "${searchTerm}"` : 'Start typing to search for users'}
        />
      ) : (
        <Box>
          <Typography variant="body2" color="text.secondary" mb={2}>
            Found {filteredUsers.length} user(s)
          </Typography>
          {filteredUsers.map((user) => (
            <UserCard
              key={user.id}
              user={user}
              isFriend={friendIds.has(user.id)}
              onAddFriend={handleAddFriend}
              currentUserId={currentUserId}
            />
          ))}
        </Box>
      )}
    </MainLayout>
  );
};

export default SearchUsersPage;
