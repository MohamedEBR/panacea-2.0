import Box from '@mui/material/Box'
import Typography from '@mui/material/Typography'
import {motion} from "framer-motion"

const Mission = () => {
  return (
    <motion.div
      initial={{opacity: 0, x: -20}}
      whileInView={{opacity: 1, x: 0}}
      transition={{ease:'backInOut', duration:2}}>
    <Box sx={{
      display: "flex",
      flexDirection: {xs :"column", sm: "column", md: "column", lg: "row"},
      mb: 10,
    }}>

<Box
sx={{
  width: {lg :"50%"},
  pr:{lg :3}
}}>
      <Typography
          variant="h2"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            color: "#9d4f4b",
          }}
        >
          Vision
        </Typography>
        <Typography
          variant="h4"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            pt: 2,
            width: "100%",
          }}
        >
          Empowering individuals to become confident, well-rounded leaders through the transformative power of karate.
        </Typography>
    </Box>
    <Box sx={{
      mt:{xs: 4,sm:4, md: 4, lg: 0},
      width: {lg :"50%"},
      pr:{lg :3}
    }}>
      <Typography
          variant="h2"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            color: "#9d4f4b",
          }}
        >
          Mission
        </Typography>
        <Typography
          variant="h4"
          sx={{
            fontFamily: '"Saira Semi Condensed"',
            pt: 2,
            width: "100%",
          }}
        >
We ignite personal growth and cultivate inner strength through traditional karate practice, fostering a supportive community that celebrates excellence on and off the mat.        </Typography>
    </Box>
    </Box>
    </motion.div>
  )
}

export default Mission