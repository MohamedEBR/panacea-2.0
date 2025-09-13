import ProgramBox from "./ProgramBox";

import Box from "@mui/material/Box";
import images from "../../constants/images";
import { motion } from "framer-motion";

const ProgramBody = () => {
  return (
    <Box
      component="div"
      sx={{
        flexDirection: "column",
        width: "100%",
        display: "flex",
        alignItems: { xs: "center", sm: "center", md: "center", lg: "start" },
        justifyContent: "center",
        height: "100%",
        textAlign: { xs: "center", sm: "center", md: "center", lg: "start" },
        px: "7%",
        mt: 10,
      }}
    >
      <Box
        sx={{
          width: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          alignItems: { xs: "center", sm: "center", md: "center", lg: "start" },
        }}
      >
        <motion.div
          initial={{ opacity: 0, x: -50 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ ease: "backInOut", duration: 1 }}
        >
          <ProgramBox
            name="Junior Beginners"
            description="Our Junior Beginners program (ages 4 - 7 years) is an energetic introduction to the world of martial arts. Through interactive games and dynamic drills, kids will build foundational karate skills, boost their fitness, and develop essential focus and discipline.  This program is all about building confidence, teamwork, and a love for learning in a safe and encouraging environment. Watch your child transform into a strong, focused individual, ready to take on any challenge!"
            image={images.programBeginner}
          />
        </motion.div>
        <motion.div
          initial={{ opacity: 0, x: -50 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ ease: "backInOut", duration: 1 }}
        >
          <ProgramBox
            name="Beginner"
            description="Our Beginner program is the ideal entry point for teens of all backgrounds. No prior experience is necessary! In this supportive environment, you'll learn fundamental techniques like strikes, blocks, and stances under the guidance of experienced instructors. We'll not only build your physical strength and flexibility, but also cultivate focus, discipline, and a sense of accomplishment. This program is a fantastic way to boost your fitness, gain valuable self-defense skills, and discover the empowering world of karate!"
            image={images.programBeginnerChild}
          />
        </motion.div>
        <motion.div
          initial={{ opacity: 0, x: -50 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ ease: "backInOut", duration: 1 }}
        >
          <ProgramBox
            name="Intermediate"
            description="Take your karate to the next level! Our Intermediate program refines your technique for power and precision, focusing on advanced strikes, blocks, and footwork. You'll also spar competitively and delve deeper into traditional forms (katas). This program prepares you to compete in Karate Ontario's recreational tournaments and become a well-rounded, confident martial artist."
            image={images.programIntermediate}
          />
        </motion.div>
        <motion.div
          initial={{ opacity: 0, x: -50 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ ease: "backInOut", duration: 1 }}
        >
          <ProgramBox
            name="Elite"
            description="Take your karate aspirations to the next level with our Elite program! Designed for dedicated athletes hungry for competition glory, this program pushes your limits like never before. We'll hone your technique to razor sharpness, focusing on advanced sparring strategies, explosive power delivery, and unwavering mental focus. Training intensifies as you refine your kata mastery, all in preparation for dominating Karate Ontario's elite provincial tournaments and ultimately, vying for national recognition at the Canada Karate Nationals. Are you ready to step onto the big stage and become an elite karate champion?"
            image={images.programElite}
          />
        </motion.div>
      </Box>
    </Box>
  );
};

export default ProgramBody;
