/**
 * Notification Snackbar Component
 */

import React from 'react';
import { Snackbar, Alert } from '@mui/material';
import { useUIStore } from '../../stores/uiStore';

const NotificationSnackbar: React.FC = () => {
  const { notifications, removeNotification } = useUIStore();

  const handleClose = (id: string) => {
    removeNotification(id);
  };

  return (
    <>
      {notifications.map((notification) => (
        <Snackbar
          key={notification.id}
          open={true}
          autoHideDuration={6000}
          onClose={() => handleClose(notification.id)}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        >
          <Alert
            onClose={() => handleClose(notification.id)}
            severity={notification.type}
            sx={{ width: '100%' }}
          >
            {notification.message}
          </Alert>
        </Snackbar>
      ))}
    </>
  );
};

export default NotificationSnackbar;
