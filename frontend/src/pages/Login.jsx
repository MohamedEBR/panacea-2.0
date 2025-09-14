import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  InputAdornment,
  IconButton,
  FormControlLabel,
  Checkbox,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { motion } from "framer-motion";

const Login = () => {
  const navigate = useNavigate();
  const { login, loading } = useAuth();
  
  const [inputValue, setInputValue] = useState({ email: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(true);
  
  const { email, password } = inputValue;
  
  const handleOnChange = (e) => {
    const { name, value } = e.target;
    setInputValue({
      ...inputValue,
      [name]: value,
    });
  };

  const handleError = (err) =>
    toast.error(err, {
      position: "bottom-left",
    });
    
  const handleSuccess = (msg) =>
    toast.success(msg, {
      position: "bottom-left",
    });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!email || !password) {
      handleError("Please fill in all fields");
      return;
    }
    
    try {
      const result = await login(email, password);
      
      if (result.success) {
        handleSuccess("Login successful!");
        setTimeout(() => {
          navigate("/dashboard");
        }, 1000);
      } else {
        handleError(result.message || "Login failed");
      }
    } catch (error) {
      console.error("Login error:", error);
      handleError("Login failed. Please try again.");
    }
    
    // Clear form
    setInputValue({
      email: "",
      password: "",
    });
  };

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '70vh', px: 2 }}>
      <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.35, ease: 'easeOut' }} style={{ width: '100%', maxWidth: 480 }}>
        <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
          <Typography variant="h5" fontWeight={700} sx={{ mb: 1 }}>Welcome back</Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>Sign in to access your dashboard</Typography>
          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              label="Email"
              name="email"
              type="email"
              value={email}
              onChange={handleOnChange}
              fullWidth
              required
              disabled={loading}
              sx={{ mb: 2 }}
            />
            <TextField
              label="Password"
              name="password"
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={handleOnChange}
              fullWidth
              required
              disabled={loading}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={() => setShowPassword(p=>!p)} edge="end">
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                )
              }}
            />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 1 }}>
              <FormControlLabel
                control={<Checkbox checked={remember} onChange={(e)=>setRemember(e.target.checked)} />}
                label="Remember me"
              />
              <Typography variant="body2" color="text.secondary">
                <Link to="/forgot-password">Forgot password?</Link>
              </Typography>
            </Box>
            <Button type="submit" variant="contained" disabled={loading} fullWidth sx={{ mt: 2 }}>
              {loading ? 'Signing in…' : 'Sign in'}
            </Button>
            <Typography variant="body2" sx={{ mt: 2 }}>
              Don’t have an account? <Link to="/signup">Sign up</Link>
            </Typography>
          </Box>
        </Paper>
      </motion.div>
      <ToastContainer position="bottom-left" />
    </Box>
  );
};

export default Login;