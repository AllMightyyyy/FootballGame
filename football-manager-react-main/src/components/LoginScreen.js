import {
  Alert,
  Box,
  Button,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material";
import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../contexts/AuthContext";

const LoginScreen = () => {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [isLoading, setIsLoading] = useState(false); // Added isLoading state

  const handleChange = (e) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true); // Set loading true before making the API call

    console.log("Attempting to login with credentials:", credentials);

    const result = await login(credentials);
    setIsLoading(false); // Set loading false after the API call completes

    console.log("Login result:", result);

    if (result.success) {
      if (result.hasTeam) {
        console.log("Login successful, navigating to the home page.");
        navigate("/"); // Navigate to the home page
      } else {
        console.log("Login successful, navigating to onboarding.");
        navigate("/onboarding"); // Navigate to the onboarding flow
      }
    } else {
      console.error("Login failed with message:", result.message);
      setError(result.message);
      setOpenSnackbar(true);
    }
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  return (
    <Box
      sx={{
        backgroundColor: "rgba(30, 30, 30, 0.8)",
        padding: "40px",
        borderRadius: "12px",
        boxShadow: "0 8px 32px rgba(0, 0, 0, 0.37)",
        maxWidth: "400px",
        width: "100%",
        margin: "24px auto",
      }}
    >
      <Typography variant="h4" sx={{ color: "#f5f5f5", marginBottom: "16px" }}>
        Log In
      </Typography>
      <form onSubmit={handleSubmit}>
        <TextField
          name="username"
          label="Username"
          fullWidth
          required
          value={credentials.username}
          onChange={handleChange}
          sx={{
            backgroundColor: "#2e2e2e",
            borderRadius: "4px",
            marginBottom: "16px",
            input: { color: "#fff" },
            "& .MuiOutlinedInput-root": {
              "& fieldset": {
                borderColor: "#555",
              },
              "&:hover fieldset": {
                borderColor: "#888",
              },
              "&.Mui-focused fieldset": {
                borderColor: "#487748",
              },
            },
            "& .MuiInputLabel-root": {
              color: "#aaa",
            },
          }}
        />
        <TextField
          name="password"
          label="Password"
          type="password"
          fullWidth
          required
          value={credentials.password}
          onChange={handleChange}
          sx={{
            backgroundColor: "#2e2e2e",
            borderRadius: "4px",
            marginBottom: "16px",
            input: { color: "#fff" },
            "& .MuiOutlinedInput-root": {
              "& fieldset": {
                borderColor: "#555",
              },
              "&:hover fieldset": {
                borderColor: "#888",
              },
              "&.Mui-focused fieldset": {
                borderColor: "#487748",
              },
            },
            "& .MuiInputLabel-root": {
              color: "#aaa",
            },
          }}
        />
        <Button
          type="submit"
          variant="contained"
          sx={{
            backgroundColor: isLoading ? "#888" : "#487748",
            color: "#121212",
            width: "100%",
            padding: "10px",
            fontWeight: "bold",
          }}
          disabled={isLoading} // Disable button during loading
        >
          {isLoading ? "Logging in..." : "Log In"}
        </Button>
      </form>

      {/* Snackbar for error messages */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity="error"
          sx={{ width: "100%" }}
        >
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default LoginScreen;
