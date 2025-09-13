import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import images from "../../constants/images";
import {Link} from "react-router-dom";
import {motion} from 'framer-motion'

import "./index.scss";
const About = () => {
  return (
    <Box
      component="div"
      sx={{
        flexDirection: {xs : "column",sm : "column",lg :"row"},
        display: "flex",
        alignItems: "center",
        justifyContent: {xs : "center", sm : "center", md : "space-between"},
        height: {lg :"100vh"},
        pb: 5,
        mb: 5,
        px: "7%",
      }}
    >
     

      <Box
        component="div"
        sx={{
          textAlign: {xs : "center", sm : "center", md :"start"},
          flexDirection: "column",
          display: "flex",
          justifyContent: {xs : "center", sm : "center", md :"flex-start"},
          alignItems: {xs : "center", sm : "center", md :"start"},
          width: "100%",
        }}
      >
         <motion.div
      initial={{x: -20}}
      whileInView={{x: 0}}
      transition={{ease : 'easeInOut', duration: 0.5}}
      >
        <Typography
          variant="h6"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            color: "#9d4f4b",
          }}
        >
          About Us
        </Typography>
        <Typography
          variant="h3"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
          }}
        >
          We're not just a karate school
          <br />
          We're a family dedicated to building
          <br />
          <span className="goldenText">Champions</span>
        </Typography>
        <Typography
          variant="subtitle1"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            pt: 2,
            fontSize: 20,
            width: "100%",
          }}
        >
          Welcome to Panacea Karate Academy, where we build champions for life!
          Our passionate black belt instructors, rooted in traditional Shotokan
          principles, provide more than just karate. We empower children to
          develop confidence, discipline, and goal-setting skills. In a
          supportive community, we celebrate every achievement, transforming
          students into well-rounded champions ready to meet any challenge.
        </Typography>
        <Link
        to="/about">
        <Button
          variant="contained"
          size="medium"
          sx={{
            mt: 3,
            width: { sm: "150px", md: "200px" },
            height: { xs: "40px", sm: "50px", md: "60px" },
            borderRadius: 0,
            backgroundColor: "#9d4f4b",
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
            Learn more
          </Typography>
        </Button>
        </Link>
        </motion.div>
      </Box>

      <Box
        sx={{
          width: {xs: "100%", sm: "100%", md :"80%"},
          pl: {xs: 0,sm: 0, md : 5},
          pt: 5,
        }}
      >
        <motion.div
         initial={{x: 20}}
         whileInView={{x: 0}}
         transition={{ease : 'easeInOut', duration: 0.5}}>

        <Box
          component="img"
          sx={{
            width: {xs : "100%",sm : "100%",md :"90%"},
            height: "500px",
            objectFit: "none",
            objectPosition: "center",
            position: "relative",
            borderRadius: " 0% 0% 0% 0% / 0% 0% 0% 0%",
            boxShadow: "20px 20px rgba(0,0,0,.15)",
            transition: "all .4s ease",
            ":hover": {
              borderRadius: " 0% 0% 50% 50% / 0% 0% 5% 5%",
              boxShadow: "10px 10px rgba(0,0,0,.25)",
            },
          }}
          alt="Logo"
          src={images.karimNats2medals}
        />
        </motion.div>

      </Box>
    </Box>
  );
};

export default About;
