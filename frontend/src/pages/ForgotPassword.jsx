import React, { useState } from 'react';
import { Box, Paper, TextField, Button, Typography } from '@mui/material';
import { motion } from 'framer-motion';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AuthService from '../lib/authService';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    if (!email) { toast.error('Please enter your email'); return; }
    setLoading(true);
    const res = await AuthService.forgotPassword(email);
    setLoading(false);
    if (res.success) {
      toast.success('If the email exists, a reset link has been sent.');
    } else {
      toast.error(res.message || 'Failed to send reset email');
    }
  };

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '70vh', px: 2 }}>
      <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.35 }} style={{ width: '100%', maxWidth: 480 }}>
        <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
          <Typography variant="h5" fontWeight={700} sx={{ mb: 1 }}>Forgot password</Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>Enter your email and we’ll send you a reset link</Typography>
          <Box component="form" onSubmit={submit}>
            <TextField type="email" label="Email" fullWidth sx={{ mb: 2 }} value={email} onChange={(e)=>setEmail(e.target.value)} required />
            <Button type="submit" variant="contained" disabled={loading} fullWidth>
              {loading ? 'Sending…' : 'Send reset link'}
            </Button>
          </Box>
        </Paper>
      </motion.div>
      <ToastContainer position="bottom-left" />
    </Box>
  );
};

export default ForgotPassword;
