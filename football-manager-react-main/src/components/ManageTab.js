// src/components/ManageTab.js

import React, { useContext } from "react";
import { Box, Button, Typography } from "@mui/material";
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

const ManageTab = () => {
  const { auth } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleAssignTeam = () => {
    navigate("/onboarding"); // Navigate to the onboarding process for team assignment
  };

  return (
    <Box sx={{ padding: 3 }}>
      {auth.team ? (
        <Box sx={{ textAlign: "center" }}>
          <Typography variant="h6" color="success.main">
            You have already assigned a team: <strong>{auth.team.name}</strong>
          </Typography>
          {/* Placeholder for future functionalities */}
          <Box sx={{ marginTop: 2 }}>
            <Typography variant="body1" color="textSecondary">
              {/* Placeholder content */}
              Team management features will be available soon.
            </Typography>
          </Box>
        </Box>
      ) : (
        <Box sx={{ textAlign: "center" }}>
          <Typography variant="h6" color="textPrimary">
            You have not assigned a team yet.
          </Typography>
          <Button
            variant="contained"
            color="primary"
            sx={{ marginTop: 2 }}
            onClick={handleAssignTeam}
          >
            Assign a Team
          </Button>
        </Box>
      )}
    </Box>
  );
};

export default ManageTab;
