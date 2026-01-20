/**
 * Friend Card Component
 */

import React, { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  Avatar,
} from '@mui/material';
import { Person as PersonIcon } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import type { FriendshipResponse } from '../../types';
import ConfirmDialog from '../Common/ConfirmDialog';

interface FriendCardProps {
  friendship: FriendshipResponse;
  onRemove?: (friendId: number) => void;
}

const FriendCard: React.FC<FriendCardProps> = ({ friendship, onRemove }) => {
  const navigate = useNavigate();
  const [confirmOpen, setConfirmOpen] = useState(false);

  const handleRemove = () => {
    if (onRemove) {
      onRemove(friendship.friendId);
    }
    setConfirmOpen(false);
  };

  return (
    <>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Box display="flex" alignItems="center" gap={2}>
            <Avatar>
              <PersonIcon />
            </Avatar>
            <Box flex={1}>
              <Typography variant="h6">{friendship.friendUsername}</Typography>
              <Typography variant="caption" color="text.secondary">
                Friends since {new Date(friendship.createdAt).toLocaleDateString()}
              </Typography>
            </Box>
            <Box display="flex" gap={1}>
              <Button
                variant="outlined"
                size="small"
                onClick={() => navigate(`/profile/${friendship.friendId}`)}
              >
                View Profile
              </Button>
              {onRemove && (
                <Button
                  variant="outlined"
                  color="error"
                  size="small"
                  onClick={() => setConfirmOpen(true)}
                >
                  Remove
                </Button>
              )}
            </Box>
          </Box>
        </CardContent>
      </Card>

      <ConfirmDialog
        open={confirmOpen}
        title="Remove Friend"
        message={`Are you sure you want to remove ${friendship.friendUsername} from your friends?`}
        confirmText="Remove"
        confirmColor="error"
        onConfirm={handleRemove}
        onCancel={() => setConfirmOpen(false)}
      />
    </>
  );
};

export default FriendCard;
