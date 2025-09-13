import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { CircularProgress } from '@mui/material';
import { useAuth } from '../contexts/AuthContext';
import http from '../lib/http'

const BlogId = () => {
  const { id: blogId } = useParams()
  const [blog, setBlog] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const { isAdmin } = useAuth()

  // Fetch the single blog from Spring Boot API
  useEffect(() => {
    async function fetchData() {
      try {
        setLoading(true)
  const response = await http.get(`/api/v1/blogs/${blogId}`)
        setBlog(response.data)
      } catch (error) {
        console.error('Error fetching blog:', error)
        setError('Failed to load blog')
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [blogId])

  // Delete the blog and redirect the user to the blogs page
  const deleteBlog = async () => {
    if (!window.confirm('Are you sure you want to delete this blog?')) return
    
    try {
  await http.delete(`/api/v1/blogs/${blogId}`)
      navigate('/blogs')
    } catch (error) {
      console.error('Error deleting blog:', error)
      alert('Failed to delete blog')
    }
  }

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <CircularProgress />
      </Box>
    )
  }

  if (error) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <Typography color="error">{error}</Typography>
      </Box>
    )
  }

  if (!blog) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <Typography>Blog not found</Typography>
      </Box>
    )
  }

  const formattedDate = new Date(blog.blogDate).toLocaleDateString()
  const createdDate = new Date(blog.createdAt || blog.blogDate).toLocaleDateString()

  return (
    <>
      <Box
        component="div"
        sx={{
          width: "100%",
          height: "fit-content",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          py: "3%",
        }}
      >
        <Box
          component="div"
          sx={{
            width: "100%",
            display: "flex",
            flexDirection: "column",
            justifyContent: {
              xs: "center",
              sm: "center",
              md: "start",
              lg: "start",
            },
            alignItems: { xs: "center", sm: "center", md: "center", lg: "center" },
            textAlign: { xs: "center", sm: "center", md: "center", lg: "center" },
            py: 5,
            px: "7%",
          }}
        >
          <Typography 
            variant='h3'
            sx={{
              fontFamily: '"Saira Semi Condensed"',
              py:{xs:1,sm:2,md:3,lg:4},
              fontSize: {xs:20,sm: 30, md: 40, lg: 50},
              color: "#9d4f4b",
            }}
          >
            {blog.title}
          </Typography>

          <Typography className="text-secondary" sx={{ mb: 4 }}>
            posted {createdDate}
          </Typography>

          {/* Display blog image if available */}
          {blog.imageUrl && (
            <Box
              component="img"
              sx={{
                width: { xs: "100%", sm: "100%", md: "100%", lg: "80%" },
                maxHeight: "1000px",
                borderRadius: "2%",
                boxShadow: "rgba(60, 64, 67, 0.3) 0px 1px 2px 0px, rgba(60, 64, 67, 0.15) 0px 2px 6px 2px",
                mb: 5,
              }}
              alt="blog"
              src={blog.imageUrl}
              onError={(e) => {
                e.target.style.display = 'none'
              }}
            />
          )}

          <Typography
            variant="subtitle1"
            sx={{
              fontFamily: '"Saira Semi Condensed"',
              py: 3,
              fontSize: {xs:15,sm: 20, md: 25, lg: 30},
              textAlign: 'left',
              whiteSpace: 'pre-wrap', // Preserve line breaks
            }}
          >
            {blog.content}
          </Typography>

          <Typography className="text-secondary" sx={{ mb: 5 }}>
            - {formattedDate}
          </Typography>

          {/* Admin controls */}
          {isAdmin() && (
            <Box sx={{ mb: 5, display: 'flex', gap: 2 }}>
              <Link to={`/blogs/${blogId}/edit`} style={{ textDecoration: 'none' }}>
                <Button variant="contained" color="primary">
                  Edit
                </Button>
              </Link>
              <Button variant="contained" color="error" onClick={deleteBlog}>
                Delete
              </Button>
            </Box>
          )}

          <Link to="/blogs" style={{ textDecoration: 'none' }}>
            <Typography sx={{ color: '#9d4f4b', '&:hover': { color: '#763C39' } }}>
              &#8592; Back to Blogs
            </Typography>
          </Link>
        </Box>
      </Box>
    </>
  )
}

export default BlogId
