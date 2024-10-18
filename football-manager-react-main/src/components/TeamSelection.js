// src/components/TeamSelection.js

import {
  Box,
  Button,
  Card,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Typography,
} from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import api from "../api";
import { AuthContext } from "../contexts/AuthContext";
import teamLogos from "./utils/teamLogos"; // Ensure this path is correct

const TeamSelection = ({
  selectedLeague,
  setSelectedLeague,
  onNext,
  onPrevious,
  selectedTeam,
  setSelectedTeam,
}) => {
  const { auth } = useContext(AuthContext);
  const [leagues, setLeagues] = useState([]);
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch leagues
    const fetchLeagues = async () => {
      try {
        const response = await api.get("/teams/leagues");
        setLeagues(response.data);
      } catch (err) {
        setError("Failed to fetch leagues.");
        console.error(err);
      }
    };

    fetchLeagues();
  }, []);

  useEffect(() => {
    if (selectedLeague) {
      // Fetch teams for the selected league
      const fetchTeams = async () => {
        setLoading(true);
        try {
          const response = await api.get(`/teams/${selectedLeague}`);
          setTeams(response.data);
        } catch (err) {
          setError("Failed to fetch teams.");
          console.error(err);
        } finally {
          setLoading(false);
        }
      };

      fetchTeams();
    }
  }, [selectedLeague]);

  const handleLeagueChange = (event) => {
    setSelectedLeague(event.target.value);
    setSelectedTeam(null); // Reset selected team when league changes
  };

  const handleTeamSelect = (teamName) => {
    setSelectedTeam(teamName);
  };

  const handleConfirmSelection = () => {
    onNext(); // Proceed to the next step (e.g., confirmation)
  };

  return (
    <Box
      sx={{
        backgroundColor: "#1e1e1e",
        padding: "20px",
        borderRadius: "8px",
        maxWidth: "800px",
        width: "100%",
        color: "#fff",
      }}
    >
      <Typography variant="h5" gutterBottom>
        Select Your Team
      </Typography>

      {/* League Selector */}
      <FormControl fullWidth sx={{ marginBottom: "20px" }}>
        <InputLabel id="league-select-label" sx={{ color: "#fff" }}>
          League
        </InputLabel>
        <Select
          labelId="league-select-label"
          value={selectedLeague || ""}
          label="League"
          onChange={handleLeagueChange}
          sx={{
            backgroundColor: "#2e2e2e",
            "& .MuiSelect-select": { color: "#fff" },
            "& .MuiInputLabel-root": { color: "#fff" },
            "& .MuiOutlinedInput-notchedOutline": { borderColor: "#555" },
            "&:hover .MuiOutlinedInput-notchedOutline": { borderColor: "#888" },
            "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
              borderColor: "#fff",
            },
          }}
        >
          {leagues.map((league) => (
            <MenuItem key={league} value={league}>
              {league}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      {/* Teams Grid */}
      {loading ? (
        <Typography>Loading teams...</Typography>
      ) : error ? (
        <Typography color="error">{error}</Typography>
      ) : (
        <Grid container spacing={2}>
          {teams.map((team) => (
            <Grid item xs={6} sm={4} md={3} key={team.id}>
              <Card
                onClick={() => handleTeamSelect(team.name)}
                sx={{
                  cursor: "pointer",
                  backgroundColor:
                    selectedTeam === team.name ? "#487748" : "#2e2e2e",
                  padding: "10px",
                  textAlign: "center",
                  borderRadius: "8px",
                  transition: "background-color 0.3s",
                  "&:hover": {
                    backgroundColor: "#487748",
                  },
                }}
              >
                <img
                  src={
                    teamLogos[selectedLeague]?.logos[team.name] ||
                    "/defaultTeamLogo.png"
                  }
                  alt={team.name}
                  style={{
                    width: "60px",
                    height: "60px",
                    objectFit: "contain",
                    marginBottom: "10px",
                  }}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = "/defaultTeamLogo.png";
                  }}
                />
                <Typography variant="body1">{team.name}</Typography>
                {team.isOccupied && (
                  <Typography variant="caption" color="error">
                    Occupied
                  </Typography>
                )}
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Navigation Buttons */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          marginTop: "20px",
        }}
      >
        <Button
          variant="outlined"
          onClick={onPrevious}
          sx={{ color: "#fff", borderColor: "#487748" }}
        >
          Back
        </Button>
        <Button
          variant="contained"
          onClick={handleConfirmSelection}
          disabled={!selectedTeam}
          sx={{ backgroundColor: "#487748", color: "#fff" }}
        >
          Confirm Team
        </Button>
      </Box>
    </Box>
  );
};

export default TeamSelection;
