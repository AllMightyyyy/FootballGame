// src/components/ConfirmationScreen.js

import { Avatar, Box, Button, Typography } from "@mui/material";
import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { AuthContext } from "../contexts/AuthContext";
import teamLogos from "./utils/teamLogos";

const ConfirmationScreen = ({
  managerName,
  selectedTeam,
  onConfirm,
  onPrevious,
  selectedLeague,
}) => {
  const { auth, updateTeam } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleConfirm = async () => {
    try {
      // Assign team to user
      const response = await api.post("/teams/assign", {
        league: selectedLeague,
        teamName: selectedTeam,
      });

      alert(response.data.message);

      // Fetch the assigned team
      const teamResponse = await api.get("/teams/my");
      if (teamResponse.data.team) {
        updateTeam(teamResponse.data.team);
      }

      // Finalize onboarding and navigate to the main game interface
      navigate("/");
    } catch (error) {
      console.error("Error assigning team:", error);
      alert(error.response?.data?.message || "Failed to assign team.");
    }
  };

  return (
    <Box
      sx={{
        width: "100%",
        maxWidth: "600px",
        backgroundColor: "#2e2e2e",
        padding: "30px",
        borderRadius: "8px",
        textAlign: "center",
      }}
    >
      {/* League Information */}
      <Box display="flex" flexDirection="column" alignItems="center" mb={3}>
        <Avatar
          src={teamLogos[selectedLeague]?.leagueLogo}
          alt="League Logo"
          sx={{ width: 80, height: 80, marginBottom: 2 }}
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = require("../../src/assets/img/defaultLeague.jpg");
          }}
        />
        <Typography variant="h5" sx={{ color: "#fff", fontWeight: "bold" }}>
          {teamLogos[selectedLeague]?.leagueName || "League"}
        </Typography>
      </Box>

      {/* Confirmation Details */}
      <Typography variant="h5" sx={{ color: "#fff", marginBottom: "20px" }}>
        Confirm Your Details
      </Typography>
      <Typography variant="body1" sx={{ color: "#aaa", marginBottom: "10px" }}>
        <strong>Manager Name:</strong> {managerName || auth.user.username}
      </Typography>
      <Typography variant="body1" sx={{ color: "#aaa", marginBottom: "30px" }}>
        <strong>Team:</strong> {auth.team ? auth.team.name : selectedTeam}
      </Typography>
      <Typography variant="body1" sx={{ color: "#aaa", marginBottom: "10px" }}>
        <strong>League:</strong>{" "}
        {teamLogos[selectedLeague]?.leagueName || "N/A"}
      </Typography>

      <Box sx={{ display: "flex", justifyContent: "space-between" }}>
        <Button
          variant="outlined"
          onClick={onPrevious}
          sx={{ color: "#fff", borderColor: "#487748" }}
        >
          Back
        </Button>
        <Button
          variant="contained"
          onClick={handleConfirm}
          sx={{ backgroundColor: "#487748", color: "#fff" }}
        >
          Confirm and Start Game
        </Button>
      </Box>
    </Box>
  );
};

export default ConfirmationScreen;
