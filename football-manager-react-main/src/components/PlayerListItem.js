// src/components/PlayerListItem.js

import { Avatar, Box, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

const getAttributeColor = (value) => {
  if (value < 50) return "red";
  if (value < 66) return "orange";
  if (value < 80) return "#9acd32";
  if (value < 90) return "limegreen";
  return "darkgreen";
};

const PlayerListItem = ({ player, onClick }) => {
  // Determine positions to display
  const positionsToDisplay =
    Array.isArray(player.positionsList) && player.positionsList.length > 0
      ? player.positionsList
      : player.positions
      ? player.positions.split(",").map((pos) => pos.trim())
      : [];

  // Check if the player is a goalkeeper (goalkeepers typically have "GK" in their positions) -> Case SENSITIVE

  // TODO : make it non case sensitive to check positions, maybe have a better mapping solution for positions using a List
  console.log(positionsToDisplay);
  const isGoalkeeper = positionsToDisplay.includes("GK");

  console.log("Player:", player);
  console.log("Player Name:", player.shortName);
  console.log("Positions to Display:", positionsToDisplay);
  console.log("Is Goalkeeper:", isGoalkeeper);

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
        cursor: "pointer",
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
          {positionsToDisplay.length > 0
            ? positionsToDisplay.join(", ")
            : "N/A"}{" "}
          | Overall: {player.overall} | Potential: {player.potential}
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
          <Typography variant="body2" sx={{ color: "#aaa" }}>
            {player.nationalityName}
          </Typography>
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
        {isGoalkeeper
          ? [
              { label: "Diving", value: player.goalkeepingDiving },
              { label: "Handling", value: player.goalkeepingHandling },
              { label: "Kicking", value: player.goalkeepingKicking },
              { label: "Positioning", value: player.goalkeepingPositioning },
              { label: "Reflexes", value: player.goalkeepingReflexes },
              { label: "Speed", value: player.goalkeepingSpeed },
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
            ))
          : [
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
    positionsList: PropTypes.arrayOf(PropTypes.string).isRequired,
    overall: PropTypes.number.isRequired,
    potential: PropTypes.number.isRequired,
    pace: PropTypes.number.isRequired,
    shooting: PropTypes.number.isRequired,
    passing: PropTypes.number.isRequired,
    dribbling: PropTypes.number.isRequired,
    defending: PropTypes.number.isRequired,
    physical: PropTypes.number.isRequired,
    goalkeepingDiving: PropTypes.number.isRequired,
    goalkeepingHandling: PropTypes.number.isRequired,
    goalkeepingKicking: PropTypes.number.isRequired,
    goalkeepingPositioning: PropTypes.number.isRequired,
    goalkeepingReflexes: PropTypes.number.isRequired,
    goalkeepingSpeed: PropTypes.number.isRequired,
    clubLogoUrl: PropTypes.string.isRequired,
    clubName: PropTypes.string.isRequired,
    nationFlagUrl: PropTypes.string.isRequired,
    nationalityName: PropTypes.string.isRequired,
  }).isRequired,
  onClick: PropTypes.func.isRequired,
};

export default PlayerListItem;
