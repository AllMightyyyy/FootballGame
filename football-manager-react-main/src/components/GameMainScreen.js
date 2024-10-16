// GameMainScreen.js
import React from "react";
import { Box, Button, Typography, TextField, Container } from "@mui/material";
import GameMainBackground from '../GameMainScreen-bg.jpg';
import SignupIcons from '../signupicons.png';

const iconStyles = {
    width: '80px',
    height: '80px',
    backgroundImage: `url(${SignupIcons})`,
    backgroundSize: '100% auto',
  };

const iconPositions = [
    { backgroundPosition: '0 0' },     // First icon (shirt)
    { backgroundPosition: '0 -80px' }, // Second icon (lineup)
    { backgroundPosition: '0 -160px' }, // Third icon (tactics)
    { backgroundPosition: '0 -240px' }, // Fourth icon (handshake)
    { backgroundPosition: '0 -320px' }, // Fifth icon (stadium)
    { backgroundPosition: '0 -400px' }, // Sixth icon (play against friends)
  ];

const features = [
    {
      title: "Choose your favourite club",
      description:
        "You want to compete for the championship with a top club or like to play against relegation? Everything is possible. Your favourite club needs you!",
    },
    {
      title: "Determine your Line-up",
      description:
        "Classic wingers or two strikers? It’s your call. You decide the formation, you decide who plays. Every single match!",
    },
    {
      title: "Decide your Tactics",
      description:
        "Are you a fan of the passing game? Or do you prefer the counter-attack? As a tactical mastermind, you can perfect your team in all details!",
    },
    {
      title: "Strengthen your Squad",
      description:
        "Search for the best talents, negotiate with other clubs, and sell your dispensable players. Create the ideal team with the right transfer policy!",
    },
    {
      title: "Daily match",
      description:
        "Manage your club against the competition daily. Train your players regularly and play friendlies to test your tactics!",
    },
    {
      title: "Play against friends",
      description:
        "Challenge friends, join their leagues, and compete as if your life depends on it. May the best manager win!",
    },
  ];

const GameMainScreen = () => {
  return (
    <Box
      sx={{
        backgroundImage: `url(${GameMainBackground})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
        color: "#fff",
        textAlign: "center",
        position: "relative",
        overflow: "hidden",
      }}
    >
      {/* Glass effect overlay */}
      <Box
        sx={{
          position: "absolute",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0, 0, 0, 0.5)",
          backdropFilter: "blur(10px)",
          zIndex: -1,
        }}
      />

      {/* Welcome Section */}
      <Box
        sx={{
          backgroundColor: "rgba(30, 30, 30, 0.8)",
          padding: "40px",
          borderRadius: "12px",
          boxShadow: "0 8px 32px rgba(0, 0, 0, 0.37)",
          maxWidth: "400px",
          width: "100%",
          marginBottom: "24px",
        }}
      >
        <Typography
          variant="h3"
          sx={{
            color: "#487748",
            marginBottom: "16px",
            fontSize: { xs: "24px", md: "36px" },
          }}
        >
          Sign up and become the best football manager!
        </Typography>
        <Box
          sx={{
            backgroundColor: "#1e1e1e",
            borderRadius: "8px",
            padding: "20px",
          }}
        >
          <Typography variant="h5" sx={{ color: "#487748", marginBottom: "8px" }}>
            Choose Manager Name
          </Typography>
          <TextField
            placeholder="Enter manager name"
            fullWidth
            sx={{
              backgroundColor: "#2e2e2e",
              borderRadius: "4px",
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
              marginBottom: "16px",
            }}
          />
          <Button
            variant="contained"
            sx={{
              backgroundColor: "#487748",
              color: "#121212",
              width: "100%",
              padding: "10px",
              fontWeight: "bold",
            }}
          >
            Create Account
          </Button>
        </Box>
        <Typography
          variant="body2"
          sx={{
            color: "#aaa",
            marginTop: "16px",
          }}
        >
          Already have an account?{" "}
          <Button
            sx={{ color: "#487748", fontWeight: "bold" }}
          >
            Log in
          </Button>
        </Typography>
      </Box>

      {/* About the Game Section */}
      <Box
        sx={{ padding: "40px", backgroundColor: "#1e1e1e", marginTop: "50px" }}
      >
        <Container maxWidth="lg">
          <Typography
            variant="h4"
            sx={{
              color: "#487748",
              textAlign: "center",
              marginBottom: "32px",
            }}
          >
            What's Our Football Manager?
          </Typography>
          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: { xs: "1fr", md: "repeat(3, 1fr)" },
              gap: "24px",
              textAlign: "center",
            }}
          >
            {features.map((feature, index) => (
              <Box
                key={index}
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  padding: "20px",
                  borderRadius: "8px",
                  backgroundColor: "#2e2e2e",
                  boxShadow: "0 4px 16px rgba(0, 0, 0, 0.2)",
                }}
              >
                <Box
                  sx={{
                    ...iconStyles,
                    ...iconPositions[index],
                    marginBottom: "16px",
                  }}
                />
                <Typography variant="h6" sx={{ color: "#487748", marginBottom: "8px" }}>
                  {feature.title}
                </Typography>
                <Typography variant="body2" sx={{ color: "#aaa" }}>
                  {feature.description}
                </Typography>
              </Box>
            ))}
          </Box>
          <Box sx={{ textAlign: "center", marginTop: "40px" }}>
            <Button variant="contained" sx={{ backgroundColor: "#487748", color: "#121212" }}>
              Sign up now
            </Button>
          </Box>
        </Container>
      </Box>
    </Box>
  );
};

export default GameMainScreen;
