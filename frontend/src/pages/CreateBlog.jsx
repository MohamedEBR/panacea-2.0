import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import {
  Box,
  Typography,
  TextField,
  Button,
  Container,
  Paper,
  Alert,
  CircularProgress
} from '@mui/material';
import http from '../lib/http';

const CreateBlog = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    imageUrl: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    if (!formData.title.trim() || !formData.content.trim()) {
      setError('Title and content are required');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const blogData = {
        title: formData.title.trim(),
        content: formData.content.trim(),
        imageUrl: formData.imageUrl.trim() || null,
        published: true // Auto-publish for now
      };

      const response = await http.post('/api/v1/blogs', { data: blogData });
      
      navigate('/blogs');
    } catch (error) {
      console.error('Error creating blog:', error);
      setError(error.response?.data?.message || 'Failed to create blog');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography 
          variant="h4" 
          component="h1" 
          gutterBottom
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            color: '#9d4f4b',
            mb: 3
          }}
        >
          Create New Blog
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit} noValidate>
          <TextField
            fullWidth
            label="Blog Title"
            name="title"
            value={formData.title}
            onChange={handleInputChange}
            required
            disabled={loading}
            sx={{ mb: 3 }}
          />

          <TextField
            fullWidth
            label="Image URL (optional)"
            name="imageUrl"
            value={formData.imageUrl}
            onChange={handleInputChange}
            disabled={loading}
            placeholder="https://example.com/image.jpg"
            sx={{ mb: 3 }}
          />

          <TextField
            fullWidth
            label="Blog Content"
            name="content"
            value={formData.content}
            onChange={handleInputChange}
            required
            disabled={loading}
            multiline
            rows={12}
            placeholder="Write your blog content here..."
            sx={{ mb: 3 }}
          />

          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
            <Button
              type="button"
              variant="outlined"
              onClick={() => navigate('/blogs')}
              disabled={loading}
            >
              Cancel
            </Button>
            
            <Button
              type="submit"
              variant="contained"
              disabled={loading}
              sx={{
                backgroundColor: '#9d4f4b',
                '&:hover': {
                  backgroundColor: '#763C39',
                },
              }}
            >
              {loading ? <CircularProgress size={20} /> : 'Create Blog'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default CreateBlog;
