import React, { useMemo, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Paper, TextField, Button, Typography } from '@mui/material';
import { motion } from 'framer-motion';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AuthService from '../lib/authService';

const useQuery = () => {
  const { search } = useLocation();
  return useMemo(() => new URLSearchParams(search), [search]);
};

const ResetPassword = () => {
  const query = useQuery();
  const token = query.get('token') || '';
  const navigate = useNavigate();
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    if (!token) { toast.error('Reset token missing'); return; }
    if (!password || !confirm) { toast.error('Please fill both fields'); return; }
    if (password !== confirm) { toast.error('Passwords do not match'); return; }
    setLoading(true);
    const res = await AuthService.resetPassword(token, password, confirm);
    setLoading(false);
    if (res.success) {
      toast.success('Password reset successfully');
      setTimeout(()=> navigate('/login'), 1200);
    } else {
      toast.error(res.message || 'Failed to reset password');
    }
  };

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '70vh', px: 2 }}>
      <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.35 }} style={{ width: '100%', maxWidth: 480 }}>
        <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
          <Typography variant="h5" fontWeight={700} sx={{ mb: 1 }}>Reset password</Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>Enter a new password</Typography>
          <Box component="form" onSubmit={submit}>
            <TextField type="password" label="New password" fullWidth sx={{ mb: 2 }} value={password} onChange={(e)=>setPassword(e.target.value)} required />
            <TextField type="password" label="Confirm password" fullWidth value={confirm} onChange={(e)=>setConfirm(e.target.value)} required />
            <Button type="submit" variant="contained" disabled={loading} fullWidth sx={{ mt: 2 }}>
              {loading ? 'Saving…' : 'Save new password'}
            </Button>
          </Box>
        </Paper>
      </motion.div>
      <ToastContainer position="bottom-left" />
    </Box>
  );
};

export default ResetPassword;
