import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import { ProgramBody } from "../components/Programs";
import { CustomBox } from "../components/shared/CustomBox";
import images from "../constants/images";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
import { saveAs } from "file-saver";
import {motion} from 'framer-motion'

const Programs = () => {
  const handleClick = () => {
    let url = images.schedule;
    saveAs(url, "panacea-schedule");
  };
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
          Programs
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
          From beginner to black belt, our karate programs offer a path for
          everyone. Explore self-defense, fitness, and personal growth in a
          supportive environment.
        </Typography>
      </CustomBox>
      </motion.div>
      <ProgramBody />
      <motion.div
        initial={{ opacity: 0, y: 100 }}
        whileInView={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, ease: 'easeInOut' }}>
        <CustomBox
          sx={{
            p: "3% 7%",
            my: 10,
            boxShadow: 3,
            display: "flex",
            flexDirection: {
              xs: "column",
              sm: "column",
              md: "column",
              lg: "row",
            },
            justifyContent: "center",
            alignItems: "center",
            textAlign: {
              xs: "center",
              sm: "center",
              md: "center",
              lg: "start",
            },
          }}
        >
          <Box
            component="div"
            sx={{
              width: { xs: "100%", sm: "100%", md: "100%", lg: "65%" },
              mr: { lg: 8 },
              mb: 2,
            }}
          >
            <Typography
              variant="h6"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                color: "#fff",
                my: 3,
              }}
            >
              Class Schedule
            </Typography>
            <Typography
              variant="h4"
              sx={{
                my: 3,
                width: "100%",
              }}
            >
              Kickstart your fitness journey in high gear! Sample a variety of
              martial arts this week and discover the perfect fit for your
              goals.
            </Typography>
            <Button
              variant="contained"
              size="medium"
              onClick={handleClick}
              sx={{
                my: 3,
                width: { sm: "150px", md: "200px" },
                height: { xs: "40px", sm: "50px", md: "60px" },
                borderRadius: 0,
                backgroundColor: "#fff",
                color: "#9d4f4b",
                ":hover": {
                  backgroundColor: "#9d4f4b",
                  color: "#fff",
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
                Schedule
              </Typography>
            </Button>
          </Box>
          <Box
            component="img"
            sx={{
              width: { xs: "30%", sm: "30%", md: "20%", lg: "20%" },
              height: "auto",
              boxShadow: 5,
              borderRadius: "50%",
              mb: 2,
            }}
            alt="logo"
            src={images.logo_white}
          />
        </CustomBox>
        </motion.div>
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
    </Box>
  );
};

export default Programs;
