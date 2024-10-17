// FixturesList.js
import { Avatar, Box, Card, Grid, Paper, Typography } from "@mui/material";
import { format } from "date-fns";
import React from "react";

const FixturesList = ({ matches, selectedMatchday, teamLogos }) => {
  const filteredMatches = matches.filter(
    (match) => match.round === selectedMatchday
  );

  return (
    <Paper sx={{ padding: 2, marginTop: 3 }}>
      {filteredMatches.length === 0 ? (
        <Typography variant="h6" align="center">
          No matches available for {selectedMatchday}.
        </Typography>
      ) : (
        <Grid container spacing={2}>
          {filteredMatches.map((match, idx) => (
            <Grid item xs={12} md={6} key={idx}>
              <Card
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  padding: 2,
                  backgroundColor: "#f4f4f4",
                  borderRadius: 2,
                  boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
                }}
              >
                <Box
                  display="flex"
                  flexDirection="column"
                  alignItems="center"
                  textAlign="center"
                  width="25%"
                >
                  <Avatar
                    src={teamLogos[match.team1]}
                    alt={match.team1}
                    sx={{ width: 40, height: 40 }}
                  />
                  <Typography variant="body2" sx={{ marginTop: 1 }}>
                    {match.team1}
                  </Typography>
                </Box>
                <Box
                  display="flex"
                  flexDirection="column"
                  alignItems="center"
                  textAlign="center"
                  width="50%"
                >
                  <Typography variant="body2" sx={{ fontWeight: "bold" }}>
                    {format(new Date(match.date), "dd MMM yyyy")} {match.time}
                  </Typography>
                  <Typography variant="caption" sx={{ color: "#666" }}>
                    vs
                  </Typography>
                </Box>
                <Box
                  display="flex"
                  flexDirection="column"
                  alignItems="center"
                  textAlign="center"
                  width="25%"
                >
                  <Avatar
                    src={teamLogos[match.team2]}
                    alt={match.team2}
                    sx={{ width: 40, height: 40 }}
                  />
                  <Typography variant="body2" sx={{ marginTop: 1 }}>
                    {match.team2}
                  </Typography>
                </Box>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Paper>
  );
};

export default FixturesList;
