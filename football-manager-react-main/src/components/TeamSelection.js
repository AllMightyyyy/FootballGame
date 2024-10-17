// src/components/TeamSelection.js

import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Typography,
  Grid,
  Avatar,
  Paper,
  CircularProgress,
  Alert,
} from "@mui/material";
import PropTypes from "prop-types";
import api from "../api";
import teamLogos from "./utils/teamLogos"; // Ensure correct path

const TeamSelection = ({
  selectedTeam,
  onTeamSelect,
  onNext,
  onPrevious,
  selectedLeague,
  setSelectedLeague,
}) => {
  const [teams, setTeams] = useState([]);
  const [loadingTeams, setLoadingTeams] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTeams = async () => {
      setLoadingTeams(true);
      setError(null);
      try {
        const response = await api.get(`/teams/${selectedLeague}`);
        setTeams(response.data); // Assuming response.data is an array of teams
      } catch (err) {
        console.error("Error fetching teams:", err);
        setError("Failed to fetch teams. Please try again.");
      } finally {
        setLoadingTeams(false);
      }
    };

    fetchTeams();
  }, [selectedLeague]);

  const handleLeagueChange = (event) => {
    setSelectedLeague(event.target.value);
    onTeamSelect(null); // Reset selected team when league changes
  };

  const handleTeamSelection = (teamName) => {
    onTeamSelect(teamName);
  };

  return (
    <Paper
      sx={{
        padding: 4,
        backgroundColor: "#2e2e2e",
        borderRadius: 3,
        width: "100%",
        maxWidth: "600px",
      }}
    >
      <Typography variant="h5" sx={{ color: "#fff", marginBottom: 3 }}>
        Select Your Team
      </Typography>

      {/* League Selector */}
      <FormControl fullWidth sx={{ marginBottom: 3 }}>
        <InputLabel id="league-select-label" sx={{ color: "#fff" }}>
          Select League
        </InputLabel>
        <Select
          labelId="league-select-label"
          value={selectedLeague}
          onChange={handleLeagueChange}
          label="Select League"
          sx={{
            color: "#fff",
            ".MuiOutlinedInput-notchedOutline": {
              borderColor: "#555",
            },
            "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
              borderColor: "#487748",
            },
            "&:hover .MuiOutlinedInput-notchedOutline": {
              borderColor: "#888",
            },
            ".MuiSvgIcon-root ": {
              fill: "white !important",
            },
          }}
        >
          {Object.entries(teamLogos).map(([leagueCode, leagueData]) => (
            <MenuItem key={leagueCode} value={leagueCode}>
              <Box display="flex" alignItems="center" gap={1}>
                <Avatar
                  src={leagueData.leagueLogo}
                  alt={leagueData.leagueName}
                  sx={{ width: 30, height: 30 }}
                />
                {leagueData.leagueName}
              </Box>
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      {/* Display Loading Indicator */}
      {loadingTeams && (
        <Box display="flex" justifyContent="center" my={2}>
          <CircularProgress />
        </Box>
      )}

      {/* Display Error Message */}
      {error && (
        <Alert severity="error" sx={{ marginBottom: 2 }}>
          {error}
        </Alert>
      )}

      {/* Teams Grid */}
      {!loadingTeams && !error && (
        <Grid container spacing={2}>
          {teams.map((team) => (
            <Grid item xs={6} sm={4} key={team.id}>
              <Button
                variant={selectedTeam === team.name ? "contained" : "outlined"}
                fullWidth
                onClick={() => handleTeamSelection(team.name)}
                sx={{
                  backgroundColor:
                    selectedTeam === team.name ? "#487748" : "#1e1e1e",
                  borderColor: "#555",
                  color: "#fff",
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  padding: 2,
                  "&:hover": {
                    backgroundColor:
                      selectedTeam === team.name ? "#3e6e3c" : "#2e2e2e",
                  },
                }}
              >
                <Avatar
                  src={teamLogos[selectedLeague]?.logos[team.name]}
                  alt={team.name}
                  sx={{ width: 120, height: 120, marginBottom: 1 }}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = require("../../src/assets/img/defaultLeague.jpg"); // Provide a default image
                  }}
                />
                <Typography variant="body1">{team.name}</Typography>
                {team.isOccupied && (
                  <Typography variant="caption" color="error">
                    Occupied
                  </Typography>
                )}
              </Button>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Navigation Buttons */}
      <Box display="flex" justifyContent="space-between" mt={4}>
        <Button
          variant="outlined"
          onClick={onPrevious}
          sx={{
            color: "#fff",
            borderColor: "#487748",
            "&:hover": {
              borderColor: "#487748",
              backgroundColor: "#48774820",
            },
          }}
        >
          Back
        </Button>
        <Button
          variant="contained"
          onClick={onNext}
          disabled={!selectedTeam}
          sx={{
            backgroundColor: "#487748",
            color: "#fff",
            "&:hover": {
              backgroundColor: "#3e6e3c",
            },
          }}
        >
          Next
        </Button>
      </Box>
    </Paper>
  );
};

// Define PropTypes for better type checking
TeamSelection.propTypes = {
  selectedTeam: PropTypes.string,
  onTeamSelect: PropTypes.func.isRequired,
  onNext: PropTypes.func.isRequired,
  onPrevious: PropTypes.func.isRequired,
  selectedLeague: PropTypes.string.isRequired,
  setSelectedLeague: PropTypes.func.isRequired,
};

export default TeamSelection;
