// src/components/PlayerTableTab.js

import React, { useState } from "react";
import { Box } from "@mui/material";
import PlayerTable from "./PlayerTable";
import Filters from "./Filters";

const PlayerTableTab = () => {
  const [filterData, setFilterData] = useState({
    rating: [40, 99],
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

  const handlePlayerSelect = (player) => {
    // Handle player selection if needed
    // For example, open PlayerCard or assign to formation
  };

  return (
    <Box sx={{ display: "flex", flexDirection: "row", gap: 2 }}>
      <Box sx={{ width: "300px", flexShrink: 0 }}>
        <Filters
          filters={filterData}
          onFilterChange={setFilterData}
          onSearch={() => { /* Implement search functionality if needed */ }}
        />
      </Box>
      <Box sx={{ flexGrow: 1 }}>
        <PlayerTable filters={filterData} onPlayerSelect={handlePlayerSelect} />
      </Box>
    </Box>
  );
};

export default PlayerTableTab;
