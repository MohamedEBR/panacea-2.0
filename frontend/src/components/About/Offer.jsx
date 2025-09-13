import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import AboutCard from "./AboutCard";
import images from "../../constants/images";
const Mission = () => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        width: "100%",
        mb: 10,
        textAlign: {
            xs: "center",
            sm: "center",
            md: "center",
            lg: "start",
          },
      }}
    >
        <Typography
          variant="h6"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            color: "#9d4f4b",
            mb: 5,
            
          }}
        >
          What We Offer
        </Typography>
      <Box 
      >

      </Box>
      <Box
        sx={{
          display: "grid",
          gridTemplateColumns: {
            xs: "repeat(auto-fill, minmax(275px, 1fr))",
            sm: "repeat(auto-fill, minmax(275px, 1fr))",
            md: "repeat(auto-fill, minmax(300px, 1fr))",
            lg: "repeat(auto-fill, minmax(350px, 1fr))",
          }, // Adjust min-width as needed
          justifyContent: "space-between",
          alignItems: "center",
          gap: 2,
        }}
      >
        <AboutCard
          title="Professional Karate Instruction"
          description="Led by certified coaches, our professional instruction focuses on proper technique, discipline, and personal growth in a supportive environment."
          image={images.professional}
        />
        <AboutCard
          title="Boosted Confidence"
          description=" Our professional instructors go beyond teaching technique. They create a supportive environment where students of all ages develop not only physical skills but also self-belief and confidence.  Through a structured curriculum and positive reinforcement, you'll gain the skills and self-assurance to excel both on and off the mat."
          image={images.confidence}
        />
        <AboutCard
          title="Fun and Rewards"
          description="Our expert instructors make learning fun and rewarding. Earn awesome rewards as you progress through competitions, all while building confidence and fitness in a supportive environment."
          image={images.fun}
        />
      </Box>
    </Box>
  );
};

export default Mission;
