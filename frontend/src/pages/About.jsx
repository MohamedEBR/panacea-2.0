import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import { Mission, Instructors, Offer } from "../components/About";
import { CustomBox } from "../components/shared/CustomBox";

import {Link} from 'react-router-dom'
import Button from '@mui/material/Button'
import {motion} from 'framer-motion'
const About = () => {
  return (
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
          About Us
        </Typography>
      </motion.div>
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
          Through martial arts, students cultivate discipline and focus, setting
          them on a path of self-improvement. Our world-class instructors are
          committed to helping them develop new skills and build confidence.
        </Typography>
      </CustomBox>
      </motion.div>

      <Box
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
          "#offer": {
            px: 0,
          },
        }}
      >
         <motion.div
      initial={{opacity: 0, x: -20}}
      whileInView={{opacity: 1, x: 0}}
      transition={{ease:'backInOut', duration:1}}>
        <Mission />

      </motion.div>
      <motion.div
      initial={{opacity: 0, x: -20}}
      whileInView={{opacity: 1, x: 0}}
      transition={{ease:'backInOut', duration:1}}>
        <Instructors />

      </motion.div>
      <motion.div
      initial={{opacity: 0, x: -20}}
      whileInView={{opacity: 1, x: 0}}
      transition={{ease:'backInOut', duration:1}}>
                <Offer />


      </motion.div>
   
      
        <Box
          sx={{
            width: "100%",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flexDirection: "column",
            p: "3% 7%",
            mb: 4,
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
              fontWeight: 500,
              mb: 3,
              textAlign: "center",
            }}
          >
            Start your journey today!
          </Typography>

      </motion.div>
         
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
      </Box>
    </Box>
  );
};

export default About;
