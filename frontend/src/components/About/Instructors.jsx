import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import images from "../../constants/images";
import { motion } from "framer-motion";
const Instructors = () => {
  return (
    <Box
      component="div"
      sx={{
        flexDirection: "column",
        display: "flex",
        alignItems: { xs: "center", sm: "center", md: "center", lg: "start" },
        justifyContent: "center",
        height: "100%",
        textAlign: { xs: "center", sm: "center", md: "center", lg: "start" },
      }}
    >
      <Typography
        variant="h6"
        sx={{
          fontFamily: '"Saira Semi Condensed"',
          color: "#9d4f4b",
          width: "200px",

          mb: 3,
        }}
      >
        Our Instructors
      </Typography>
      <motion.div
        initial={{ opacity: 0, x: -50 }}
        whileInView={{ opacity: 1, x: 0 }}
        transition={{ ease: "backInOut", duration: 1 }}
      >
        <Box
          sx={{
            width: "100%",
            display: "flex",
            flexDirection: {
              xs: "column",
              sm: "column",
              md: "column",
              lg: "row",
            },
            justifyContent: "space-between",
            alignItems: {
              xs: "center",
              sm: "center",
              md: "center",
              lg: "start",
            },
            mb: 20,
          }}
        >
          <Box
            component="img"
            sx={{
              width: { xs: "100%", sm: "100%", md: "60%", lg: "50%" },
              height: "500px",
              objectFit: "cover",
              objectPosition: "50% 35%",
              position: "relative",
              mb: { xs: 5, sm: 5, md: 5, lg: 0 },
              borderRadius: " 0% 0% 0% 0% / 0% 0% 0% 0%",
              boxShadow: "20px 20px rgba(0,0,0,.15)",
              mr: 5,
              transition: "all .4s ease",
              ":hover": {
                borderRadius: " 0% 0% 50% 50% / 0% 0% 5% 5%",
                boxShadow: "10px 10px rgba(0,0,0,.25)",
              },
            }}
            alt="Logo"
            src={images.Amr}
          />
          <Box
            component="div"
            sx={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "start",
              alignItems: "start",
              width: { xs: "100%", sm: "100%", md: "100%", lg: "50%" },
            }}
          >
            <Typography
              variant="h3"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                width: "100%",

                mb: 3,
              }}
            >
              Sensei Amr Ibraheem
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
              Sensei Amr Ibraheem believes that learning karate should be a fun and
              rewarding journey. He fosters a positive and engaging environment
              where students are encouraged to explore their potential and
              celebrate their progress. His passion lies in helping individuals
              of all ages discover the path to personal growth and achievement
              through the discipline of karate.
            </Typography>
          </Box>
        </Box>
      </motion.div>
      <motion.div
        initial={{ opacity: 0, x: -50 }}
        whileInView={{ opacity: 1, x: 0 }}
        transition={{ ease: "backInOut", duration: 1 }}
      >
        <Box
          sx={{
            width: "100%",
            display: "flex",
            flexDirection: {
              xs: "column",
              sm: "column",
              md: "column",
              lg: "row",
            }, // Only change here (md: "row")
            justifyContent: "space-between",
            alignItems: {
              xs: "center",
              sm: "center",
              md: "center",
              lg: "flex-start",
            }, // Minor adjustment (md: "flex-start")
            mb: 20,
          }}
        >
          <Box
            component="img"
            sx={{
              width: { xs: "100%", sm: "100%", md: "60%", lg: "50%" },
              height: "500px",
              objectFit: "cover",
              objectPosition: "50% 35%",
              position: "relative",
              borderRadius: "0% 0% 0% 0% / 0% 0% 0% 0%",
              boxShadow: "20px 20px rgba(0,0,0,.15)",
              mb: { xs: 5, sm: 5, md: 5, lg: 0 },
              mr: 5,
              transition: "all .4s ease",
              ":hover": {
                borderRadius: "0% 0% 50% 50% / 0% 0% 5% 5%",
                boxShadow: "10px 10px rgba(0,0,0,.25)",
              },
            }}
            alt="Logo"
            src={images.Karim}
          />
          <Box
            component="div"
            sx={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "start",
              alignItems: "start",
              width: { xs: "100%", sm: "100%", md: "100%", lg: "50%" }, // Adjust width on medium screens
            }}
          >
            <Typography
              variant="h3"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                mb: 3,
                width: "100%",
              }}
            >
              Sensei Karim Ebraheem
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
              Sensei Ebraheem Karim, a member of the prestigious National Team
              of Canada and the Karate Ontario team, Sensei Karim boasts an
              impressive competitive history. He's not only a champion, claiming
              first place in Canada's Junior, U21, and Senior divisions, but
              also a world-class competitor. Driven and ambitious, Sensei Karim inspires
              students with his dedication and relentless pursuit of excellence,
              aiming to reach the coveted number one spot on the world stage.
            </Typography>
          </Box>
        </Box>
      </motion.div>
      <motion.div
        initial={{ opacity: 0, x: -50 }}
        whileInView={{ opacity: 1, x: 0 }}
        transition={{ ease: "backInOut", duration: 1 }}
      >
        <Box
          sx={{
            width: "100%",
            display: "flex",
            flexDirection: {
              xs: "column",
              sm: "column",
              md: "column",
              lg: "row",
            },
            justifyContent: "space-between",
            alignItems: {
              xs: "center",
              sm: "center",
              md: "center",
              lg: "start",
            },
            mb: 10,
          }}
        >
          <Box
            component="img"
            sx={{
              width: { xs: "100%", sm: "100%", md: "60%", lg: "50%" },
              height: "500px",
              objectFit: "cover",
              objectPosition: "50% 35%",
              position: "relative",
              borderRadius: "0% 0% 0% 0% / 0% 0% 0% 0%",
              boxShadow: "20px 20px rgba(0,0,0,.15)",
              mb: { xs: 5, sm: 5, md: 5, lg: 0 },
              mr: 5,
              transition: "all .4s ease",
              ":hover": {
                borderRadius: "0% 0% 50% 50% / 0% 0% 5% 5%",
                boxShadow: "10px 10px rgba(0,0,0,.25)",
              },
            }}
            alt="Logo"
            src={images.Mohamed}
          />
          <Box
            component="div"
            sx={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "start",
              alignItems: "start",
              width: { xs: "100%", sm: "100%", md: "100%", lg: "50%" },
            }}
          >
            <Typography
              variant="h3"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                mb: 3,
                width: "100%",
              }}
            >
              Sensei Mohamed Ebraheem
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
              Sensei Ebraheem Mohamed, a dominant force in Ontario tournaments,
              Sensei Mohamed consistently claims first-place finishes,
              demonstrating his exceptional skills. But his ambitions don't stop
              there. This year, he sets his sights even higher, aiming to excel
              at Canada's Nationals and make a significant impact at the
              prestigious Pan American competitions. Sensei Mohamed's dedication
              and competitive spirit are sure to inspire students who share his
              passion for pushing their limits. (Member of the Karate Ontario
              team)
            </Typography>
          </Box>
        </Box>
      </motion.div>
    </Box>
  );
};

export default Instructors;
