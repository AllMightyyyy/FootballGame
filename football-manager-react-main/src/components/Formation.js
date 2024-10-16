import { Box } from "@mui/material";
import React from "react";
import { useFormation } from "../contexts/FormationContext";
import PitchImage from "../pitch.png";
import PlayerCard from "./PlayerCard";

const positions = {
  GK: { x: (22 / 626) * 100, y: (205 / 417) * 100 }, // x: 3.51%, y: 49.16%
  LB: { x: (197 / 626) * 100, y: ((336 * 0.2) / 417) * 100 }, // x: 31.5%, y: 31.45%
  LCB: { x: (118 / 626) * 100, y: ((449 * 0.305) / 417) * 100 }, // x: 23.6%, y: 42.05%
  RCB: { x: (118 / 626) * 100, y: ((604 * 0.45) / 417) * 100 }, // x: 23.6%, y: 56.57%
  RB: { x: (197 / 626) * 100, y: ((716 * 0.49) / 417) * 100 }, // x: 31.0%, y: 67.04%
  LCM: { x: (405 / 626) * 100, y: ((350 * 0.3905) / 417) * 100 }, // x: 66.3%, y: 37.35%
  CM: { x: (265 / 626) * 100, y: ((530 * 0.3905) / 417) * 100 }, // x: 47.1%, y: 49.6%
  RCM: { x: (405 / 626) * 100, y: ((720 * 0.3905) / 417) * 100 }, // x: 66.3%, y: 61.29%
  LW: { x: (496 / 626) * 100, y: ((340 * 0.2) / 417) * 100 }, // x: 87.2%, y: 31.85%
  ST: { x: (551 / 626) * 100, y: ((528 * 0.3905) / 417) * 100 }, // x: 97.6%, y: 49.3%
  RW: { x: (496 / 626) * 100, y: ((714 * 0.49) / 417) * 100 }, // x: 87.2%, y: 66.8%
};

const Formation = ({ selectedPlayer, availablePositions, onPositionSelect, onPositionFilterUpdate }) => {
  const { formation, updateFormation, removePlayer } = useFormation();

  console.log('Selected Player in Formation:', selectedPlayer);

  React.useEffect(() => {
    console.log('Current Formation State in Formation.js (useEffect):', formation);
  }, [formation]);

  const handlePositionClick = (position) => {
    console.log('Clicked Position:', position);
  
    if (selectedPlayer && availablePositions.includes(position)) {
      console.log('Assigning player to position:', selectedPlayer, position);
      onPositionSelect(position);
    } else if (formation[position]) {
      removePlayer(position);
    } else if (!formation[position] && onPositionFilterUpdate) {
      onPositionFilterUpdate(position);
    }
  };

  console.log('Current Formation State in Formation.js:', formation);
  

  return (
    <>
      <Box
        sx={{
          position: "relative",
          width: "100%",
          maxWidth: "800px",
          aspectRatio: "626 / 417",
          backgroundImage: `url(${PitchImage})`,
          backgroundSize: "contain",
          backgroundRepeat: "no-repeat",
          backgroundPosition: "center",
          margin: "0 auto",
        }}
      >
        {Object.keys(positions).map((position) => {
          const isAvailable =
            selectedPlayer && availablePositions.includes(position);
          const playerInPosition = formation[position];

          if (selectedPlayer && !availablePositions.includes(position) && !playerInPosition) {
            return null; 
          }

          return (
            <Box
              key={position}
              sx={{
                position: "absolute",
                left: `${positions[position].x}%`,
                top: `${positions[position].y}%`,
                transform: "translate(-50%, -50%)",
                cursor: "pointer",
                borderRadius: "50%",
                backgroundColor: isAvailable
                  ? "rgba(0, 255, 0, 0.4)"
                  : "transparent",
              }}
              onClick={() => handlePositionClick(position)}
            >
              {playerInPosition ? (
                <PlayerCard player={playerInPosition} size="small" />
              ) : (
                <Box
                  sx={{
                    width: "60px",
                    height: "60px",
                    borderRadius: "50%",
                    backgroundColor: "rgba(255, 255, 255, 0.8)",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    color: "#000",
                    fontWeight: "bold",
                  }}
                >
                  {position}
                </Box>
              )}
            </Box>
          );
        })}
      </Box>
    </>
  );
};

export default Formation;