import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { CardActionArea } from "@mui/material";

export default function AboutCard(prop) {
  return (
    <Card >
      <CardActionArea
    >
        <CardMedia
          component="img"
          image={prop.image}
          alt="Offers"

        />
        <CardContent
        sx={{
            height: '200px'
        }}>
          <Typography gutterBottom variant="h5" component="div">
            {prop.title}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {prop.description}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
