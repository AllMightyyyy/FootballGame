// src/components/LiveStandings.js

import {
  Alert,
  Box,
  CircularProgress,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Tab,
  Tabs,
} from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import { api } from "../api/index";
import { LeagueContext } from "../contexts/LeagueContext";
import FixturesList from "./FixturesList";
import StandingsTable from "./StandingsTable";
import { leagueNameMap } from "./utils/leagueMapping";
import teamLogos from "./utils/teamLogos";

const LiveStandings = () => {
  const [standings, setStandings] = useState([]);
  const [matches, setMatches] = useState([]);
  const [selectedLeague, setSelectedLeague] = useState("en.1"); // Default league code
  const [selectedTab, setSelectedTab] = useState(0);
  const [matchdays, setMatchdays] = useState([]);
  const [selectedMatchday, setSelectedMatchday] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const leagues = useContext(LeagueContext);

  const standardLeagueName = leagueNameMap[selectedLeague]; // "English Premier League 2024/25"

  useEffect(() => {
    const getStandings = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await api.get(`/football/standings/${selectedLeague}`);
        const data = response.data;

        // Destructure matches and standings from the response
        const { matches: fetchedMatches, standings: fetchedStandings } = data;

        setMatches(fetchedMatches);
        setStandings(fetchedStandings);

        console.log("Fetched Matches:", fetchedMatches);
        console.log("Fetched Standings:", fetchedStandings);

        const matchdaysData = extractMatchdays(fetchedMatches);
        setMatchdays(matchdaysData);
        setSelectedMatchday(matchdaysData[0] || "");
      } catch (err) {
        console.error(err);
        setError("Error fetching data. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    getStandings();
  }, [selectedLeague]);

  const extractMatchdays = (matches) => {
    const rounds = matches.map((match) => match.round);
    const uniqueRounds = [...new Set(rounds)];
    uniqueRounds.sort(
      (a, b) => parseInt(a.replace(/\D/g, "")) - parseInt(b.replace(/\D/g, ""))
    );
    return uniqueRounds;
  };

  const handleTabChange = (event, newValue) => {
    setSelectedTab(newValue);
  };

  const handleLeagueChange = (event) => {
    console.log("New selected league:", event.target.value); // Debug log
    setSelectedLeague(event.target.value);
  };

  return (
    <Container maxWidth="xl" sx={{ backgroundColor: "#f8f9fa", padding: 3 }}>
      {/* Header with League Selector */}
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        mb={2}
      >
        {/* League Title with Logo */}
        <Box display="flex" alignItems="center" gap={1}>
          <Box
            component="img"
            src={teamLogos[selectedLeague]?.leagueLogo}
            alt={`${teamLogos[selectedLeague]?.leagueName} Logo`}
            sx={{
              width: 140,
              height: 140,
              objectFit: "contain",
              backgroundColor: "white",
            }}
          />
        </Box>

        {/* Enhanced League Selector */}
        <FormControl variant="outlined" size="small" sx={{ minWidth: 120 }}>
          <InputLabel id="league-select-label">League</InputLabel>
          <Select
            labelId="league-select-label"
            value={selectedLeague}
            onChange={handleLeagueChange}
            label="League"
            sx={{
              backgroundColor: "#ffffff",
              borderRadius: 1,
              "& .MuiSelect-select": {
                padding: "6px 8px",
              },
            }}
          >
            {leagues.map((league) => (
              <MenuItem key={league.code} value={league.code}>
                {league.name} {league.season}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>

      {/* Tabs for Standings and Matchday */}
      <Tabs
        value={selectedTab}
        onChange={handleTabChange}
        indicatorColor="primary"
        textColor="primary"
        centered
      >
        <Tab label="Standings" />
        <Tab label="Matchday" />
      </Tabs>

      {/* Loading Indicator */}
      {loading && (
        <Box display="flex" justifyContent="center" my={4}>
          <CircularProgress />
        </Box>
      )}

      {/* Error Message */}
      {error && (
        <Box my={4}>
          <Alert severity="error">{error}</Alert>
        </Box>
      )}

      {/* Content Based on Selected Tab */}
      {!loading && !error && (
        <>
          {selectedTab === 0 &&
            (console.log(
              "Passing selectedLeague to StandingsTable:",
              selectedLeague
            ),
            (
              <StandingsTable
                standings={standings}
                teamLogos={teamLogos[selectedLeague]?.logos}
                selectedLeague={selectedLeague}
              />
            ))}
          {selectedTab === 1 && (
            <>
              {/* Matchday Selector */}
              <Box display="flex" justifyContent="center" my={2}>
                <FormControl
                  variant="outlined"
                  size="small"
                  sx={{ minWidth: 160 }}
                >
                  <InputLabel id="matchday-select-label">Matchday</InputLabel>
                  <Select
                    labelId="matchday-select-label"
                    value={selectedMatchday}
                    onChange={(e) => setSelectedMatchday(e.target.value)}
                    label="Matchday"
                    sx={{
                      backgroundColor: "#ffffff",
                      borderRadius: 2,
                      boxShadow: "0 2px 5px rgba(0, 0, 0, 0.2)",
                      "& .MuiSelect-select": {
                        padding: "8px 10px",
                      },
                    }}
                  >
                    {matchdays.map((round, idx) => (
                      <MenuItem key={idx} value={round}>
                        {round}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Box>
              <FixturesList
                matches={matches}
                selectedMatchday={selectedMatchday}
                teamLogos={teamLogos[selectedLeague]?.logos}
              />
            </>
          )}
        </>
      )}
    </Container>
  );
};

export default LiveStandings;
