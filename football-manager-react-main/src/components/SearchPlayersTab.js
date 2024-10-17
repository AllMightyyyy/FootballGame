// src/components/SearchPlayersTab.js

import React, { useState } from "react";
import { Box } from "@mui/material";
import Filters from "./Filters";
import Formation from "./Formation";
import PlayerSearchOverlay from "./PlayerSearchOverlay";
import CurrentTeam from "./CurrentTeam";
import { useFormation } from "../contexts/FormationContext"; // Import useFormation

const SearchPlayersTab = () => {
  const { formation, updateFormation } = useFormation(); // Destructure from context

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

  const handlePlayerSelect = (player) => {
    setSelectedPlayer(player);
    const playerPositions = player.positions ? player.positions.split(",").map((pos) => pos.trim()) : [];

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
    setIsSearchOpen(true);
  };

  const handlePositionSelect = (position) => {
    if (!selectedPlayer) return;
    updateFormation(position, selectedPlayer);
    setSelectedPlayer(null);
    setAvailablePositions([]);
  };

  const openSearchOverlay = () => {
    setIsSearchOpen(true);
  };

  const closeSearchOverlay = () => {
    setIsSearchOpen(false);
  };

  return (
    <Box sx={{ display: "flex", gap: 2 }}>
      {/* Filters Component */}
      <Box sx={{ width: "30%" }}>
        <Filters
          filters={filterData}
          onFilterChange={setFilterData}
          onSearch={openSearchOverlay}
        />
      </Box>

      {/* Formation Component */}
      <Box sx={{ width: "70%" }}>
        <Formation
          selectedPlayer={selectedPlayer}
          availablePositions={availablePositions}
          onPositionSelect={handlePositionSelect}
        />
        <CurrentTeam />
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
  );
};

export default SearchPlayersTab;
