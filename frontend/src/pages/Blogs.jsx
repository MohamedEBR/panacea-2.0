import { useEffect, useState } from 'react'
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import {motion} from 'framer-motion'
import Divider from '@mui/material/Divider';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import { CustomBox } from "../components/shared/CustomBox";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
import images from "../constants/images";
import BlogsBox from '../components/Blogs/BlogsBox';
import { useAuth } from '../contexts/AuthContext';
import { CircularProgress } from '@mui/material';
import http from '../lib/http'

const Blogs = () => {
  const [blogs, setBlogs] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const { isAdmin } = useAuth()

  useEffect(() => {
    // Call the Spring Boot API to fetch the blogs
    async function fetchData() {
      try {
        setLoading(true)
        const response = await http.get('/api/v1/blogs')

        // New BlogController returns { success, data: { blogs, ... } }
        const blogs = response.data?.data?.blogs
        setBlogs(Array.isArray(blogs) ? blogs : [])
      } catch (error) {
        console.error('Error fetching blogs:', error)
  setError('Failed to load blogs')
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <CircularProgress />
      </Box>
    )
  }

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
          alignItems: { xs: "center", sm: "center", md: "start", lg: "start" },
          textAlign: { xs: "center", sm: "center", md: "start", lg: "start" },
          py: 5,
          px: "7%",
        }}
      >
         <motion.div
      initial={{opacity: 0, x: -20}}
      whileInView={{opacity: 1, x: 0}}
      transition={{ease:'backInOut', duration:1}}>
        <Typography
          variant="h2"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            color: "#9d4f4b",
          }}
        >
          Blogs
        </Typography>
        </motion.div>

        {/* Admin Create Blog Button */}
        {isAdmin() && (
          <Box sx={{ mt: 3 }}>
            <Link to="/blogs/new" style={{ textDecoration: 'none' }}>
              <Button
                variant="contained"
                sx={{
                  backgroundColor: "#9d4f4b",
                  color: "#fff",
                  '&:hover': {
                    backgroundColor: "#763C39",
                  },
                }}
              >
                Create New Blog
              </Button>
            </Link>
          </Box>
        )}
      </Box>
      
      <motion.div
      initial={{opacity: 0, x: -20}}
      whileInView={{opacity: 1, x: 0}}
      transition={{ease:'backInOut', duration:1}}>
      <CustomBox
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
          alignItems: { xs: "center", sm: "center", md: "start", lg: "start" },
          textAlign: { xs: "center", sm: "center", md: "start", lg: "start" },
          py: 10,
          px: "7%",
        }}
      >
        <Typography
          variant="h4"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
          }}
        >
          Celebrating the unwavering commitment and remarkable achievements of our karate family. Here, we honor the triumphs of our students and instructors, who consistently demonstrate excellence at the international, national, and provincial levels.
        </Typography>
      </CustomBox>
      </motion.div>

    </Box>

      <Box
      component="div"
      sx={{
        flexDirection: "column",
        width: "100%",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        height: "100%",
        textAlign: { xs: "center", sm: "center", md: "center", lg: "center" },
        px: "7%",
        my: 5,
      }}>

        {error && (
          <Typography color="error" sx={{ mb: 3 }}>
            {error}
          </Typography>
        )}

        <List>
          {/* Dynamic blogs from Spring Boot API */}
          {blogs.length > 0 && blogs.map((blog) => {
            // Format the blog date (Spring Boot returns LocalDateTime)
            const formattedDate = new Date(blog.blogDate).toLocaleDateString()
            const createdDate = new Date(blog.createdAt || blog.blogDate).toLocaleDateString()
            
            return (
              <div key={blog.id}>
                <ListItem>
                  <motion.div
                    initial={{ opacity: 0, x: -50 }}
                    whileInView={{ opacity: 1, x: 0 }}
                    transition={{ ease: "backInOut", duration: 1 }}
                  >
                      <Link to={`/blogs/${blog.id}`} style={{ textDecoration: 'none'}}>
                      <Typography
                        variant='h4'
                        sx={{
                          py:{xs:1,sm:2,md:3,lg:4},
                          fontSize: {xs:20,sm: 30},
                          fontFamily: '"Saira Semi Condensed"',
                          color:"#9d4f4b",
                          ":hover" : {
                            color:"#763C39",
                          }
                        }}
                      >
                        {blog.title}
                      </Typography>
                    </Link>

                    {/* Display image if available */}
                    {blog.imageUrl && (
                      <Box
                        component="img"
                        sx={{
                          width: { xs: "100%", sm: "100%", md: "100%", lg: "100%" },
                          mb: 5,
                          boxShadow: "rgba(60, 64, 67, 0.3) 0px 1px 2px 0px, rgba(60, 64, 67, 0.15) 0px 2px 6px 2px",
                          borderRadius: "2%"
                        }}
                        alt="blog"
                        src={blog.imageUrl}
                        onError={(e) => {
                          e.target.style.display = 'none'
                        }}
                      />
                    )}

                    {/* Blog content preview */}
                    <Typography sx={{ mb: 2, color: 'text.secondary' }}>
                      {blog.content.substring(0, 200)}...
                    </Typography>

                    <Typography>
                      {formattedDate} - <span className="text-secondary">posted {createdDate}</span>
                    </Typography>

                    {/* Admin edit/delete buttons */}
                    {isAdmin() && (
                      <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
                          <Link to={`/blogs/${blog.id}/edit`}>
                          <Button size="small" variant="outlined">Edit</Button>
                        </Link>
                        <Button 
                          size="small" 
                          variant="outlined" 
                          color="error"
                          onClick={() => {
                            if (window.confirm('Are you sure you want to delete this blog?')) {
                              // TODO: Implement delete functionality
                              console.log('Delete blog:', blog.id)
                            }
                          }}
                        >
                          Delete
                        </Button>
                      </Box>
                    )}
                  </motion.div>
                </ListItem>
                <Divider variant="middle" component="li" />
              </div>
            )
          })}

          {/* Static achievement blogs - keeping these as they seem to be permanent content */}
          <BlogsBox
            title="Karim Ebraheem, Youth League Bronze medal at Merida, Mexico 2023"
            content="This isn't just a win for Karim Ebraheem, it's a giant leap for Panacea Karate Academy!  Karim's bronze medal at the Merida, Mexico 2023 Youth League marks the first time a Panacea student has taken the top spot at an international tournament. This incredible achievement paves the way for future karatekas at our academy, proving that dedication and hard work can lead to success on the world stage. Karim's rise to 26th in the world rankings is an inspiration to us all. Let's use his triumph as a springboard to push our own limits and strive for excellence in everything we do!"
            date="23/09/2023"
            image={images.karimMexico} 
          />
          <BlogsBox
            title="Karim Ebraheem, 2 National Gold Medals Senior and Junior, Laval, Quebec 2023"
            content="Karim didn't just claim one, but two National Gold Medals, dominating both the Senior and Junior divisions. This phenomenal feat marks a monumental achievement for our academy – not only is it our first ever Senior National Gold Medal, but it also secured Karim's well-deserved spot at the 2023 World Championships! This wasn't just a victory, it was an extreme endeavor conquered with unwavering focus and relentless determination. Karim is a true inspiration, proving that dedication and hard work can shatter expectations and propel you to the world stage. Let this double gold medal be a burning torch, igniting a fire within us all to reach for greatness and rewrite the meaning of possible!"
            date="11/07/2023"
            image={images.karimNats2medals} 
          />
          <BlogsBox
            title="Karim Ebraheem, National Junior Gold Medal, New Foundland, 2022"
            content="This National Junior Gold Medal win by Karim in Newfoundland, 2022, is a historic moment for our club. It's the first time a Panacea athlete has claimed gold at a national competition, shattering records and setting a new standard for excellence. This achievement is a testament to the dedication of both the athlete and the entire Panacea family. It proves that with unwavering focus and relentless training, dreams can become reality. Let Karim's gold medal be a beacon of inspiration for all of us. Let's train harder, push our boundaries, and strive to be the next national champion!"
            date="09/07/2022"
            image={images.karim2022} 
          />
        </List>
        </Box>

        <motion.div
        initial={{ opacity: 0, y: 100 }}
        whileInView={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, ease: 'easeInOut' }}>
   <Box
          sx={{
            width: "100%",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flexDirection: "column",
            p: "3% 7%",
            mb: 4,
            mt:10,
          }}
        >
          <Typography
            variant="h2"
            sx={{
              fontFamily: '"Saira Semi Condensed"',
              fontWeight: 500,
              mb: 3,
              textAlign: "center",
            }}
          >
            Start your journey today!
          </Typography>
          <Link to="/contact">
            <Button
              variant="contained"
              size="medium"
              sx={{
                my: 3,
                width: { sm: "150px", md: "200px" },
                height: { xs: "40px", sm: "50px", md: "60px" },
                borderRadius: 0,
                backgroundColor: "#9d4f4b",
                color: "#fff",
                ":hover": {
                  backgroundColor: "#fff",
                  color: "#9d4f4b",
                },
              }}
            >
              <Typography
                variant="h6"
                sx={{
                  fontFamily: '"Saira Semi Condensed"',
                  fontWeight: 500,
                }}
              >
                Join Now
              </Typography>
            </Button>
          </Link>
        </Box>
        </motion.div>
    </>
  )
}

export default Blogs
