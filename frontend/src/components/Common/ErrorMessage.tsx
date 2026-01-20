/**
 * Error Message Component
 */

import React from 'react';
import { Alert, AlertTitle, Box } from '@mui/material';

interface ErrorMessageProps {
  message?: string;
  title?: string;
  onRetry?: () => void;
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({
  message = 'An error occurred. Please try again.',
  title = 'Error',
  onRetry,
}) => {
  return (
    <Box my={2}>
      <Alert severity="error" onClose={onRetry ? undefined : undefined}>
        <AlertTitle>{title}</AlertTitle>
        {message}
      </Alert>
    </Box>
  );
};

export default ErrorMessage;
