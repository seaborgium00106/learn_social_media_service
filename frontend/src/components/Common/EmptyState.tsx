/**
 * Empty State Component
 */

import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Inbox as InboxIcon } from '@mui/icons-material';

interface EmptyStateProps {
  icon?: React.ReactNode;
  title: string;
  message?: string;
  actionLabel?: string;
  onAction?: () => void;
}

const EmptyState: React.FC<EmptyStateProps> = ({
  icon,
  title,
  message,
  actionLabel,
  onAction,
}) => {
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      minHeight="300px"
      textAlign="center"
      p={3}
    >
      {icon || <InboxIcon sx={{ fontSize: 64, color: 'text.disabled', mb: 2 }} />}
      <Typography variant="h6" color="text.primary" gutterBottom>
        {title}
      </Typography>
      {message && (
        <Typography variant="body2" color="text.secondary" mb={2}>
          {message}
        </Typography>
      )}
      {actionLabel && onAction && (
        <Button variant="contained" onClick={onAction}>
          {actionLabel}
        </Button>
      )}
    </Box>
  );
};

export default EmptyState;
