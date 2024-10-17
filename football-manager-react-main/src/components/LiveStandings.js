import {
  Alert,
  Box,
  CircularProgress,
  Container,
  MenuItem,
  Select,
  Tab,
  Tabs,
  Typography,
} from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import FixturesList from "./FixturesList";
import StandingsTable from "./StandingsTable";

// Mapping team names to their corresponding logo images
const teamLogos = {
  "Arsenal FC": "assets/img/img_icon1.png",
  "Aston Villa FC": "assets/img/img_icon2.png",
  "Brighton & Hove Albion FC": "assets/img/img_icon3.png",
  "Burnley FC": "assets/img/img_icon4.png",
  "Chelsea FC": "assets/img/img_icon5.png",
  "Crystal Palace FC": "assets/img/img_icon6.png",
  "Everton FC": "assets/img/img_icon7.png",
  "Fulham FC": "assets/img/img_icon8.png",
  "Leicester City FC": "assets/img/img_icon10.png",
  "Liverpool FC": "assets/img/img_icon11.png",
  "Manchester City FC": "assets/img/img_icon12.png",
  "Manchester United FC": "assets/img/img_icon13.png",
  "Newcastle United FC": "assets/img/img_icon14.png",
  "Southampton FC": "assets/img/img_icon16.png",
  "Tottenham Hotspur FC": "assets/img/img_icon17.png",
  "West Ham United FC": "assets/img/img_icon19.png",
  "Wolverhampton Wanderers FC": "assets/img/img_icon20.png",
  "AFC Bournemouth": "assets/img/img_icon21.png",
  "Ipswich Town FC": "assets/img/img_icon22.png",
  "Nottingham Forest FC": "assets/img/img_icon23.png",
};

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
        const response = await axios.get(
          `http://localhost:8081/api/football/standings/${selectedLeague}`
        );

        const matchesData = response.data;
        setMatches(matchesData);

        const standingsData = calculateStandings(matchesData);
        setStandings(standingsData);

        const matchdaysData = extractMatchdays(matchesData);
        setMatchdays(matchdaysData);
        // Set the first matchday as the selected one if it's not set
        setSelectedMatchday(matchdaysData[0] || "");
      } catch (err) {
        setError("Error fetching matches. Please try again.");
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

  return (
    <Container maxWidth="lg" sx={{ backgroundColor: "#f8f9fa", padding: 3 }}>
      <Typography
        variant="h4"
        align="center"
        gutterBottom
        sx={{ color: "#37003c", fontWeight: "bold" }}
      >
        {selectedLeague === "en.1" && "Premier League"}
      </Typography>
      <Box display="flex" justifyContent="center" my={2}>
        <Select
          value={selectedLeague}
          onChange={(e) => setSelectedLeague(e.target.value)}
          sx={{ width: 200, marginBottom: 2 }}
        >
          <MenuItem value="en.1">Premier League</MenuItem>
          <MenuItem value="es.1">La Liga</MenuItem>
          <MenuItem value="de.1">Bundesliga</MenuItem>
          <MenuItem value="it.1">Serie A</MenuItem>
          <MenuItem value="fr.1">Ligue 1</MenuItem>
          <MenuItem value="uefa.cl">Champions League</MenuItem>
        </Select>
      </Box>
      <Tabs
        value={selectedTab}
        onChange={handleTabChange}
        indicatorColor="primary"
        textColor="primary"
        centered
        sx={{
          backgroundColor: "#1e1e1e",
          "& .MuiTab-root": {
            color: "#fff",
          },
        }}
      >
        <Tab label="Standings" />
        <Tab label="Matchday" />
      </Tabs>
      {loading && <CircularProgress />}
      {error && <Alert severity="error">{error}</Alert>}
      {!loading && !error && (
        <>
          {selectedTab === 0 && (
            <StandingsTable standings={standings} teamLogos={teamLogos} />
          )}
          {selectedTab === 1 && (
            <>
              <Box display="flex" justifyContent="center" my={2}>
                <Select
                  value={selectedMatchday}
                  onChange={(e) => setSelectedMatchday(e.target.value)}
                  sx={{ width: 200, marginBottom: 2 }}
                >
                  {matchdays.map((round, idx) => (
                    <MenuItem key={idx} value={round}>
                      {round}
                    </MenuItem>
                  ))}
                </Select>
              </Box>
              <FixturesList
                matches={matches}
                selectedMatchday={selectedMatchday}
                teamLogos={teamLogos}
              />
            </>
          )}
        </>
      )}
    </Container>
  );
};

export default LiveStandings;
