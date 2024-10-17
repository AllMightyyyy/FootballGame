// src/components/PlayerList.js
import React from "react";
import { List, ListItem, ListItemText } from "@mui/material";

const PlayerList = ({ players, isLoading, isError, onPlayerSelect }) => {
  if (isLoading) {
    return <div>Loading players...</div>;
  }

  if (isError) {
    return <div>Error loading players</div>;
  }

  if (!Array.isArray(players) || players.length === 0) {
    return <div>No players found or invalid data format.</div>;
  }

  const handlePlayerClick = (player) => {
    onPlayerSelect(player); 
  };

  return (
    <List>
      {players.map((player) => (
        <ListItem
          button
          key={player.id}
          onClick={() => handlePlayerClick(player)}
        >
          <ListItemText primary={player.shortName} secondary={player.positions} />
        </ListItem>
      ))}
    </List>
  );
};

export default PlayerList;
