import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Signup = () => {
  const navigate = useNavigate();
  const { register, loading } = useAuth();
  
  const [inputValue, setInputValue] = useState({
    email: "",
    password: "",
    name: "",
    lastName: "",
  });
  
  const { email, password, name, lastName } = inputValue;
  
  const handleOnChange = (e) => {
    const { name, value } = e.target;
    setInputValue({
      ...inputValue,
      [name]: value,
    });
  };

  const handleError = (err) =>
    toast.error(err, {
      position: "bottom-left",
    });
    
  const handleSuccess = (msg) =>
    toast.success(msg, {
      position: "bottom-right",
    });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!email || !password || !name || !lastName) {
      handleError("Please fill in all fields");
      return;
    }
    
    try {
      const result = await register({
        email,
        password,
        name,
        lastName
      });
      
      if (result.success) {
        handleSuccess("Registration successful!");
        setTimeout(() => {
          navigate("/");
        }, 1000);
      } else {
        handleError(result.message || "Registration failed");
      }
    } catch (error) {
      console.error("Registration error:", error);
      handleError("Registration failed. Please try again.");
    }
    
    // Clear form
    setInputValue({
      email: "",
      password: "",
      name: "",
      lastName: "",
    });
  };

  return (
    <div className="form_container">
      <h2>Signup Account</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            name="email"
            value={email}
            placeholder="Enter your email"
            onChange={handleOnChange}
            required
            disabled={loading}
          />
        </div>
        
        <div>
          <label htmlFor="name">First Name</label>
          <input
            type="text"
            name="name"
            value={name}
            placeholder="Enter your first name"
            onChange={handleOnChange}
            required
            disabled={loading}
          />
        </div>
        
        <div>
          <label htmlFor="lastName">Last Name</label>
          <input
            type="text"
            name="lastName"
            value={lastName}
            placeholder="Enter your last name"
            onChange={handleOnChange}
            required
            disabled={loading}
          />
        </div>
        
        <div>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            name="password"
            value={password}
            placeholder="Enter your password"
            onChange={handleOnChange}
            required
            disabled={loading}
          />
        </div>
        
        <button type="submit" disabled={loading}>
          {loading ? "Creating account..." : "Submit"}
        </button>
        
        <span>
          Already have an account? <Link to={"/login"}>Login</Link>
        </span>
      </form>
      <ToastContainer />
    </div>
  );
};

export default Signup;