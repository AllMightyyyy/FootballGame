// App.js
import { Box, Container, Tab, Tabs, Button } from "@mui/material";
import React, { useState } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import Formation from "./components/Formation";
import PlayerCard from "./components/PlayerCard";
import PlayerTable from "./components/PlayerTable";
import PlayerSearchOverlay from "./components/PlayerSearchOverlay";
import CurrentTeam from "./components/CurrentTeam";
import { useFormation } from "./contexts/FormationContext";

const queryClient = new QueryClient();

const App = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [filterData, setFilterData] = useState({});
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
    updateFormation(position, selectedPlayer);
    setSelectedPlayer(null); // Clear selected player after assignment
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
            <h1 style={{ textAlign: "center", margin: "20px 0" }}>
              Football Manager
            </h1>

            <Tabs
              value={activeTab}
              onChange={handleTabChange}
              aria-label="App Tabs"
              centered
              sx={{
                backgroundColor: "#1e1e1e",
                borderBottom: "1px solid #333",
                "& .MuiTab-root": {
                  color: "#fff",
                },
                "& .Mui-selected": {
                  color: "#1976d2",
                },
              }}
            >
              <Tab label="Squad Builder" />
              <Tab label="Player List" />
            </Tabs>

            {/* Main Content Area */}
            <Box sx={{ display: "flex", flex: 1 }}>
              {activeTab === 0 && (
                <>
                  {/* Left Side: Button to Open Search Overlay */}
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
                      color="primary"
                      onClick={openSearchOverlay}
                      fullWidth
                    >
                      Search Players
                    </Button>
                    {/* Display selected player card */}
                    {selectedPlayer && (
                      <Box sx={{ marginTop: "20px" }}>
                        <PlayerCard player={selectedPlayer} />
                      </Box>
                    )}
                  </Box>

                  {/* Pitch and Current Team */}
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
                    {/* Current Team Section */}
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
            </Box>

            {/* Player Search Overlay */}
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
