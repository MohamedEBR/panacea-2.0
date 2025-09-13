import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {motion} from 'framer-motion'

const ProgramBox = (prop) => {
  return (
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
        alignItems: { xs: "center", sm: "center", md: "center", lg: "start" },
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
          mr: { xs: 0, sm: 0, md: 0, lg: 10 },
          boxShadow:
            "rgba(157, 79, 75, 0.4) 5px 5px, rgba(157, 79, 75, .3) 10px 10px, rgba(157, 79, 75, 0.2) 15px 15px, rgba(157, 79, 75, 0.1) 20px 20px, rgba(240, 46, 170, 0.05) 25px 25px",
        }}
        alt="Logo"
        src={prop.image}
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
          {prop.name}
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
          {prop.description}
        </Typography>
      </Box>
    </Box>
  );
};

export default ProgramBox;
