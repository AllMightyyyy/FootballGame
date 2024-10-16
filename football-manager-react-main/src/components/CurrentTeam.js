// CurrentTeam.js
import React from "react";
import { useFormation } from "../contexts/FormationContext";
import { List, ListItem, ListItemText, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

const CurrentTeam = () => {
  const { formation, removePlayer } = useFormation();

  const playersInFormation = Object.entries(formation)
    .filter(([, player]) => player)
    .map(([position, player]) => ({ position, player }));

  return (
    <List>
      {playersInFormation.map(({ position, player }) => (
        <ListItem key={position}>
          <ListItemText primary={`${position}: ${player.shortName}`} />
          <IconButton onClick={() => removePlayer(position)}>
            <CloseIcon />
          </IconButton>
        </ListItem>
      ))}
    </List>
  );
};

export default CurrentTeam;
