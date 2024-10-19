// src/components/FixturesList.js

import { Box, Card, Grid, Paper, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

const FixturesList = ({ matches, selectedMatchday, teamLogos }) => {
  const filteredMatches = selectedMatchday
    ? matches.filter((match) => match.round === selectedMatchday)
    : [];

  return (
    <Paper
      sx={{
        padding: 4,
        marginTop: 3,
        backgroundColor: "#f9f9f9",
        borderRadius: 3,
      }}
    >
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 4,
          flexWrap: "wrap",
        }}
      >
        <Typography
          variant="h4"
          sx={{
            fontWeight: "bold",
            color: "#37003c",
            textAlign: "center",
            flexGrow: 1,
            marginBottom: { xs: 2, sm: 0 },
          }}
        >
          Matchday Fixtures
        </Typography>
        {/* Removed the matchday selector from FixturesList.js */}
      </Box>

      {filteredMatches.length === 0 ? (
        <Typography variant="h6" align="center" sx={{ color: "#333" }}>
          No matches available for {selectedMatchday}.
        </Typography>
      ) : (
        <Grid container spacing={3} justifyContent="center">
          {filteredMatches.map((match, idx) => {
            const hasScore = match.score && match.score.ft;
            const displayScore = hasScore
              ? `${match.score.ft[0]} - ${match.score.ft[1]}`
              : `${match.time}`;

            return (
              <Grid item xs={12} sm={6} md={4} key={idx}>
                <Card
                  sx={{
                    display: "flex",
                    alignItems: "center",
                    padding: 3,
                    backgroundColor: "#ffffff",
                    borderRadius: 3,
                    boxShadow: "0 6px 15px rgba(0, 0, 0, 0.1)",
                    textAlign: "center",
                    transition: "transform 0.3s ease",
                    "&:hover": {
                      transform: "scale(1.05)",
                    },
                  }}
                >
                  {/* Team 1 */}
                  <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    justifyContent="center"
                    width="30%"
                  >
                    <Box
                      component="img"
                      src={teamLogos[match.team1]} // Correct usage
                      alt={match.team1}
                      sx={{
                        width: 70,
                        height: 70,
                        marginBottom: 1,
                        borderRadius: "10%",
                        objectFit: "contain",
                        border: "2px solid #ddd",
                      }}
                    />
                    <Typography
                      variant="body1"
                      sx={{
                        fontWeight: "bold",
                        fontSize: "1rem",
                        color: "#333",
                      }}
                    >
                      {match.team1}
                    </Typography>
                  </Box>

                  {/* Score or Time */}
                  <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    justifyContent="center"
                    width="40%"
                    sx={{
                      backgroundColor: hasScore ? "#4a148c" : "#e0e0e0",
                      color: hasScore ? "#fff" : "#000",
                      padding: 2,
                      borderRadius: 3,
                      boxShadow: "0 3px 6px rgba(0, 0, 0, 0.2)",
                      marginX: 1,
                    }}
                  >
                    <Typography
                      variant="h5"
                      sx={{
                        fontWeight: "bold",
                        fontSize: "1.8rem",
                        letterSpacing: "0.1rem",
                      }}
                    >
                      {displayScore}
                    </Typography>
                    {!hasScore && (
                      <Typography
                        variant="caption"
                        sx={{
                          color: "#444",
                          fontStyle: "italic",
                          marginTop: 0.5,
                        }}
                      >
                        Kick-off: {match.time}
                      </Typography>
                    )}
                  </Box>

                  {/* Team 2 */}
                  <Box
                    display="flex"
                    flexDirection="column"
                    alignItems="center"
                    justifyContent="center"
                    width="30%"
                  >
                    <Box
                      component="img"
                      src={teamLogos[match.team2]} // Correct usage
                      alt={match.team2}
                      sx={{
                        width: 70,
                        height: 70,
                        marginBottom: 1,
                        borderRadius: "10%",
                        objectFit: "contain",
                        border: "2px solid #ddd",
                      }}
                    />
                    <Typography
                      variant="body1"
                      sx={{
                        fontWeight: "bold",
                        fontSize: "1rem",
                        color: "#333",
                      }}
                    >
                      {match.team2}
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

FixturesList.propTypes = {
  matches: PropTypes.array.isRequired,
  selectedMatchday: PropTypes.string.isRequired,
  teamLogos: PropTypes.object.isRequired,
};

export default FixturesList;
