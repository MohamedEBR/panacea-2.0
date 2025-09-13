import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import AccountCircle from "@mui/icons-material/AccountCircle";
import useScrollTrigger from "@mui/material/useScrollTrigger";
import Slide from "@mui/material/Slide";
import CssBaseline from "@mui/material/CssBaseline";
import * as React from "react";
import PropTypes from "prop-types";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import Drawer from "@mui/material/Drawer";
import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import { useAuth } from "../../contexts/AuthContext";
import images from "../../constants/images";
import { Link, useNavigate } from "react-router-dom";

const navItems = ["Home", "About", "Programs", "Blogs", "Contact"];
const drawerWidth = 300;

const returnNavItem = (item) => {
  if (item == "Home") {
    return "";
  }
  return item;
};

function HideOnScroll(props) {
  const { children, window } = props;
  const trigger = useScrollTrigger({
    target: window ? window() : undefined,
  });

  return (
    <Slide appear={false} direction="down" in={!trigger}>
      {children}
    </Slide>
  );
}

HideOnScroll.propTypes = {
  children: PropTypes.element.isRequired,
  window: PropTypes.func,
};

function Header(props) {
  const { window } = props;
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const [anchorEl, setAnchorEl] = React.useState(null);
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleDrawerToggle = () => {
    setMobileOpen((prevState) => !prevState);
  };

  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = async () => {
    handleMenuClose();
    await logout();
    navigate('/');
  };

  const handleLogin = () => {
    navigate('/login');
  };

  // Auth menu items for mobile drawer
  const getAuthMenuItems = () => {
    if (isAuthenticated()) {
      return [
        ...(isAdmin() ? ["Create Blog"] : []),
        "Logout"
      ];
    }
    return ["Login", "Signup"];
  };

  const handleAuthMenuItemClick = (item) => {
    switch(item) {
      case "Login":
        navigate('/login');
        break;
      case "Signup":
        navigate('/signup');
        break;
      case "Create Blog":
        navigate('/blogs/new');
        break;
      case "Logout":
        logout().then(() => navigate('/'));
        break;
      default:
        break;
    }
  };

  const drawer = (
    <Box onClick={handleDrawerToggle} sx={{ textAlign: "center" }}>
      <Box
        component="div"
        sx={{
          flexGrow: 1,
        }}
      >
        <Box
          component="img"
          sx={{
            height: 120,
            marginTop: 2,
            maxHeight: { xs: 233, md: 167 },
            maxWidth: { xs: 350, md: 250 },
          }}
          alt="Logo"
          src={images.Logo}
        />
      </Box>
      <Typography variant="h6" sx={{
        marginBottom: 2,
        fontWeight: "bold",
        }}>
        Panacea Karate Academy
      </Typography>
      <Divider />
      
      {/* Navigation items */}
      <List>
        {navItems.map((item) => (
          <ListItem key={item} disablePadding>
            <Link
                to={`/${returnNavItem(item).toLowerCase()}`}
                style={{ textDecoration: "none", color: "inherit",
                  width: "100%"
                 }}
              >
            <ListItemButton
              sx={{
                textAlign: "start",
                ":hover": {
                  backgroundColor: "#9d4f4b",
                  color: "white",
                },
              }}
            >
                <ListItemText primary={item} />
            </ListItemButton>
            </Link>
          </ListItem>
        ))}
      </List>

      <Divider />
      
      {/* Auth items */}
      <List>
        {isAuthenticated() && (
          <ListItem>
            <Typography variant="body2" sx={{ p: 1 }}>
              Welcome, {user?.name || user?.email}
              {isAdmin() && <Typography variant="caption" display="block">Admin</Typography>}
            </Typography>
          </ListItem>
        )}
        
        {getAuthMenuItems().map((item) => (
          <ListItem key={item} disablePadding>
            <ListItemButton
              onClick={() => handleAuthMenuItemClick(item)}
              sx={{
                textAlign: "start",
                ":hover": {
                  backgroundColor: "#9d4f4b",
                  color: "white",
                },
              }}
            >
              <ListItemText primary={item} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );

  const container =
    window !== undefined ? () => window().document.body : undefined;

  return (
    <React.Fragment>
      <CssBaseline />
      <HideOnScroll {...props}>
        <Box sx={{ flexGrow: 1 }}>
          <AppBar component="nav" position="static" color="transparent">
            <Toolbar
              style={{ paddingLeft: 0 }}
              sx={{
                height: "75px",
                backgroundColor: { sm: "#fff", md: "#9d4f4b" },
              }}
            >
              <Box
                component="div"
                sx={{
                  flexGrow: 1,
                  display: "flex",
                  flexDirection: "row",
                  alignItems: "center",
                  backgroundColor: "#fff",
                }}
              >
                <Box
                  component="div"
                  sx={{
                    flexGrow: 1,
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                    transform: "skew(-20deg)",
                    backgroundColor: "#fff",
                    pl: {xs: "5%",sm : "7%",md :"12%", lg: "12%"},
                  }}
                >
                  <Box
                    component="img"
                    sx={{
                      height: 75,
                      paddingTop: 1,
                      maxHeight: { xs: 233, md: 167 },
                      maxWidth: { xs: 350, md: 250 },
                      transform: "skew(20deg)",
                    }}
                    alt="Logo"
                    src={images.Logo}
                  />
                  <Link
                    to="/"
                    style={{ textDecoration: "none", color: "inherit" }}
                  >
                    <Typography
                      variant="h5"
                      noWrap
                      sx={{
                        mr: 2,
                        display: "flex",
                        fontWeight: 700,
                        color: "inherit",
                        textDecoration: "none",
                        fontFamily: '"Saira Semi Condensed"',
                        pl: {xs :1, sm : 3, md: 3, lg: 3},
                        fontSize: {xs : '1.25rem'},
                        transform: "skew(20deg)",
                      }}
                    >
                      Panacea Karate Academy
                    </Typography>
                  </Link>
                </Box>
              </Box>

              {/* Desktop Navigation */}
              <Box
                sx={{
                  display: { xs: "none", sm: "none", md: "block" },
                  p: 0,
                  mr: "7%",
                  height: "75px",
                  backgroundColor: "#9d4f4b",
                }}
              >
                {navItems.map((item) => (
                   <Link
                   key={item}
                   to={`/${returnNavItem(item).toLowerCase()}`}
                   style={{ textDecoration: "none", color: "inherit" }}
                 >
                  <Button
                    sx={{
                      backgroundColor: "#9d4f4b",
                      color: "#fff",
                      textTransform: "none",
                      fontSize: { md: 14, lg: 16 },
                      borderRadius: 0,
                      width: { md: "90px", lg: "130px" },
                      height: { md: "100%" },
                      transform: "skew(-20deg)",
                      ":hover": {
                        backgroundColor: "#fff",
                        color: "#9d4f4b",
                      },
                    }}
                  >
                    <Typography
                      sx={{
                        transform: "skew(20deg)",
                        fontFamily: '"Saira Semi Condensed"',
                      }}
                    >
                        {item}
                    </Typography>
                  </Button>
                  </Link>
                ))}

                {/* Desktop Auth Section */}
                {isAuthenticated() ? (
                  <Box sx={{ display: 'inline-flex', alignItems: 'center' }}>
                    <IconButton
                      size="large"
                      onClick={handleProfileMenuOpen}
                      color="inherit"
                      sx={{ 
                        height: '75px',
                        borderRadius: 0,
                        transform: "skew(-20deg)",
                        ":hover": {
                          backgroundColor: "#fff",
                          color: "#9d4f4b",
                        },
                      }}
                    >
                      <AccountCircle sx={{ transform: "skew(20deg)" }} />
                    </IconButton>
                    <Menu
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={handleMenuClose}
                      anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'right',
                      }}
                      transformOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                      }}
                    >
                      <MenuItem disabled>
                        <Typography variant="body2">
                          {user?.name || user?.email}
                          {isAdmin() && <Typography variant="caption" display="block">Admin</Typography>}
                        </Typography>
                      </MenuItem>
                      <Divider />
                      {isAdmin() && (
                        <MenuItem onClick={() => { handleMenuClose(); navigate('/blogs/new'); }}>
                          Create Blog
                        </MenuItem>
                      )}
                      <MenuItem onClick={handleLogout}>Logout</MenuItem>
                    </Menu>
                  </Box>
                ) : (
                  <Box sx={{ display: 'inline-flex' }}>
                    <Button
                      onClick={handleLogin}
                      sx={{
                        backgroundColor: "#9d4f4b",
                        color: "#fff",
                        textTransform: "none",
                        fontSize: { md: 14, lg: 16 },
                        borderRadius: 0,
                        width: { md: "90px", lg: "100px" },
                        height: { md: "100%" },
                        transform: "skew(-20deg)",
                        ":hover": {
                          backgroundColor: "#fff",
                          color: "#9d4f4b",
                        },
                      }}
                    >
                      <Typography
                        sx={{
                          transform: "skew(20deg)",
                          fontFamily: '"Saira Semi Condensed"',
                        }}
                      >
                        Login
                      </Typography>
                    </Button>
                  </Box>
                )}
              </Box>

              {/* Mobile menu button */}
              <IconButton
                color="inherit"
                aria-label="open drawer"
                edge="start"
                onClick={handleDrawerToggle}
                sx={{ mr:{xs : "3%", sm :"7%"}, display: { md: "none" } }}
              >
                <MenuIcon />
              </IconButton>
            </Toolbar>
          </AppBar>
          <nav>
            <Drawer
              container={container}
              variant="temporary"
              open={mobileOpen}
              anchor={"right"}
              onClose={handleDrawerToggle}
              ModalProps={{
                keepMounted: true,
              }}
              sx={{
                display: { sm: "block", md: "none" },
                "& .MuiDrawer-paper": {
                  boxSizing: "border-box",
                  width: drawerWidth,
                },
              }}
            >
              {drawer}
            </Drawer>
          </nav>
        </Box>
      </HideOnScroll>
    </React.Fragment>
  );
}

Header.propTypes = {
  window: PropTypes.func,
};

export default Header;
