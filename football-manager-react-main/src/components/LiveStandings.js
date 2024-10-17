// src/components/LiveStandings.js
import {
  Alert,
  Box,
  CircularProgress,
  Container,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Tab,
  Tabs,
  Typography,
} from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import FixturesList from "./FixturesList";
import StandingsTable from "./StandingsTable";
import teamLogos from "./utils/teamLogos";

const LiveStandings = () => {
  const [standings, setStandings] = useState([]);
  const [matches, setMatches] = useState([]);
  const [selectedLeague, setSelectedLeague] = useState("en.1");
  const [selectedTab, setSelectedTab] = useState(0);
  const [matchdays, setMatchdays] = useState([]);
  const [selectedMatchday, setSelectedMatchday] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getMatches = async () => {
      setLoading(true);
      setError(null);
      try {
        // Correct API endpoint for fetching football standings and matches
        const response = await axios.get(
          `http://localhost:8081/api/football/standings/${selectedLeague}`
        );

        const matchesData = response.data; // Assuming response.data is the matches array
        setMatches(matchesData); // Set matches directly

        const standingsData = calculateStandings(matchesData);
        setStandings(standingsData);

        console.log("Fetched Matches:", matchesData);
        console.log("Calculated Standings:", standingsData);

        const matchdaysData = extractMatchdays(matchesData);
        setMatchdays(matchdaysData);
        setSelectedMatchday(matchdaysData[0] || "");
      } catch (err) {
        console.error(err);
        setError("Error fetching data. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    getMatches();
  }, [selectedLeague]);

  const calculateStandings = (matches) => {
    const teams = {};
    matches.forEach((match) => {
      const { team1, team2, score } = match;
      if (!score || !score.ft) return;
      const [team1Goals, team2Goals] = score.ft.map(Number);
      if (!teams[team1]) {
        teams[team1] = {
          name: team1,
          played: 0,
          win: 0,
          draw: 0,
          lose: 0,
          goalsFor: 0,
          goalsAgainst: 0,
          goalDifference: 0,
          points: 0,
        };
      }
      if (!teams[team2]) {
        teams[team2] = {
          name: team2,
          played: 0,
          win: 0,
          draw: 0,
          lose: 0,
          goalsFor: 0,
          goalsAgainst: 0,
          goalDifference: 0,
          points: 0,
        };
      }
      teams[team1].played += 1;
      teams[team2].played += 1;
      teams[team1].goalsFor += team1Goals;
      teams[team2].goalsFor += team2Goals;
      teams[team1].goalsAgainst += team2Goals;
      teams[team2].goalsAgainst += team1Goals;
      teams[team1].goalDifference =
        teams[team1].goalsFor - teams[team1].goalsAgainst;
      teams[team2].goalDifference =
        teams[team2].goalsFor - teams[team2].goalsAgainst;
      if (team1Goals > team2Goals) {
        teams[team1].win += 1;
        teams[team1].points += 3;
        teams[team2].lose += 1;
      } else if (team1Goals < team2Goals) {
        teams[team2].win += 1;
        teams[team2].points += 3;
        teams[team1].lose += 1;
      } else {
        teams[team1].draw += 1;
        teams[team2].draw += 1;
        teams[team1].points += 1;
        teams[team2].points += 1;
      }
    });
    return Object.values(teams).sort((a, b) => {
      if (b.points !== a.points) {
        return b.points - a.points;
      } else if (b.goalDifference !== a.goalDifference) {
        return b.goalDifference - a.goalDifference;
      } else {
        return b.goalsFor - a.goalsFor;
      }
    });
  };

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
    setSelectedLeague(event.target.value);
  };

  return (
    <Container maxWidth="xl" sx={{ backgroundColor: "#f8f9fa", padding: 3 }}>
      {/* Header with League Selector */}
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        {/* League Title with Logo */}
        <Box display="flex" alignItems="center" gap={1}>
        <Box
          component="img"
          src={teamLogos[selectedLeague].leagueLogo}
          alt={`${teamLogos[selectedLeague].leagueName} Logo`}
          sx={{
            width: 140, 
            height: 140, 
            objectFit: 'contain',  
            backgroundColor: 'white', 
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
              backgroundColor: '#ffffff',
              borderRadius: 1,
              '& .MuiSelect-select': {
                padding: '6px 8px',
              },
            }}
          >
            <MenuItem value="en.1">Premier League</MenuItem>
            <MenuItem value="es.1">La Liga</MenuItem>
            <MenuItem value="de.1">Bundesliga</MenuItem>
            <MenuItem value="it.1">Serie A</MenuItem>
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
          {selectedTab === 0 && (
            <StandingsTable
              standings={standings}
              teamLogos={teamLogos[selectedLeague].logos}
              matches={matches}
            />
          )}
          {selectedTab === 1 && (
            <>
              {/* Matchday Selector */}
              <Box display="flex" justifyContent="center" my={2}>
                <FormControl variant="outlined" size="small" sx={{ minWidth: 160 }}>
                  <InputLabel id="matchday-select-label">Matchday</InputLabel>
                  <Select
                    labelId="matchday-select-label"
                    value={selectedMatchday}
                    onChange={(e) => setSelectedMatchday(e.target.value)}
                    label="Matchday"
                    sx={{
                      backgroundColor: '#ffffff',
                      borderRadius: 2,
                      boxShadow: "0 2px 5px rgba(0, 0, 0, 0.2)",
                      '& .MuiSelect-select': {
                        padding: '8px 10px',
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
                teamLogos={teamLogos[selectedLeague].logos}
              />
            </>
          )}
        </>
      )}
    </Container>
  );
};

export default LiveStandings;
