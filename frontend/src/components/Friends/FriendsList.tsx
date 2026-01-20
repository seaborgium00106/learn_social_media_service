/**
 * Friends List Component
 */

import React from 'react';
import { Box, Typography } from '@mui/material';
import FriendCard from './FriendCard';
import type { FriendshipResponse } from '../../types';
import { EmptyState } from '../Common';
import { MESSAGES } from '../../utils/constants';

interface FriendsListProps {
  friendships: FriendshipResponse[];
  onRemoveFriend?: (friendId: number) => void;
}

const FriendsList: React.FC<FriendsListProps> = ({ friendships, onRemoveFriend }) => {
  if (friendships.length === 0) {
    return (
      <EmptyState
        title="No Friends Yet"
        message={MESSAGES.NO_FRIENDS}
      />
    );
  }

  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        Your Friends ({friendships.length})
      </Typography>
      {friendships.map((friendship) => (
        <FriendCard
          key={friendship.id}
          friendship={friendship}
          onRemove={onRemoveFriend}
        />
      ))}
    </Box>
  );
};

export default FriendsList;
