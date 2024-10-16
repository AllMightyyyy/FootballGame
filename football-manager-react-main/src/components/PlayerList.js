import React, { useState } from "react";
import { Dialog, List, ListItem, ListItemText } from "@mui/material";
import PlayerCard from "./PlayerCard";

const PlayerList = ({ players, isLoading, isError }) => {
  const [selectedPlayer, setSelectedPlayer] = useState(null);

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
    setSelectedPlayer(player);
  };

  return (
    <div>
      <List>
        {players.map((player) => (
          <ListItem button key={player.id} onClick={() => handlePlayerClick(player)}>
            <ListItemText primary={player.shortName} secondary={player.positions} />
          </ListItem>
        ))}
      </List>

      <Dialog open={!!selectedPlayer} onClose={() => setSelectedPlayer(null)}>
        {selectedPlayer && <PlayerCard player={selectedPlayer} />}
      </Dialog>
    </div>
  );
};

export default PlayerList;
