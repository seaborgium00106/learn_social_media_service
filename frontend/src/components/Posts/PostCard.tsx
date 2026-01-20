/**
 * Post Card Component
 */

import React, { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Box,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import { MoreVert as MoreVertIcon } from '@mui/icons-material';
import type { Post } from '../../types';
import { formatRelativeTime } from '../../utils/dateFormatter';
import { useAuthStore } from '../../stores/authStore';

interface PostCardProps {
  post: Post;
  onDelete?: (postId: number) => void;
  userName?: string;
}

const PostCard: React.FC<PostCardProps> = ({ post, onDelete, userName }) => {
  const { currentUserId } = useAuthStore();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const isOwner = currentUserId === post.userId;

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleDelete = () => {
    if (onDelete) {
      onDelete(post.id);
    }
    handleMenuClose();
  };

  return (
    <Card sx={{ mb: 2 }}>
      <CardContent>
        <Box display="flex" justifyContent="space-between" alignItems="flex-start">
          <Box flex={1}>
            <Typography variant="subtitle2" color="primary" fontWeight="bold">
              {userName || `User ${post.userId}`}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              {formatRelativeTime(post.createdAt)}
            </Typography>
          </Box>
          {isOwner && onDelete && (
            <>
              <IconButton size="small" onClick={handleMenuOpen}>
                <MoreVertIcon />
              </IconButton>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                <MenuItem onClick={handleDelete} sx={{ color: 'error.main' }}>
                  Delete
                </MenuItem>
              </Menu>
            </>
          )}
        </Box>
        <Typography variant="body1" sx={{ mt: 1, whiteSpace: 'pre-wrap' }}>
          {post.text}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default PostCard;
