import Box from "@mui/material/Box";
import { Fragment } from "react";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { Link } from "react-router-dom";
import images from "../../constants/images";
import { motion } from "framer-motion";
import "./index.scss";

const Programs = () => {
  return (
    <Fragment>
      <Typography
        variant="h6"
        sx={{
          fontFamily: '"Saira Semi Condensed"',
          color: "#9d4f4b",
          px: "7%",
          textAlign: { xs: "center", sm: "center", md: "center", lg: "start" },
          my: 3,
        }}
      >
        Our Programs
      </Typography>
      <Box
        component="div"
        sx={{
          width: { lg: "105vw" },
          height: { md: "90vh", lg: "70vh" },
          display: "flex",
          alignItems: { xs: "center", sm: "center", md: "start", lg: "start" },
          justifyContent: {
            xs: "center",
            sm: "center",
            md: "start",
            lg: "start",
          },
          flexDirection: {
            xs: "column",
            sm: "column",
            md: "row",
            lg: "row",
          },
          px: { xs: "5%", sm: "5%", md: 0 },
          backgroundColor: "#fff",
        }}
      >
        {/* <Box> */}

        <Box
          component="div"
          sx={{
            width: { xs: "100%", sm: "100%", md: "100%", lg: "33.6%" },
            height: "100%",
            clipPath: {
              md: "none",
              lg: "polygon(0 0, calc(100% - 10vh) 0, 100% 100%, 0 100%)",
            },
            WebkitClipPath: {
              mf: "none",
              lg: "polygon(0 0, calc(100% - 10vh) 0, 100% 100%, 0 100%)",
            },
            marginRight: { md: 0, lg: "-4.2vh" },
            padding: { md: "0", lg: "0px 11vh 0px 5px" },
            textAlign: "center",
            position: "relative",
            transition: "transform .3s",

            ":hover": {
              transform: {md: "scale(1.05)",lg: "scale(1.05)"},
            },
          }}
        >
          <Box
            sx={{
              height: "100%",
              img: {
                width: { xs: "100%", sm: "100%", md: "100%", lg: "39.5vw" },
                height: { md: "100%", lg: "100%" },
                objectFit: "cover",
                objectPosition: "center",
                position: "relative",
                filter: "brightness(75%)",
                right: { md: 0, lg: "10%" },
                transition: "transform 7s ease-in-out",
              },
              h2: {
                color: "#fff",
                position: "absolute",
                width: "100%",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                fontFamily: '"Saira Semi Condensed"',
              },
              div: {
                visibility: "hidden",
              },
              ":hover": {
                img: {
                  transform: { md: "scale(1.25)",lg: "scale(1.25)" },
                  filter:{md: "brightness(40%)",lg: "brightness(40%)"},
                },
                h2: {
                  visibility: {md:"hidden", lg: "hidden"},
                },
                div: {
                  visibility: {md:"visible", lg: "visible"},
                },
              },
            }}
          >
            <Box component="img" alt="Logo" src={images.whiteBelt} />
            <Typography variant="h2">Beginner</Typography>
           
              <Box
                component="div"
               
              >
          
                <Typography variant="subtitle1"
                 sx={{
                  
                    color: "#fff",
                    position: "absolute",
                    width: "100%",
                    top: "50%",
                    px: { xs: 2, sm: 2, md: 2, lg: 7 },
                    left: { xs: "50%", sm: "50%", md: "50%", lg: "45%" },
                    transform: "translate(-50%, -50%)",
                    fontFamily: '"Saira Semi Condensed"',
                  
                }}>
                  Kickstart your karate journey! Our Beginner program is the
                  ideal entry point for kids and teens of all backgrounds. No
                  prior experience is necessary! In this supportive environment,
                  you'll learn fundamental techniques like strikes, blocks, and
                  stances under the guidance of experienced instructors. We'll
                  not only build your physical strength and flexibility, but
                  also cultivate focus, discipline, and a sense of
                  accomplishment. This program is a fantastic way to boost your
                  fitness, gain valuable self-defense skills, and discover the
                  empowering world of karate!
                </Typography>

              </Box>
          </Box>
        </Box>
        <Typography variant="subtitle1"
                 sx={{
                  
                    color: "#000",
                    visibility: {md: "hidden", lg: "hidden"},

                    width: {xs: "100%",sm :"100%", md: 0, lg: 0},
                    px: { xs: 2, sm: 2, md: 2, lg: 7 },
                    py: { xs: 5, sm: 5,},
                    position: {md: "absolute", lg: "absolute"},
                    zIndex: {md: -1, lg: -1},
                    fontFamily: '"Saira Semi Condensed"',
                  
                }}>
                  Kickstart your karate journey! Our Beginner program is the
                  ideal entry point for kids and teens of all backgrounds. No
                  prior experience is necessary! In this supportive environment,
                  you'll learn fundamental techniques like strikes, blocks, and
                  stances under the guidance of experienced instructors. We'll
                  not only build your physical strength and flexibility, but
                  also cultivate focus, discipline, and a sense of
                  accomplishment. This program is a fantastic way to boost your
                  fitness, gain valuable self-defense skills, and discover the
                  empowering world of karate!
                </Typography>
        {/* </Box> */}


        <Box
          component="div"
          sx={{
            width: { xs: "100%", sm: "100%", md: "100%", lg: "35%" },
            height: "100%",
            position: "relative",
            clipPath: {
              md: "none",
              lg: "polygon(0 0, calc(100% - 10vh) 0, 100% 100%, calc(0% + 10vh) 100%)",
            },
            WebkitClipPath: {
              md: "none",
              lg: "polygon(0 0, calc(100% - 10vh) 0, 100% 100%, calc(0% + 10vh) 100%)",
            },
            marginLeft: { md: 1, lg: "-4.2vh" },
            marginRight: { md: 1, lg: "-4.2vh" },
            padding: { md: 0, lg: "1px 5px 1px 11vh" },
            textAlign: "center",
            transition: "transform .5s",
            ":hover": {
              transform: {md:"scale(1.05)", lg: "scale(1.05)"},
            },
          }}
        >
          <Box
            sx={{
              height: "100%",
              img: {
                width: { xs: "100%", sm: "100%", md: "95%", lg: "40vw" },
                height: { md: "100%", lg: "101%" },
                objectFit: "cover",
                objectPosition: "center",
                position: "relative",
                filter: "brightness(75%)",
                right: { md: 0, lg: "20%" },
                transition: "transform 7s ease-in-out",
              },
              h2: {
                color: "#fff",
                position: "absolute",
                width: "100%",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                fontFamily: '"Saira Semi Condensed"',
              },
              div: {
                visibility: "hidden",
              },
              ":hover": {
                img: {
                  transform: { md: "scale(1.25)",lg: "scale(1.25)" },
                  filter:{md: "brightness(40%)",lg: "brightness(40%)"},
                },
                h2: {
                  visibility: {md:"hidden", lg: "hidden"},
                },
                div: {
                  visibility: {md:"visible", lg: "visible"},
                },
              },
            }}
          >
            <Box component="img" alt="Logo" src={images.beginner} />
            <Typography variant="h2">Intermediate</Typography>
            <Box
              component="div"
              sx={{
                h6: {
                  color: "#fff",
                  position: "absolute",
                  width: "100%",
                  top: "50%",
                  px: { xs: 2, sm: 2, md: 2, lg: 7 },
                  left: "50%",
                  transform: "translate(-50%, -50%)",
                  fontFamily: '"Saira Semi Condensed"',
                },
              }}
            >
              <Typography variant="subtitle1">
                Take your karate to the next level! Our Intermediate program
                refines your technique for power and precision, focusing on
                advanced strikes, blocks, and footwork. You'll also spar
                competitively and delve deeper into traditional forms (katas).
                This program prepares you to compete in Karate Ontario's
                recreational tournaments and become a well-rounded, confident
                martial artist.
              </Typography>
            </Box>
          </Box>
        </Box>
        <Typography variant="subtitle1"
                 sx={{
                  
                    color: "#000",
                    visibility: {md: "hidden", lg: "hidden"},

                    width: {xs: "100%",sm :"100%", md: 0, lg: 0},
                    px: { xs: 2, sm: 2, md: 2, lg: 7 },
                    py: { xs: 5, sm: 5,},
                    position: {md: "absolute", lg: "absolute"},
                    zIndex: {md: -1, lg: -1},
                    fontFamily: '"Saira Semi Condensed"',
                  
                }}>
                  Take your karate to the next level! Our Intermediate program
                refines your technique for power and precision, focusing on
                advanced strikes, blocks, and footwork. You'll also spar
                competitively and delve deeper into traditional forms (katas).
                This program prepares you to compete in Karate Ontario's
                recreational tournaments and become a well-rounded, confident
                martial artist.
                </Typography>
        <Box
          component="div"
          sx={{
            width: { xs: "100%", sm: "100%", md: "100%", lg: "33.6%" },
            height: "100%",
            position: "relative",
            clipPath: {
              md: "none",
              lg: "polygon(0 0, 100% 0, 100% 100%, calc(0% + 10vh) 100%)",
            },
            WebkitClipPath: {
              md: "none",
              lg: "polygon(0 0, 100% 0, 100% 100%, calc(0% + 10vh) 100%)",
            },
            marginLeft: { md: 0, lg: "-4.2vh" },
            padding: { md: 0, lg: "1px 5px 1px 11vh" },
            textAlign: "center",
            transition: "transform .5s",
            ":hover": {
              transform: {md:"scale(1.05)", lg: "scale(1.05)"},
            },
          }}
        >
          <Box
            sx={{
              height: "100%",
              img: {
                width: { xs: "100%", sm: "100%", md: "100%", lg: "40vw" },
                height: { md: "100%", lg: "101%" },
                objectFit: "cover",
                objectPosition: "center",
                position: "relative",
                filter: "brightness(75%)",
                right: { md: 0, lg: "30%" },
                transition: "transform 7s ease-in-out",
              },
              h2: {
                color: "#fff",
                position: "absolute",
                width: "100%",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                fontFamily: '"Saira Semi Condensed"',
              },
              div: {
                visibility: "hidden",
              },
              ":hover": {
                img: {
                  transform: { md: "scale(1.25)",lg: "scale(1.25)" },
                  filter:{md: "brightness(40%)",lg: "brightness(40%)"},
                },
                h2: {
                  visibility: {md:"hidden", lg: "hidden"},
                },
                div: {
                  visibility: {md:"visible", lg: "visible"},
                },
              },
            }}
          >
            <Box component="img" alt="Logo" src={images.elite} />
            <Typography variant="h2">Elite</Typography>
            <Box
              component="div"
              sx={{
                h6: {
                  color: "#fff",
                  position: "absolute",
                  width: "100%",
                  top: "50%",
                  px: { xs: 2, sm: 2, md: 2, lg: 7 },
                  left: "50%",
                  transform: "translate(-50%, -50%)",
                  fontFamily: '"Saira Semi Condensed"',
                },
              }}
            >
              <Typography variant="subtitle1">
                Designed for dedicated athletes hungry for competition glory,
                this program pushes your limits like never before. We'll hone
                your technique to razor sharpness, focusing on advanced sparring
                strategies, explosive power delivery, and unwavering mental
                focus. Training intensifies as you refine your kata mastery, all
                in preparation for dominating Karate Ontario's elite provincial
                tournaments and ultimately, vying for national recognition at
                the Canada Karate Nationals. Are you ready to step onto the big
                stage and become an elite karate champion?
              </Typography>
            </Box>
          </Box>
        </Box>
        <Typography variant="subtitle1"
                 sx={{
                  
                    color: "#000",
                    visibility: {md: "hidden", lg: "hidden"},

                    width: {xs: "100%",sm :"100%", md: 0, lg: 0},
                    px: { xs: 2, sm: 2, md: 2, lg: 7 },
                    py: { xs: 5, sm: 5,},
                    position: {md: "absolute", lg: "absolute"},
                    zIndex: {md: -1, lg: -1},
                    fontFamily: '"Saira Semi Condensed"',
                  
                }}>
                  Take your karate to the next level! Our Intermediate program
                refines your technique for power and precision, focusing on
                advanced strikes, blocks, and footwork. You'll also spar
                competitively and delve deeper into traditional forms (katas).
                This program prepares you to compete in Karate Ontario's
                recreational tournaments and become a well-rounded, confident
                martial artist.
                </Typography>
      </Box>
      <Box
        sx={{
          width: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Link to="/programs">
          <Button
            variant="contained"
            size="medium"
            sx={{
              mt: 3,
              width: { sm: "175px", md: "225px" },
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
              Discover More
            </Typography>
          </Button>
        </Link>
      </Box>
    </Fragment>
  );
};

export default Programs;
