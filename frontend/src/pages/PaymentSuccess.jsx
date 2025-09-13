import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

export default function PaymentSuccess() {
  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', px: 2, py: 6, textAlign: 'center' }}>
      <Typography variant="h4" fontWeight={700} gutterBottom color="primary">Payment successful</Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
        Thanks! Your enrollment is confirmed. You can manage your account and students from your dashboard.
      </Typography>
      <Button component={Link} to="/dashboard" variant="contained">Go to dashboard</Button>
    </Box>
  );
}
