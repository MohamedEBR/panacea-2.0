/* eslint-disable */
import "./contact.scss";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import  { useState } from "react";
import emailjs from "@emailjs/browser";

const Contact = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [isNameValid, setIsNameValid] = useState(true);
  const [isEmailValid, setIsEmailValid] = useState(true);

  const validateEmail = (email) => {
    return /\S+@\S+\.\S+/.test(email);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const nameIsValid = name.trim() !== "";
    const emailIsValid = validateEmail(email);
    setIsNameValid(nameIsValid);
    setIsEmailValid(emailIsValid);

    if (!nameIsValid || !emailIsValid) {
      return;
    }

    const serviceId = import.meta.env.VITE_REACT_APP_SERVICE_ID;
    const templateId = import.meta.env.VITE_REACT_APP_TEMPLATE_ID;
    const publicKey = import.meta.env.VITE_REACT_APP_PUBLIC_KEY;
    

    const templateParams = {
      from_name: name,
      from_email: email,
      to_name: "Panacea Karate Academy",
      message: message,
    };

    emailjs
      .send(serviceId, templateId, templateParams, publicKey)
      .then((response) => {
        console.log("Email sent successfully", response);
        alert(
          "Thank you. Panacea Karate Academey's Management will respond shortly",
        );
        setName("");
        setEmail("");
        setMessage("");
      })
      .catch((error) => {
        console.log("Error Sending email:", error);
      });
  };

  const inputStyle = (isValid) => ({
    borderColor: isValid ? "initial" : "red",
  });

  return (
    <Box
      sx={{
        maxWidth: "1170px",
        marginLeft: "auto",
        marginRight: "auto",
        mt: 5,
        padding: "1em",
        marginBottom: "30px",
      }}
    >
      <Typography
        variant="h2"
        sx={{
          textAlign: { xs: "center", sm: "center", md: "left", lg: "left" },
          fontWeight: "300",
          textTransform: "uppercase",
          letterSpacing: "0.1em",
          color: "#9d4f4b",
          fontFamily: '"Saira Semi Condensed", sans-serif',

          mb: 3,
        }}
      >
        Get In Touch
      </Typography>

      <Box
        variant="div"
        sx={{
          boxShadow: "0 0 20px 0 #9d4f4b",
          display: { md: "grid", lg: "grid" },
          gridTemplateColumns: { md: "1fr 2fr", lg: "1fr 2fr" },
          "& > *": { padding: { xs: "1em", sm: "1em", md: "2em", lg: "2em" } },
        }}
      >
        <Box
          sx={{
            background: "#9d4f4b",
            color: "#fff",
            borderTopLeftRadius: "4px",
            borderTopRightRadius: "4px",
            borderRadius: { lg: "4px 0 0 4px" },
            h3: {
              textAlign: {
                xs: "center",
                sm: "center",
                md: "center",
                lg: "left",
              },
              margin: "0 0 1rem 0",
              fontFamily: '"Saira Semi Condensed", sans-serif',
            },
            ul: {
              textAlign: {
                xs: "center",
                sm: "center",
                md: "center",
                lg: "left",
              },
              margin: "0 0 1rem 0",
              fontFamily: '"Saira Semi Condensed", sans-serif',
            },
          }}
        >
          <h3>Panacea Karate Academy</h3>

          <ul>
            <li>
              <i className="fa fa-road"></i> 1201 Britannia Road West,
              Mississauga, ON
            </li>
            <li>
              <i className="fa fa-phone"></i> (647) 772-2249
            </li>
            <li>
              <i className="fa fa-envelope"></i> info@panaceakarateacademy.com
            </li>
          </ul>
          <Box
          sx={{
            width: '100%',
            height: '425px',
          }}>
      <iframe
        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2889.1485748267414!2d-79.7070768!3d43.603448!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x882b40455744469f%3A0x6576d101b02f6d61!2s1201%20Britannia%20Rd%20W%2C%20Mississauga%2C%20ON%20L5V%201N2!5e0!3m2!1sen!2sca!4v1703445905462!5m2!1sen!2sca"
        style={{ border: "0",
      width: "100%",
      height: "100%",
      }}
        allowfullscreen=""
        loading="lazy"
        referrerPolicy="no-referrer-when-downgrade"
      ></iframe>
          </Box>
        </Box>

        <Box
          sx={{
            background: "#fff",
            borderBottomLeftRadius: "4px",
            borderBottomRightRadius: "4px",
            borderRadius: { lg: "0 4px 4px 0" },
            fontFamily: '"Saira Semi Condensed", sans-serif',
            h3: {
              fontFamily: '"Saira Semi Condensed", sans-serif',
            },
            form: {
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              gridGap: "20px",
              label: {
                display: "block",
              },
              p: {
                m: 0,
              },
              ".full": {
                gridColumn: "1 / 3",
              },
              input: {
                width: "100%",
                padding: "1em",
                border: "solid 1px #9d4f4b",
                borderRadius: "4px",
              },
              textarea: {
                width: "100%",
                padding: "1em",
                border: "solid 1px #9d4f4b",
                borderRadius: "4px",
                resize: "none",
              },
              button: {
                width: "100%",
                padding: "1em",
                borderRadius: "4px",
                resize: "none",
                background: "#9d4f4b",
                border: "0",
                color: "#e4e4e4",
                textTransform: "uppercase",
                fontSize: "14px",
                fontWeight: "bold",
                ":hover": {
                  background: "#62312e",
                  color: "#ffffff",
                  outline: "0",
                  transition: "background-color 2s ease-out",
                },
                ":focus": {
                  background: "#62312e",
                  color: "#ffffff",
                  outline: "0",
                  transition: "background-color 2s ease-out",
                },
              },
            },
          }}
        >
          <h3>E-mail Us</h3>

          <form id="contact-form" onSubmit={handleSubmit}>
            <p className="full">
              <label>Name</label>
              <input type="text" name="name" id="name" required 
              placeholder="Full Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              style={inputStyle(isNameValid)}/>
            </p>

            <p className="full">
              <label>E-mail Address</label>
              <input type="email" name="email" id="email" required
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={inputStyle(isEmailValid)} />
            </p>


            <p className="full">
              <label>Message</label>
              <textarea name="message" rows="5" id="message"
              
              placeholder="Write here"
          value={message}
          onChange={(e) => setMessage(e.target.value)}></textarea>
            </p>

            <p className="full">
              <button type="submit">Submit</button>
            </p>
          </form>
        </Box>
      </Box>
    </Box>
  );
};

export default Contact;
