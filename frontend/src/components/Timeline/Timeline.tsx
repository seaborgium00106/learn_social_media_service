/**
 * Timeline Container Component
 */

import React from 'react';
import { Box, Typography } from '@mui/material';
import TimelinePost from './TimelinePost';
import type { TimelinePost as TimelinePostType } from '../../types';
import { EmptyState, LoadingSpinner, ErrorMessage } from '../Common';

interface TimelineProps {
  posts: TimelinePostType[];
  isLoading?: boolean;
  error?: Error | null;
}

const Timeline: React.FC<TimelineProps> = ({ posts, isLoading, error }) => {
  if (isLoading) {
    return <LoadingSpinner message="Loading timeline..." />;
  }

  if (error) {
    return <ErrorMessage message={error.message} />;
  }

  if (posts.length === 0) {
    return (
      <EmptyState
        title="Your Timeline is Empty"
        message="Add friends to see their posts here!"
      />
    );
  }

  return (
    <Box>
      <Typography variant="h5" gutterBottom>
        Your Timeline
      </Typography>
      {posts.map((post) => (
        <TimelinePost key={post.id} post={post} />
      ))}
    </Box>
  );
};

export default Timeline;
