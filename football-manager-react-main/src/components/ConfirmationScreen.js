// src/components/ConfirmationScreen.js
import React from "react";
import { Box, Button, Typography } from "@mui/material";

const ConfirmationScreen = ({ managerName, selectedTeam, onConfirm, onPrevious }) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "20px",
        backgroundColor: "#2e2e2e",
        padding: "30px",
        borderRadius: "8px",
      }}
    >
      <Typography variant="h5" sx={{ color: "#fff" }}>
        Confirm Your Choices
      </Typography>
      <Typography variant="body1" sx={{ color: "#aaa" }}>
        Manager Name: <strong>{managerName}</strong>
      </Typography>
      <Typography variant="body1" sx={{ color: "#aaa" }}>
        Selected Team: <strong>{selectedTeam}</strong>
      </Typography>
      <Box sx={{ display: "flex", gap: "10px" }}>
        <Button variant="contained" onClick={onPrevious} sx={{ backgroundColor: "#444" }}>
          Back
        </Button>
        <Button variant="contained" color="primary" onClick={onConfirm} sx={{ backgroundColor: "#487748" }}>
          Start Game
        </Button>
      </Box>
    </Box>
  );
};

export default ConfirmationScreen;
