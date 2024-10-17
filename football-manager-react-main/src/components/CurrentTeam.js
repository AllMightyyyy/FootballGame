// src/components/CurrentTeam.js
import React from "react";
import { useFormation } from "../contexts/FormationContext";
import { Box, List, ListItem, ListItemText, IconButton, Avatar, Typography, Divider } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

const CurrentTeam = () => {
  const { formation, removePlayer } = useFormation();

  const playersInFormation = Object.entries(formation)
    .filter(([, player]) => player)
    .map(([position, player]) => ({ position, player }));

  console.log('Current Team Formation State:', formation);

  return (
    <Box sx={{ backgroundColor: "#1e1e1e", borderRadius: "8px", padding: "10px", marginTop: "10px" }}>
      <Typography variant="h6" sx={{ color: "#fff", marginBottom: "16px" }}>
        Current Team
      </Typography>
      <List>
        {playersInFormation.map(({ position, player }) => (
          <React.Fragment key={position}>
            <ListItem
              sx={{
                display: "flex",
                alignItems: "center",
                padding: "8px",
                backgroundColor: "#2e2e2e",
                borderRadius: "8px",
                marginBottom: "10px",
                boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
              }}
            >
              <Avatar
                src={player.playerFaceUrl}
                alt={player.shortName}
                sx={{ width: 70, height: 80, marginRight: 2 }}
              />
              <Box sx={{ flex: 1, marginLeft: "16px" }}>
                <Typography variant="body1" sx={{ color: "#fff", fontWeight: "bold" }}>
                  {player.shortName}
                </Typography>
                <Typography variant="body2" sx={{ color: "#aaa" }}>
                  Position: {position}
                </Typography>
                <Typography variant="body2" sx={{ color: "#aaa" }}>
                  Overall: {player.overall}
                </Typography>
              </Box>
              <IconButton onClick={() => removePlayer(position)} sx={{ color: "#fff" }}>
                <CloseIcon />
              </IconButton>
            </ListItem>
            <Divider variant="middle" sx={{ borderColor: "#444" }} />
          </React.Fragment>
        ))}
        {playersInFormation.length === 0 && (
          <Typography variant="body2" sx={{ color: "#aaa", textAlign: "center", marginTop: "20px" }}>
            No players in the current team.
          </Typography>
        )}
      </List>
    </Box>
  );
};

export default CurrentTeam;
