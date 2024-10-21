import {
  Box,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import PropTypes from "prop-types";
import React, { useEffect, useState } from "react";
import { getLeagueQualifications, getNextOpponent, getTeamForm } from "../api";

const StandingsTable = ({ standings, teamLogos, selectedLeague }) => {
  const [teamForms, setTeamForms] = useState({});
  const [nextOpponents, setNextOpponents] = useState({});
  const [qualifications, setQualifications] = useState({});

  useEffect(() => {
    if (!selectedLeague || standings.length === 0) return;

    standings.forEach((team) => {
      getTeamForm(team.teamName).then((form) => {
        setTeamForms((prevForms) => ({
          ...prevForms,
          [team.teamName]: form,
        }));
      });

      getNextOpponent(team.teamName).then((nextOpponent) => {
        setNextOpponents((prevOpponents) => ({
          ...prevOpponents,
          [team.teamName]: nextOpponent,
        }));
      });
    });

    // Fetch league qualification positions
    getLeagueQualifications(selectedLeague)
      .then((data) => {
        setQualifications(data);
      })
      .catch((error) => {
        console.error("Error fetching qualifications:", error);
      });
  }, [standings, selectedLeague]);

  // Render Form helper function
  const renderForm = (form) => {
    return form.map((result, index) => {
      let color = "grey"; // default for draw
      if (result === "W") color = "#43A047"; // green for win
      if (result === "L") color = "#E53935"; // red for loss

      return (
        <Box
          key={index}
          sx={{
            width: 16,
            height: 16,
            backgroundColor: color,
            borderRadius: "50%",
            margin: "0 3px",
            border: "1px solid #ffffff",
          }}
        />
      );
    });
  };

  // Determine the left border color based on qualifications
  const getQualificationBorderColor = (position) => {
    // Convert position to integer (indexed from 1)
    const currentPosition = position + 1;

    if (
      qualifications["Champions League"]?.some(
        (q) => parseInt(q) === currentPosition
      )
    ) {
      return "#003399"; // Blue for Champions League
    } else if (
      qualifications["Europa League"]?.some(
        (q) => parseInt(q) === currentPosition
      )
    ) {
      return "#FF7F00"; // Orange for Europa League
    } else if (
      qualifications["Conference League"]?.some(
        (q) => parseInt(q) === currentPosition
      )
    ) {
      return "#00CC00"; // Green for Conference League
    } else if (
      qualifications["Relegation"]?.some((q) => parseInt(q) === currentPosition)
    ) {
      return "#FF3333"; // Red for Relegation
    }
    return null;
  };

  return (
    <Box sx={{ padding: "20px", backgroundColor: "#f9f9f9" }}>
      {/* Header for League */}
      <Box sx={{ display: "flex", alignItems: "center", marginBottom: 2 }}>
        <Typography variant="h4" sx={{ fontWeight: "bold", marginRight: 2 }}>
          Premier League Standings
        </Typography>
        <Box
          component="img"
          src={teamLogos?.leagueLogo || "/assets/img/defaultLeagueLogo.png"}
          alt="League Logo"
          sx={{ width: 60, height: 60 }}
        />
      </Box>

      {/* Table layout */}
      <TableContainer component={Paper} sx={{ borderRadius: 4 }}>
        <Table>
          <TableHead>
            <TableRow sx={{ backgroundColor: "#efefef" }}>
              <TableCell sx={{ fontWeight: "bold" }}>Position</TableCell>
              <TableCell sx={{ fontWeight: "bold" }}>Club</TableCell>
              <TableCell>Played</TableCell>
              <TableCell>Won</TableCell>
              <TableCell>Drawn</TableCell>
              <TableCell>Lost</TableCell>
              <TableCell>GF</TableCell>
              <TableCell>GA</TableCell>
              <TableCell>GD</TableCell>
              <TableCell>Form</TableCell>
              <TableCell>Next</TableCell>
              <TableCell>Points</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {standings.map((team, index) => (
              <TableRow
                key={team.teamName}
                sx={{
                  "&:nth-of-type(odd)": { backgroundColor: "#fafafa" },
                  borderLeft: `8px solid ${
                    getQualificationBorderColor(index) || "transparent"
                  }`,
                }}
              >
                <TableCell sx={{ fontWeight: "bold" }}>{index + 1}</TableCell>
                <TableCell>
                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Box
                      component="img"
                      src={
                        teamLogos[team.teamName] ||
                        "/assets/img/defaultTeamLogo.png"
                      }
                      alt={team.teamName}
                      sx={{ width: 40, height: 40, marginRight: 2 }}
                    />
                    <Typography variant="body1" sx={{ fontWeight: "bold" }}>
                      {team.teamName}
                    </Typography>
                  </Box>
                </TableCell>
                <TableCell>{team.played}</TableCell>
                <TableCell>{team.win}</TableCell>
                <TableCell>{team.draw}</TableCell>
                <TableCell>{team.lose}</TableCell>
                <TableCell>{team.goalsFor}</TableCell>
                <TableCell>{team.goalsAgainst}</TableCell>
                <TableCell>{team.goalDifference}</TableCell>
                <TableCell>
                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    {renderForm(teamForms[team.teamName] || [])}
                  </Box>
                </TableCell>
                <TableCell>
                  {nextOpponents[team.teamName] ? (
                    <Box sx={{ display: "flex", alignItems: "center" }}>
                      <Box
                        component="img"
                        src={
                          teamLogos[nextOpponents[team.teamName]] ||
                          "/assets/img/defaultTeamLogo.png"
                        }
                        alt={nextOpponents[team.teamName]}
                        sx={{ width: 30, height: 30, marginRight: 2 }}
                      />
                      <Typography variant="body2">
                        {nextOpponents[team.teamName]}
                      </Typography>
                    </Box>
                  ) : (
                    "N/A"
                  )}
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>{team.points}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Legend for League Qualification */}
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          marginTop: 3,
          paddingTop: 2,
          borderTop: "1px solid #ccc",
        }}
      >
        <Typography
          variant="body1"
          sx={{ fontWeight: "bold", marginRight: 2, color: "black" }}
        >
          Qualification Keys :
        </Typography>
        <Stack direction="row" spacing={4}>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <Box
              sx={{ width: 8, height: 20, backgroundColor: "#003399", mr: 1 }}
            />
            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", color: "#003399" }}
            >
              Champions League qualification
            </Typography>
          </Box>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <Box
              sx={{ width: 8, height: 20, backgroundColor: "#FF7F00", mr: 1 }}
            />
            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", color: "#FF7F00" }}
            >
              Europa League Qualification
            </Typography>
          </Box>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <Box
              sx={{ width: 8, height: 20, backgroundColor: "#00CC00", mr: 1 }}
            />
            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", color: "#00CC00" }}
            >
              Conference League qualification
            </Typography>
          </Box>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <Box
              sx={{ width: 8, height: 20, backgroundColor: "#FF3333", mr: 1 }}
            />
            <Typography
              variant="body2"
              sx={{ fontWeight: "bold", color: "#FF3333" }}
            >
              Relegation
            </Typography>
          </Box>
        </Stack>
      </Box>
    </Box>
  );
};

StandingsTable.propTypes = {
  standings: PropTypes.array.isRequired,
  teamLogos: PropTypes.object.isRequired,
  selectedLeague: PropTypes.string.isRequired,
};

export default StandingsTable;
