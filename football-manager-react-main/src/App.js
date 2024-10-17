import { Box, Button, Container, Tab, Tabs } from "@mui/material";
import React, { useState } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import CurrentTeam from "./components/CurrentTeam";
import Formation from "./components/Formation";
import Game from "./components/Game";
import LiveStandings from "./components/LiveStandings";
import PlayerCard from "./components/PlayerCard";
import PlayerSearchOverlay from "./components/PlayerSearchOverlay";
import PlayerTable from "./components/PlayerTable";
import { useFormation } from "./contexts/FormationContext";

const queryClient = new QueryClient();

const App = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [filterData, setFilterData] = useState({
    rating: [40, 120],
    position: [],
    league: [],
    club: [],
    nation: [],
    height: [150, 215],
    weight: [40, 120],
    excludeSelected: {
      position: false,
      league: false,
      club: false,
      nation: false,
    },
  });
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [availablePositions, setAvailablePositions] = useState([]);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const { formation, updateFormation } = useFormation();

  const handlePlayerSelect = (player) => {
    setSelectedPlayer(player);
    const playerPositions = player.positions
      .split(",")
      .map((pos) => pos.trim());
    let positionsInFormation = [];

    playerPositions.forEach((pos) => {
      switch (pos) {
        case "CB":
          if (!formation.LCB) positionsInFormation.push("LCB");
          if (!formation.RCB) positionsInFormation.push("RCB");
          break;
        case "CM":
          if (!formation.LCM) positionsInFormation.push("LCM");
          if (!formation.RCM) positionsInFormation.push("RCM");
          if (!formation.CM) positionsInFormation.push("CM");
          break;
        case "LB":
        case "RB":
        case "GK":
        case "LW":
        case "RW":
        case "ST":
          if (!formation[pos]) positionsInFormation.push(pos);
          break;
        default:
          break;
      }
    });

    setAvailablePositions(positionsInFormation);
  };

  const handlePositionSelect = (position) => {
    if (!selectedPlayer) return;
    updateFormation(position, selectedPlayer);
    setSelectedPlayer(null);
    setAvailablePositions([]);
  };

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const openSearchOverlay = () => {
    setIsSearchOpen(true);
  };

  const closeSearchOverlay = () => {
    setIsSearchOpen(false);
  };

  return (
    <QueryClientProvider client={queryClient}>
      <Container maxWidth={false} disableGutters>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            backgroundColor: "#121212",
            color: "#fff",
            minHeight: "100vh",
          }}
        >
          <h1
            style={{ textAlign: "center", margin: "20px 0", color: "#487748" }}
          >
            Football Manager
          </h1>

          <Tabs
            value={activeTab}
            onChange={handleTabChange}
            aria-label="App Tabs"
            centered
            TabIndicatorProps={{
              style: {
                backgroundColor: "green",
              },
            }}
            sx={{
              backgroundColor: "#1e1e1e",
              borderBottom: "1px solid #333",
              "& .MuiTab-root": {
                color: "#fff",
              },
              "& .Mui-selected": {
                color: "green",
              },
              "& .MuiTabs-indicator": {
                backgroundColor: "green",
                height: 3,
              },
            }}
          >
            <Tab label="Squad Builder" />
            <Tab label="Player List" />
            <Tab label="Play Game" />
            <Tab label="Live Football Standings" />{" "}
            {/* New tab for Live Standings */}
          </Tabs>

          <Box sx={{ display: "flex", flex: 1 }}>
            {activeTab === 0 && (
              <>
                <Box
                  sx={{
                    width: "300px",
                    backgroundColor: "#1e1e1e",
                    padding: "20px",
                    borderRight: "1px solid #333",
                  }}
                >
                  <Button
                    variant="contained"
                    sx={{ backgroundColor: "#487748" }}
                    onClick={openSearchOverlay}
                    fullWidth
                  >
                    Search Players
                  </Button>
                  {selectedPlayer && (
                    <Box sx={{ marginTop: "20px" }}>
                      <PlayerCard player={selectedPlayer} />
                    </Box>
                  )}
                </Box>
                <Box
                  sx={{
                    flex: 1,
                    padding: "20px",
                    display: "flex",
                    flexDirection: "column",
                    gap: "20px",
                  }}
                >
                  <Formation
                    selectedPlayer={selectedPlayer}
                    availablePositions={availablePositions}
                    onPositionSelect={handlePositionSelect}
                  />
                  <CurrentTeam />
                </Box>
              </>
            )}

            {activeTab === 1 && (
              <Box sx={{ flex: 1 }}>
                <Box sx={{ padding: "20px" }}>
                  <PlayerTable filters={filterData} />
                </Box>
              </Box>
            )}

            {activeTab === 2 && (
              <Box sx={{ flex: 1, padding: "20px" }}>
                <Game />
              </Box>
            )}

            {activeTab === 3 && (
              <Box sx={{ flex: 1, padding: "20px" }}>
                <LiveStandings />
              </Box>
            )}
          </Box>

          <PlayerSearchOverlay
            open={isSearchOpen}
            handleClose={closeSearchOverlay}
            filters={filterData}
            onFilterChange={setFilterData}
            onPlayerSelect={handlePlayerSelect}
          />
        </Box>
      </Container>
    </QueryClientProvider>
  );
};

export default App;
