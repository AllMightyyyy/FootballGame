// StandingsTable.js
import {
    Avatar,
    Box,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";
import React from "react";
  
  const StandingsTable = ({ standings, teamLogos }) => {
    const getPositionColor = (position) => {
      if (position <= 4) return "#37003c";
      if (position === 5) return "#2d6a4f";
      if (position >= 18) return "#e63946";
      return "#00000000";
    };
  
    return (
      <Paper sx={{ marginTop: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Position</TableCell>
              <TableCell>Team</TableCell>
              <TableCell>Played</TableCell>
              <TableCell>Won</TableCell>
              <TableCell>Drawn</TableCell>
              <TableCell>Lost</TableCell>
              <TableCell>GF</TableCell>
              <TableCell>GA</TableCell>
              <TableCell>GD</TableCell>
              <TableCell>Points</TableCell>
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
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    );
  };
  
  export default StandingsTable;
  