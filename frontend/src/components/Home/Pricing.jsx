import Box from "@mui/joy/Box";
import Card from "@mui/joy/Card";
import CardActions from "@mui/joy/CardActions";
import Chip from "@mui/joy/Chip";
import Divider from "@mui/joy/Divider";
import List from "@mui/joy/List";
import ListItem from "@mui/joy/ListItem";
import ListItemDecorator from "@mui/joy/ListItemDecorator";
import Typography from "@mui/material/Typography";
import Check from "@mui/icons-material/Check";
import { CustomBox } from "../shared/CustomBox";
import Button from "@mui/material/Button";
import {Link} from "react-router-dom"

export default function Pricing() {
  return (
    <CustomBox
      sx={{
        p: "3% 7%",
        mt: 10,
        
        boxShadow: 3,
        display: "flex",
        flexDirection: "column",

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
          color: "#fff",
          my: 3,
        }}
      >
        Pricing
      </Typography>
      <Typography
        variant="h4"
        sx={{
          fontFamily: '"Saira Semi Condensed"',
          color: "#fff",
          mt: 3,
          mb: 5,
        }}
      >
        We offer flexible membership plans to suit your needs and budget.
      </Typography>
      <Typography
        variant="subtitle1"
        sx={{
          fontFamily: '"Saira Semi Condensed"',
          color: "#fff",
          mt: 3,
          mb: 5,
          fontWeight: 'bold',
        }}
      >
        *Contact for Pricing Information*
      </Typography>
      <Link to="/contact">
            <Button
              variant="contained"
              size="medium"
              sx={{
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
                Contact
              </Typography>
            </Button>
          </Link>
      {/* <Box
        component="div"
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
      > */}
        {/* <Card
          size="lg"
          variant="outlined"
          sx={{
            mx: 2,
          }}
        >
          <Chip size="sm" variant="outlined" color="neutral">
            Junior
          </Chip>
          <Typography
            variant="h3"
            sx={{ fontFamily: '"Saira Semi Condensed"' }}
          >
            Beginner
          </Typography>
          <Divider inset="none" />
          <List size="sm" sx={{ mx: "calc(-1 * var(--ListItem-paddingX))" }}>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              2 hr/week
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Fun and Games
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Basic Techniques
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Forms (Katas)
            </ListItem>
          </List>
          <Divider inset="none" />
          <CardActions>
            <Typography
              variant="h4"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                mr: "auto",
                width: "100%",
                textAlign: {
                  xs: "center",
                  sm: "center",
                  md: "start",
                  lg: "start",
                },
                color: "#9d4f4b",
              }}
            >
              $85{" "}
              <Typography variant="body2" color="text.secondary">
                / month
              </Typography>
            </Typography>
          </CardActions>
        </Card>
        <Card
          size="lg"
          variant="outlined"
          sx={{
            mx: 2,
          }}
        >
          <Chip size="sm" variant="outlined" color="neutral">
            Basic
          </Chip>
          <Typography
            variant="h3"
            sx={{ fontFamily: '"Saira Semi Condensed"' }}
          >
            Beginner
          </Typography>
          <Divider inset="none" />
          <List size="sm" sx={{ mx: "calc(-1 * var(--ListItem-paddingX))" }}>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              4 hr/week
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Basic Techniques
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Forms (Katas)
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Goal Setting
            </ListItem>
          </List>
          <Divider inset="none" />
          <CardActions>
            <Typography
              variant="h4"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                mr: "auto",
                width: "100%",
                textAlign: {
                  xs: "center",
                  sm: "center",
                  md: "start",
                  lg: "start",
                },
                color: "#9d4f4b",
              }}
            >
              $105{" "}
              <Typography variant="body2" color="text.secondary">
                / month
              </Typography>
            </Typography>
          </CardActions>
        </Card>
        <Card
          size="lg"
          variant="outlined"
          sx={{
            mx: 2,
          }}
        >
          <Chip size="sm" variant="outlined" color="neutral">
            Basic
          </Chip>
          <Typography
            variant="h3"
            sx={{ fontFamily: '"Saira Semi Condensed"' }}
          >
            Intermediate
          </Typography>
          <Divider inset="none" />
          <List size="sm" sx={{ mx: "calc(-1 * var(--ListItem-paddingX))" }}>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              8 hr/week
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Advanced Techniques
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Competitions
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Sparring/Combat Training
            </ListItem>
          </List>
          <Divider inset="none" />
          <CardActions>
            <Typography
              variant="h4"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                mr: "auto",
                width: "100%",
                textAlign: {
                  xs: "center",
                  sm: "center",
                  md: "start",
                  lg: "start",
                },
                color: "#9d4f4b",
              }}
            >
              $150{" "}
              <Typography variant="body2" color="text.secondary">
                / month
              </Typography>
            </Typography>
          </CardActions>
        </Card>
        <Card
          size="lg"
          variant="outlined"
          sx={{
            mx: 2,
          }}
        >
          <Chip size="sm" variant="outlined" color="neutral">
            Basic
          </Chip>
          <Typography
            variant="h3"
            sx={{ fontFamily: '"Saira Semi Condensed"' }}
          >
            Elite
          </Typography>
          <Divider inset="none" />
          <List size="sm" sx={{ mx: "calc(-1 * var(--ListItem-paddingX))" }}>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              11 hr/week
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
             National Competitions
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Training Camps
            </ListItem>
            <ListItem>
              <ListItemDecorator>
                <Check />
              </ListItemDecorator>
              Travel Opportunities
            </ListItem>
          </List>
          <Divider inset="none" />
          <CardActions>
            <Typography
              variant="h4"
              sx={{
                fontFamily: '"Saira Semi Condensed"',
                mr: "auto",
                width: "100%",
                textAlign: {
                  xs: "center",
                  sm: "center",
                  md: "start",
                  lg: "start",
                },
                color: "#9d4f4b",
              }}
            >
              $250{" "}
              <Typography variant="body2" color="text.secondary">
                / month
              </Typography>
            </Typography>
          </CardActions>
        </Card> */}
      {/* </Box> */}
     
    </CustomBox>
  );
}
