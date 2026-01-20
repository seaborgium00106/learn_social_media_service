/**
 * Timeline Post Component
 */

import React from 'react';
import {
  Card,
  CardContent,
  Typography,
  Box,
  Chip,
} from '@mui/material';
import type { TimelinePost as TimelinePostType } from '../../types';
import { formatRelativeTime } from '../../utils/dateFormatter';

interface TimelinePostProps {
  post: TimelinePostType;
}

const TimelinePost: React.FC<TimelinePostProps> = ({ post }) => {
  return (
    <Card sx={{ mb: 2 }}>
      <CardContent>
        <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={1}>
          <Box>
            <Typography variant="subtitle2" color="primary" fontWeight="bold">
              {post.username}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              {formatRelativeTime(post.createdAt)}
            </Typography>
          </Box>
        </Box>
        <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
          {post.text}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default TimelinePost;
