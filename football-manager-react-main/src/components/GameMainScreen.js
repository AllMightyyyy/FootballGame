// src/components/GameMainScreen.js

import React, { useState, useContext } from "react";
import { Box, Button, TextField, Typography } from "@mui/material";
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom'; 
import { api } from "../api";

const GameMainScreen = () => {
  const { register } = useContext(AuthContext);
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [managerName, setManagerName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [usernameAvailable, setUsernameAvailable] = useState(false);
  const [suggestedUsername, setSuggestedUsername] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const checkUsername = async () => {
    if (managerName.length < 3) {
      setError("Manager name must be at least 3 characters.");
      return;
    }
    try {
      const response = await api.get("/auth/check-username", {
        params: { username: managerName },
      });
      // Backend now returns { available: boolean, suggestedUsername: string (optional) }
      const { available, suggestedUsername: suggested } = response.data;
      setUsernameAvailable(available);
      if (!available && suggested) {
        setSuggestedUsername(suggested);
      } else {
        setSuggestedUsername("");
      }
      setError("");
    } catch (err) {
      console.error("Error checking username:", err);
      if (err.response && err.response.data) {
        const { available, suggestedUsername } = err.response.data;
        setUsernameAvailable(available);
        if (!available && suggestedUsername) {
          setSuggestedUsername(suggestedUsername);
        }
      }
      setError("Error checking username.");
    }
  };

  const handleUseSuggested = () => {
    if (suggestedUsername) {
      setManagerName(suggestedUsername);
      setUsernameAvailable(true);
      setSuggestedUsername("");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!usernameAvailable) {
      setError("Username is not available. Please choose another one.");
      return;
    }

    if (!managerName || !email || !password) {
      setError("Please fill in all fields.");
      return;
    }

    setIsSubmitting(true);
    const registrationData = { username: managerName, email, password };

    const result = await register(registrationData);
    setIsSubmitting(false);

    if (result.success) {
      if (result.hasTeam) {
        console.log('Registration successful, navigating to home.');
        navigate('/'); // Navigate to home if user already has a team
      } else {
        console.log('Registration successful, navigating to onboarding.');
        navigate('/onboarding'); // Navigate to onboarding for team assignment
      }
    } else {
      setError(result.message);
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "20px",
        padding: "30px",
        borderRadius: "8px",
      }}
    >
      {step === 1 && (
        <Box
          sx={{
            backgroundColor: "#1e1e1e",
            borderRadius: "8px",
            padding: "20px",
            width: "100%",
            maxWidth: "400px",
          }}
        >
          <Typography variant="h5" sx={{ color: "#fff", marginBottom: "16px" }}>
            Enter Manager Name
          </Typography>
          <TextField
            value={managerName}
            onChange={(e) => setManagerName(e.target.value)}
            label="Manager Name"
            fullWidth
            sx={{
              backgroundColor: "#2e2e2e",
              borderRadius: "4px",
              marginBottom: "16px",
              input: { color: "#fff" },
              "& .MuiOutlinedInput-root": {
                "& fieldset": { borderColor: "#444" },
                "&:hover fieldset": { borderColor: "#888" },
                "&.Mui-focused fieldset": { borderColor: "#fff" },
              },
              "& .MuiInputLabel-root": { color: "#aaa" },
            }}
          />
          <Button
            variant="contained"
            onClick={checkUsername}
            sx={{ backgroundColor: "#487748", color: "#fff", width: "100%", marginBottom: "16px" }}
            disabled={!managerName}
          >
            Check Availability
          </Button>
          {usernameAvailable && (
            <Button
              variant="contained"
              onClick={() => setStep(2)}
              sx={{ backgroundColor: "#487748", color: "#fff", width: "100%", marginBottom: "16px" }}
            >
              Proceed to Sign Up
            </Button>
          )}
          {!usernameAvailable && suggestedUsername && (
            <Box sx={{ marginBottom: "16px" }}>
              <Typography variant="body2" color="error">
                Username is taken. Suggested: {suggestedUsername}
              </Typography>
              <Button onClick={handleUseSuggested} size="small" sx={{ marginTop: "8px" }}>
                Use Suggested Username
              </Button>
            </Box>
          )}
          {error && (
            <Typography variant="body2" color="error" sx={{ marginBottom: "16px" }}>
              {error}
            </Typography>
          )}
        </Box>
      )}

      {step === 2 && (
        <Box
          sx={{
            backgroundColor: "#1e1e1e",
            borderRadius: "8px",
            padding: "20px",
            width: "100%",
            maxWidth: "400px",
          }}
        >
          <form onSubmit={handleSubmit}>
            <Typography variant="h5" sx={{ color: "#fff", marginBottom: "16px" }}>
              Sign Up
            </Typography>
            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              fullWidth
              required
              sx={{
                backgroundColor: "#2e2e2e",
                borderRadius: "4px",
                marginBottom: "16px",
                input: { color: "#fff" },
                "& .MuiOutlinedInput-root": {
                  "& fieldset": { borderColor: "#444" },
                  "&:hover fieldset": { borderColor: "#888" },
                  "&.Mui-focused fieldset": { borderColor: "#fff" },
                },
                "& .MuiInputLabel-root": { color: "#aaa" },
              }}
            />
            <TextField
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
              required
              sx={{
                backgroundColor: "#2e2e2e",
                borderRadius: "4px",
                marginBottom: "16px",
                input: { color: "#fff" },
                "& .MuiOutlinedInput-root": {
                  "& fieldset": { borderColor: "#444" },
                  "&:hover fieldset": { borderColor: "#888" },
                  "&.Mui-focused fieldset": { borderColor: "#fff" },
                },
                "& .MuiInputLabel-root": { color: "#aaa" },
              }}
            />
            {error && (
              <Typography variant="body2" color="error" sx={{ marginBottom: "16px" }}>
                {error}
              </Typography>
            )}
            <Button
              type="submit"
              variant="contained"
              sx={{
                backgroundColor: "#487748",
                color: "#121212",
                width: "100%",
                padding: "10px",
                fontWeight: "bold",
              }}
              disabled={isSubmitting}
            >
              {isSubmitting ? "Creating Account..." : "Create Account"}
            </Button>
          </form>
        </Box>
      )}
    </Box>
  );
};

export default GameMainScreen;
