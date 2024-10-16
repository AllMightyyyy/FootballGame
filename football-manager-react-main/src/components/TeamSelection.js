// TeamSelection.js
import React from "react";
import { Box, Button, Typography, List, ListItem, ListItemText } from "@mui/material";

const teams = ["Manchester United", "Real Madrid", "Barcelona", "Bayern Munich", "Juventus"];

const TeamSelection = ({ selectedTeam, onTeamSelect, onNext, onPrevious }) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "20px",
        backgroundColor: "#2e2e2e",
        padding: "30px",
        borderRadius: "8px",
      }}
    >
      <Typography variant="h5" sx={{ color: "#fff" }}>
        Select Your Team
      </Typography>
      <List sx={{ width: "100%", maxWidth: "360px", bgcolor: "#1e1e1e", borderRadius: "8px" }}>
        {teams.map((team) => (
          <ListItem
            button
            key={team}
            selected={selectedTeam === team}
            onClick={() => onTeamSelect(team)}
            sx={{
              "&.Mui-selected": { backgroundColor: "#487748" },
              "&:hover": { backgroundColor: "#333" },
            }}
          >
            <ListItemText primary={team} sx={{ color: "#fff" }} />
          </ListItem>
        ))}
      </List>
      <Box sx={{ display: "flex", gap: "10px" }}>
        <Button variant="contained" onClick={onPrevious} sx={{ backgroundColor: "#444" }}>
          Back
        </Button>
        <Button
          variant="contained"
          color="primary"
          onClick={onNext}
          sx={{ backgroundColor: "#487748" }}
          disabled={!selectedTeam}
        >
          Next
        </Button>
      </Box>
    </Box>
  );
};

export default TeamSelection;
