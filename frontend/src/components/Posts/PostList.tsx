/**
 * Post List Component
 */

import React from 'react';
import { Box } from '@mui/material';
import PostCard from './PostCard';
import type { Post } from '../../types';
import { EmptyState } from '../Common';
import { MESSAGES } from '../../utils/constants';

interface PostListProps {
  posts: Post[];
  onDeletePost?: (postId: number) => void;
  userName?: string;
}

const PostList: React.FC<PostListProps> = ({ posts, onDeletePost, userName }) => {
  if (posts.length === 0) {
    return (
      <EmptyState
        title="No Posts Yet"
        message={MESSAGES.NO_POSTS}
      />
    );
  }

  return (
    <Box>
      {posts.map((post) => (
        <PostCard key={post.id} post={post} onDelete={onDeletePost} userName={userName} />
      ))}
    </Box>
  );
};

export default PostList;
