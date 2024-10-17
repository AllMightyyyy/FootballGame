import { Avatar, Box, Card, Grid, Paper, Typography } from "@mui/material";
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
        <Grid container spacing={1}>
          {filteredMatches.map((match, idx) => {
            // Check if the score is available
            const hasScore = match.score && match.score.ft;
            const displayScore = hasScore
              ? `${match.score.ft[0]} - ${match.score.ft[1]}`
              : `${match.time}`;

            return (
              <Grid item xs={12} key={idx}>
                <Card
                  sx={{
                    display: "flex",
                    alignItems: "center",
                    padding: 1,
                    backgroundColor: "#fff",
                    borderRadius: 1,
                    boxShadow: "0 2px 6px rgba(0, 0, 0, 0.15)",
                    textAlign: "center",
                    maxWidth: 350,
                    margin: "0 auto",
                    marginBottom: 1,
                  }}
                >
                  {/* Team 1 */}
                  <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    justifyContent="center"
                    width="30%"
                    padding={1}
                  >
                    <Avatar
                      src={teamLogos[match.team1]}
                      alt={match.team1}
                      sx={{ width: 30, height: 30 }}
                    />
                    <Typography
                      variant="caption"
                      sx={{ fontWeight: "bold", marginTop: 0.5 }}
                    >
                      {match.team1
                        .split(" ")
                        .map((word) => word[0])
                        .join("")}
                    </Typography>
                  </Box>

                  {/* Score */}
                  <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    justifyContent="center"
                    width="40%"
                  >
                    <Typography
                      variant="h6"
                      sx={{
                        fontWeight: "bold",
                        color: hasScore ? "#37003c" : "#666",
                        fontSize: "1rem",
                      }}
                    >
                      {displayScore}
                    </Typography>
                  </Box>

                  {/* Team 2 */}
                  <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    justifyContent="center"
                    width="30%"
                    padding={1}
                  >
                    <Avatar
                      src={teamLogos[match.team2]}
                      alt={match.team2}
                      sx={{ width: 30, height: 30 }}
                    />
                    <Typography
                      variant="caption"
                      sx={{ fontWeight: "bold", marginTop: 0.5 }}
                    >
                      {match.team2
                        .split(" ")
                        .map((word) => word[0])
                        .join("")}
                    </Typography>
                  </Box>
                </Card>
              </Grid>
            );
          })}
        </Grid>
      )}
    </Paper>
  );
};

export default FixturesList;
