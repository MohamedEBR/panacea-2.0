import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

export default function PaymentCancel() {
  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', px: 2, py: 6, textAlign: 'center' }}>
      <Typography variant="h4" fontWeight={700} gutterBottom color="error.main">Payment canceled</Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
        No worries. You can revisit checkout anytime from your dashboard.
      </Typography>
      <Button component={Link} to="/dashboard" variant="contained">Back to dashboard</Button>
    </Box>
  );
}
