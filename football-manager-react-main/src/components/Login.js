import React, { useState } from "react";
import { Box, Button, TextField, Typography } from "@mui/material";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = () => {
    console.log("Logging in with", email, password);
  };

  return (
    <Box
      sx={{
        backgroundColor: "#1e1e1e",
        padding: "40px",
        borderRadius: "8px",
        color: "#fff",
        maxWidth: "400px",
        margin: "0 auto",
      }}
    >
      <Typography variant="h4" sx={{ textAlign: "center", marginBottom: "20px" }}>
        Login
      </Typography>
      <TextField
        label="Email"
        variant="outlined"
        fullWidth
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        sx={{
          marginBottom: "16px",
          "& .MuiOutlinedInput-root": {
            "& fieldset": {
              borderColor: "#555",
            },
            "&:hover fieldset": {
              borderColor: "#888",
            },
            "&.Mui-focused fieldset": {
              borderColor: "#fff",
            },
          },
          "& .MuiInputLabel-root": {
            color: "#aaa",
          },
          "& .MuiInputLabel-root.Mui-focused": {
            color: "#fff",
          },
          "& .MuiOutlinedInput-input": {
            color: "#fff",
          },
        }}
      />
      <TextField
        label="Password"
        type="password"
        variant="outlined"
        fullWidth
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        sx={{
          marginBottom: "16px",
          "& .MuiOutlinedInput-root": {
            "& fieldset": {
              borderColor: "#555",
            },
            "&:hover fieldset": {
              borderColor: "#888",
            },
            "&.Mui-focused fieldset": {
              borderColor: "#fff",
            },
          },
          "& .MuiInputLabel-root": {
            color: "#aaa",
          },
          "& .MuiInputLabel-root.Mui-focused": {
            color: "#fff",
          },
          "& .MuiOutlinedInput-input": {
            color: "#fff",
          },
        }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={handleLogin}
        sx={{ width: "100%", backgroundColor: "#487748" }}
      >
        Login
      </Button>
    </Box>
  );
};

export default Login;
