import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import {
  Box,
  Typography,
  TextField,
  Button,
  Container,
  Paper,
  Alert,
  CircularProgress,
  FormControlLabel,
  Switch
} from '@mui/material';
import http from '../lib/http';

const EditBlog = () => {
  const navigate = useNavigate();
  const { id: blogId } = useParams();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    imageUrl: '',
    published: true
  });
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  // Fetch blog data on component mount
  useEffect(() => {
    const fetchBlog = async () => {
      try {
        const response = await http.get(`/api/v1/blogs/${blogId}`);
        const blog = response.data;
        
        setFormData({
          title: blog.title || '',
          content: blog.content || '',
          imageUrl: blog.imageUrl || '',
          published: blog.published !== undefined ? blog.published : true
        });
      } catch (error) {
        console.error('Error fetching blog:', error);
        setError('Failed to load blog');
      } finally {
        setLoading(false);
      }
    };

    fetchBlog();
  }, [blogId]);

  const handleInputChange = (event) => {
    const { name, value, type, checked } = event.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    if (!formData.title.trim() || !formData.content.trim()) {
      setError('Title and content are required');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      const blogData = {
        title: formData.title.trim(),
        content: formData.content.trim(),
        imageUrl: formData.imageUrl.trim() || null,
        published: formData.published
      };

      await http.put(`/api/v1/blogs/${blogId}`, { data: blogData });
      navigate('/blogs');
    } catch (error) {
      console.error('Error updating blog:', error);
      setError(error.response?.data?.message || 'Failed to update blog');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <CircularProgress />
      </Box>
    );
  }

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
          Edit Blog
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
            disabled={submitting}
            sx={{ mb: 3 }}
          />

          <TextField
            fullWidth
            label="Image URL (optional)"
            name="imageUrl"
            value={formData.imageUrl}
            onChange={handleInputChange}
            disabled={submitting}
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
            disabled={submitting}
            multiline
            rows={12}
            placeholder="Write your blog content here..."
            sx={{ mb: 3 }}
          />

          <FormControlLabel
            control={
              <Switch
                checked={formData.published}
                onChange={handleInputChange}
                name="published"
                disabled={submitting}
              />
            }
            label="Published"
            sx={{ mb: 3 }}
          />

          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
            <Button
              type="button"
              variant="outlined"
              onClick={() => navigate('/blogs')}
              disabled={submitting}
            >
              Cancel
            </Button>
            
            <Button
              type="submit"
              variant="contained"
              disabled={submitting}
              sx={{
                backgroundColor: '#9d4f4b',
                '&:hover': {
                  backgroundColor: '#763C39',
                },
              }}
            >
              {submitting ? <CircularProgress size={20} /> : 'Update Blog'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default EditBlog;
