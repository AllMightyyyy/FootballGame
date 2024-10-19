// src/components/StandingsTable.js

import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Avatar,
  Typography,
} from "@mui/material";
import PropTypes from "prop-types";

const StandingsTable = ({ standings, teamLogos }) => {
  return (
    <TableContainer component={Paper} sx={{ marginTop: 3 }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Position</TableCell>
            <TableCell>Team</TableCell>
            <TableCell>Played</TableCell>
            <TableCell>Win</TableCell>
            <TableCell>Draw</TableCell>
            <TableCell>Lose</TableCell>
            <TableCell>GF</TableCell>
            <TableCell>GA</TableCell>
            <TableCell>GD</TableCell>
            <TableCell>Points</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {standings.map((team, index) => (
            <TableRow key={team.teamName}>
              <TableCell>{index + 1}</TableCell>
              <TableCell>
                <Avatar
                  src={teamLogos[team.teamName]}
                  alt={team.teamName}
                  sx={{ marginRight: 1, verticalAlign: "middle" }}
                />
                <Typography variant="body1" component="span">
                  {team.teamName}
                </Typography>
              </TableCell>
              <TableCell>{team.played}</TableCell>
              <TableCell>{team.win}</TableCell>
              <TableCell>{team.draw}</TableCell>
              <TableCell>{team.lose}</TableCell>
              <TableCell>{team.goalsFor}</TableCell>
              <TableCell>{team.goalsAgainst}</TableCell>
              <TableCell>{team.goalDifference}</TableCell>
              <TableCell>{team.points}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

StandingsTable.propTypes = {
  standings: PropTypes.array.isRequired,
  teamLogos: PropTypes.object.isRequired,
};

export default StandingsTable;
