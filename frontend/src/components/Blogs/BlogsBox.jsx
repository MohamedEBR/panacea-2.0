
import ListItem from '@mui/material/ListItem';
import {motion} from 'framer-motion'
import Divider from '@mui/material/Divider';
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";




const BlogsBox = (prop) => {
 

  return (
    <>
    
              <ListItem sx={{
                flexDirection: "column",
                width: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                textAlign: "center",
              }}>
                <motion.div
              initial={{ opacity: 0, x: -50 }}
              whileInView={{ opacity: 1, x: 0 }}
              transition={{ ease: "backInOut", duration: 1 }}
            >
               
              <Typography 
              variant='h4'
              sx={{
                py:{xs:1,sm:2,md:3,lg:4},
                fontSize: {xs:20,sm: 30},
                fontFamily: '"Saira Semi Condensed"',

                color:"#9d4f4b",
                // ":hover" : {
                //   color:"#763C39",
                // }
              }}
              >
                    {prop.title}
                 
                </Typography>
             
              <Box
        component="img"
        sx={{
          width: { xs: "100%", sm: "100%", md: "100%", lg: "800px" },
          mb: 3,
          boxShadow: "rgba(60, 64, 67, 0.3) 0px 1px 2px 0px, rgba(60, 64, 67, 0.15) 0px 2px 6px 2px",
          borderRadius: "2%"          
        }}
        alt="blog"
        src={prop.image}      />
               
               <Typography 
        variant="subtitle1"
        sx={{
          fontFamily: '"Saira Semi Condensed"',
          py: 1,
          fontSize: {xs:12,sm: 15, md: 17, lg: 20},
          // width: "100%",

        }}>
          {prop.content}
          </Typography>

                <Typography
                sx={{
                  fontFamily: '"Saira Semi Condensed"',
                  color: 'grey',
          fontSize: {xs:12,sm: 13, md: 14, lg: 15},
                }}>
                  {prop.date}
                </Typography>
            </motion.div>
                
              </ListItem>
              <Divider variant="middle" component="li" />

    </>
  )
}

export default BlogsBox