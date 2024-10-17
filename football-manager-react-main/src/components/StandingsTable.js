// StandingsTable.js
import {
  Avatar,
  Box,
  Chip,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

const StandingsTable = ({ standings, teamLogos, matches }) => {
  // Function to determine the position color
  const getPositionColor = (position) => {
    if (position <= 4) return "#37003c"; // Top 4 - Champions League
    if (position === 5) return "#2d6a4f"; // Europa League
    if (position >= 18) return "#e63946"; // Relegation zone
    return "#00000000"; // Transparent for others
  };

  // Function to get the last 5 results for each team
  const getForm = (teamName) => {
    const results = matches
      .filter(
        (match) =>
          (match.team1 === teamName || match.team2 === teamName) &&
          match.score?.ft // Ensure the match has a full-time score
      )
      .slice(-5) // Get the last 5 results
      .map((match) => {
        const isHomeTeam = match.team1 === teamName;
        const [team1Goals, team2Goals] = match.score.ft;
        if (team1Goals > team2Goals) return isHomeTeam ? "W" : "L";
        if (team1Goals < team2Goals) return isHomeTeam ? "L" : "W";
        return "D"; // Draw
      });
    return results;
  };

  // Function to get the next opponent for each team
  const getNextOpponent = (teamName) => {
    const nextMatch = matches.find(
      (match) =>
        (match.team1 === teamName || match.team2 === teamName) &&
        !match.score?.ft // Ensure the match has not been played yet
    );
    return nextMatch
      ? nextMatch.team1 === teamName
        ? nextMatch.team2
        : nextMatch.team1
      : "N/A"; // If no upcoming match found
  };

  return (
    <Paper sx={{ marginTop: 3 }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Position</TableCell>
            <TableCell>Club</TableCell>
            <TableCell>Played</TableCell>
            <TableCell>Won</TableCell>
            <TableCell>Drawn</TableCell>
            <TableCell>Lost</TableCell>
            <TableCell>GF</TableCell>
            <TableCell>GA</TableCell>
            <TableCell>GD</TableCell>
            <TableCell>Points</TableCell>
            <TableCell>Form</TableCell>
            <TableCell>Next</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {standings.map((team, idx) => (
            <TableRow
              key={team.name}
              sx={{ borderLeft: `4px solid ${getPositionColor(idx + 1)}` }}
            >
              <TableCell>{idx + 1}</TableCell>
              <TableCell>
                <Box display="flex" alignItems="center">
                  <Avatar
                    src={teamLogos[team.name]}
                    alt={team.name}
                    sx={{ width: 24, height: 24, marginRight: 1 }}
                  />
                  <Typography component="span">{team.name}</Typography>
                </Box>
              </TableCell>
              <TableCell>{team.played}</TableCell>
              <TableCell>{team.win}</TableCell>
              <TableCell>{team.draw}</TableCell>
              <TableCell>{team.lose}</TableCell>
              <TableCell>{team.goalsFor}</TableCell>
              <TableCell>{team.goalsAgainst}</TableCell>
              <TableCell>{team.goalDifference}</TableCell>
              <TableCell>{team.points}</TableCell>
              <TableCell>
                <Box display="flex">
                  {getForm(team.name).map((result, i) => (
                    <Chip
                      key={i}
                      label={result}
                      sx={{
                        backgroundColor:
                          result === "W"
                            ? "green"
                            : result === "L"
                            ? "red"
                            : "gray",
                        color: "#fff",
                        marginRight: 0.5,
                      }}
                      size="small"
                    />
                  ))}
                </Box>
              </TableCell>
              <TableCell>
                <Avatar
                  src={teamLogos[getNextOpponent(team.name)]}
                  alt={getNextOpponent(team.name)}
                  sx={{ width: 24, height: 24, marginRight: 1 }}
                />
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
};

// PropTypes for the component
StandingsTable.propTypes = {
  standings: PropTypes.array.isRequired,
  teamLogos: PropTypes.object.isRequired,
  matches: PropTypes.array.isRequired,
};

export default StandingsTable;
