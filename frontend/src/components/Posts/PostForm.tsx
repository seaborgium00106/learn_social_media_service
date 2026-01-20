/**
 * Post Form Component
 */

import React from 'react';
import {
  Card,
  CardContent,
  TextField,
  Button,
  Box,
  Typography,
} from '@mui/material';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { postSchema } from '../../utils/validators';
import { useCreatePost } from '../../hooks/usePosts';
import { useUIStore } from '../../stores/uiStore';
import { MESSAGES, VALIDATION } from '../../utils/constants';

interface PostFormData {
  text: string;
}

interface PostFormProps {
  userId: number;
}

const PostForm: React.FC<PostFormProps> = ({ userId }) => {
  const { addNotification } = useUIStore();
  const createPostMutation = useCreatePost();

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    watch,
  } = useForm<PostFormData>({
    resolver: yupResolver(postSchema),
  });

  const text = watch('text', '');

  const onSubmit = async (data: PostFormData) => {
    if (!userId) {
      addNotification('User ID is required to create a post', 'error');
      return;
    }

    try {
      await createPostMutation.mutateAsync({
        text: data.text,
        userId: userId,
      });
      addNotification(MESSAGES.POST_CREATED, 'success');
      reset();
    } catch (error) {
      addNotification(MESSAGES.ERROR_GENERIC, 'error');
    }
  };

  return (
    <Card sx={{ mb: 3 }}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Create a Post
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField
            {...register('text')}
            multiline
            rows={3}
            fullWidth
            placeholder="What's on your mind?"
            error={!!errors.text}
            helperText={errors.text?.message}
            sx={{ mb: 1 }}
          />
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" color="text.secondary">
              {text.length}/{VALIDATION.POST_MAX_LENGTH}
            </Typography>
            <Button
              type="submit"
              variant="contained"
              disabled={createPostMutation.isPending}
            >
              {createPostMutation.isPending ? 'Posting...' : 'Post'}
            </Button>
          </Box>
        </form>
      </CardContent>
    </Card>
  );
};

export default PostForm;
