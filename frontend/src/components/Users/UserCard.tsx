/**
 * User Card Component
 */

import React from 'react';
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
import type { UserResponse } from '../../types';

interface UserCardProps {
  user: UserResponse;
  isFriend?: boolean;
  onAddFriend?: (userId: number) => void;
  currentUserId?: number | null;
}

const UserCard: React.FC<UserCardProps> = ({
  user,
  isFriend = false,
  onAddFriend,
  currentUserId,
}) => {
  const navigate = useNavigate();
  const isSelf = currentUserId === user.id;

  return (
    <Card sx={{ mb: 2 }}>
      <CardContent>
        <Box display="flex" alignItems="center" gap={2}>
          <Avatar>
            <PersonIcon />
          </Avatar>
          <Box flex={1}>
            <Typography variant="h6">{user.username}</Typography>
            <Typography variant="body2" color="text.secondary">
              {user.email}
            </Typography>
          </Box>
          <Box display="flex" gap={1}>
            <Button
              variant="outlined"
              size="small"
              onClick={() => navigate(`/profile/${user.id}`)}
            >
              View Profile
            </Button>
            {!isSelf && (
              <>
                {isFriend ? (
                  <Button variant="outlined" size="small" disabled>
                    Already Friends
                  </Button>
                ) : (
                  onAddFriend && (
                    <Button
                      variant="contained"
                      size="small"
                      onClick={() => onAddFriend(user.id)}
                    >
                      Add Friend
                    </Button>
                  )
                )}
              </>
            )}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
};

export default UserCard;
