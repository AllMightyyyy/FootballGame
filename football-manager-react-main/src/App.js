// App.js
import { Box, Container, Tab, Tabs } from "@mui/material";
import React, { useState } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import Filters from "./components/Filters";
import Formation from "./components/Formation";
import PlayerCard from "./components/PlayerCard";
import PlayerSearchOverlay from "./components/PlayerSearchOverlay";
import PlayerTable from "./components/PlayerTable";
import { FormationProvider, useFormation } from "./contexts/FormationContext";

const queryClient = new QueryClient();

const App = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [filterData, setFilterData] = useState({});
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [availablePositions, setAvailablePositions] = useState([]);
  const [isSearchOpen, setSearchOpen] = useState(false);
  const [overlayFilters, setOverlayFilters] = useState({});
  const { updateFormation } = useFormation();

  const handlePlayerSelect = (player, positionsInFormation) => {
    setSelectedPlayer(player);
    setAvailablePositions(positionsInFormation);
  };

  const handlePositionSelect = (position) => {
    // Logic to update the formation with the selected player
    updateFormation(position, selectedPlayer);
    setSelectedPlayer(null); // Clear selected player after assignment
    setAvailablePositions([]); // Clear available positions
  };

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const handleFilterChange = (filters) => {
    setFilterData(filters);
  };

  const openSearchOverlay = () => {
    setOverlayFilters(filterData); // Pass the current filterData to the overlay
    setSearchOpen(true);
  };

  const closeSearchOverlay = () => {
    setSearchOpen(false);
  };

  return (
    <QueryClientProvider client={queryClient}>
      <FormationProvider>
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
              {/* Filters on the Left */}
              <Box
                sx={{
                  width: "300px",
                  backgroundColor: "#1e1e1e",
                  padding: "20px",
                  borderRight: "1px solid #333",
                }}
              >
                <Filters
                  onFilterChange={handleFilterChange}
                  onSearch={openSearchOverlay}
                />
              </Box>

              {/* Tab Content */}
              <Box
                role="tabpanel"
                hidden={activeTab !== 0}
                sx={{ flex: 1, padding: "20px", display: "flex", gap: "20px" }} // ADD "display: flex, gap: 20px"
              >
                {selectedPlayer && <PlayerCard player={selectedPlayer} />}
                <Formation
                  selectedPlayer={selectedPlayer}
                  availablePositions={availablePositions}
                  onPositionSelect={handlePositionSelect}
                />
              </Box>

              <Box role="tabpanel" hidden={activeTab !== 1} sx={{ flex: 1 }}>
                <Box sx={{ padding: "20px" }}>
                  <PlayerTable filters={filterData} />
                </Box>
              </Box>
            </Box>

            {/* Player Search Overlay */}
            <PlayerSearchOverlay
              open={isSearchOpen}
              handleClose={closeSearchOverlay}
              filters={overlayFilters}
              onFilterChange={setOverlayFilters}
            />
          </Box>
        </Container>
      </FormationProvider>
    </QueryClientProvider>
  );
};

export default App;
