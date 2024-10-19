// src/components/PlayerListItem.js

import { Avatar, Box, Typography } from "@mui/material";
import React from "react";
import PropTypes from "prop-types";

const getAttributeColor = (value) => {
  if (value < 50) return "red";
  if (value < 66) return "orange";
  if (value < 80) return "#9acd32";
  if (value < 90) return "limegreen";
  return "darkgreen";
};

const PlayerListItem = ({ player, onClick }) => {
  return (
    <Box
      onClick={() => onClick(player)}
      sx={{
        display: "flex",
        alignItems: "center",
        padding: "16px",
        border: "1px solid #444",
        borderRadius: "8px",
        marginBottom: "10px",
        backgroundColor: "#2e2e2e",
        cursor: "pointer", // Indicate clickable
        transition: "background-color 0.3s",
        "&:hover": {
          backgroundColor: "#3e3e3e",
        },
      }}
    >
      {/* Player Image */}
      <Avatar
        src={player.playerFaceUrl}
        alt={player.shortName}
        sx={{ width: 56, height: 56, marginRight: 2 }}
      />

      {/* Player Details */}
      <Box sx={{ flex: 1, marginLeft: "16px" }}>
        <Typography variant="h6" sx={{ color: "#fff" }}>
          {player.shortName}
        </Typography>
        <Typography variant="body2" sx={{ color: "#aaa" }}>
          {player.positions.join(", ")} | Overall: {player.overall} | Potential:{" "}
          {player.potential}
        </Typography>
        <Box sx={{ display: "flex", alignItems: "center", marginTop: "8px" }}>
          <img
            src={player.clubLogoUrl}
            alt={player.clubName}
            style={{ width: "24px", height: "24px", marginRight: "8px" }}
          />
          <Typography variant="body2" sx={{ color: "#aaa" }}>
            {player.clubName}
          </Typography>
          <img
            src={player.nationFlagUrl}
            alt="Nationality"
            style={{
              width: "24px",
              height: "24px",
              marginLeft: "16px",
              marginRight: "8px",
            }}
          />
        </Box>
      </Box>

      {/* Key Attributes */}
      <Box
        sx={{
          display: "flex",
          gap: "16px",
          flexWrap: "wrap",
          marginLeft: "16px",
        }}
      >
        {[
          { label: "Pace", value: player.pace },
          { label: "Shooting", value: player.shooting },
          { label: "Passing", value: player.passing },
          { label: "Dribbling", value: player.dribbling },
          { label: "Defending", value: player.defending },
          { label: "Physical", value: player.physical },
        ].map((attr) => (
          <Box key={attr.label} sx={{ textAlign: "center" }}>
            {/* Attribute Label */}
            <Typography
              variant="caption"
              sx={{ color: "#aaa", marginBottom: "4px" }}
            >
              {attr.label.toUpperCase()}
            </Typography>

            {/* Attribute Value Box */}
            <Box
              sx={{
                backgroundColor: getAttributeColor(attr.value),
                color: "#fff",
                borderRadius: "4px",
                padding: "4px 8px",
                minWidth: "40px",
                textAlign: "center",
                fontWeight: "bold",
              }}
            >
              {attr.value}
            </Box>
          </Box>
        ))}
      </Box>
    </Box>
  );
};

PlayerListItem.propTypes = {
  player: PropTypes.shape({
    id: PropTypes.number.isRequired,
    shortName: PropTypes.string.isRequired,
    positions: PropTypes.arrayOf(PropTypes.string).isRequired,
    overall: PropTypes.number.isRequired,
    potential: PropTypes.number.isRequired,
    playerFaceUrl: PropTypes.string.isRequired,
    clubName: PropTypes.string.isRequired,
    clubLogoUrl: PropTypes.string.isRequired,
    nationFlagUrl: PropTypes.string.isRequired,
    pace: PropTypes.number.isRequired,
    shooting: PropTypes.number.isRequired,
    passing: PropTypes.number.isRequired,
    dribbling: PropTypes.number.isRequired,
    defending: PropTypes.number.isRequired,
    physical: PropTypes.number.isRequired,
  }).isRequired,
  onClick: PropTypes.func.isRequired,
};

export default PlayerListItem;
