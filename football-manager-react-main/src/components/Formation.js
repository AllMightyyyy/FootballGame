import { Box } from "@mui/material";
import React, { useState } from "react";
import { useFormation } from "../contexts/FormationContext";
import PitchImage from "../pitch.png";
import PlayerCard from "./PlayerCard";
import PlayerSearchOverlay from "./PlayerSearchOverlay";

/*
  xPercent = (xPosition / imageWidth) * 100
  yPercent = (yPosition / imageHeight) * 100

*/

const positions = {
  GK: { x: (22 / 626) * 100, y: (205 / 417) * 100 }, // x: 3.51%, y: 49.16%
  LB: { x: (197 / 626) * 100, y: ((336 * 0.2) / 417) * 100 }, // x: 31.5%, y: 31.45%
  LCB: { x: (118 / 626) * 100, y: ((449 * 0.305) / 417) * 100 }, // x: 23.6%, y: 42.05%
  RCB: { x: (118 / 626) * 100, y: ((604 * 0.45) / 417) * 100 }, // x: 23.6%, y: 56.57%
  RB: { x: (197 / 626) * 100, y: ((716 * 0.49) / 417) * 100 }, // x: 31.0%, y: 67.04%
  LCM: { x: (405 / 626) * 100, y: ((399 * 0.3905) / 417) * 100 }, // x: 66.3%, y: 37.35%
  CM: { x: (265 / 626) * 100, y: ((530 * 0.3905) / 417) * 100 }, // x: 47.1%, y: 49.6%
  RCM: { x: (405 / 626) * 100, y: ((655 * 0.3905) / 417) * 100 }, // x: 66.3%, y: 61.29%
  LW: { x: (496 / 626) * 100, y: ((340 * 0.2) / 417) * 100 }, // x: 87.2%, y: 31.85%
  ST: { x: (551 / 626) * 100, y: ((528 * 0.3905) / 417) * 100 }, // x: 97.6%, y: 49.3%
  RW: { x: (496 / 626) * 100, y: ((714 * 0.49) / 417) * 100 }, // x: 87.2%, y: 66.8%
};

const Formation = ({
  selectedPlayer,
  availablePositions,
  onPositionSelect,
}) => {
  const { formation } = useFormation();
  const [selectedPosition, setSelectedPosition] = useState(null);
  const [isSearchOpen, setIsSearchOpen] = useState(false);

  const handlePositionClick = (position) => {
    if (availablePositions.includes(position)) {
      onPositionSelect(position);
    }
  };

  const closeSearchOverlay = () => {
    setIsSearchOpen(false);
    setSelectedPosition(null);
  };

  const logPositions = () => {
    console.log("Current Positions:", positions);
  };

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
        {Object.keys(positions).map((position) => (
          <Box
            key={position}
            sx={{
              position: "absolute",
              left: `${positions[position].x}%`,
              top: `${positions[position].y}%`,
              transform: "translate(-50%, -50%)",
              cursor: "pointer",
              backgroundColor: availablePositions.includes(position)
                ? "rgba(0, 255, 0, 0.4)"
                : "transparent",
              borderRadius: "50%",
            }}
            onClick={() => handlePositionClick(position)}
          >
            {formation[position] ? (
              <PlayerCard player={formation[position]} size="small" />
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
        ))}
      </Box>

      {/*
        <Button
          variant="contained"
          color="primary"
          onClick={logPositions}
          sx={{
            position: 'absolute',
            bottom: 10,
            left: '50%',
            transform: 'translateX(-50%)',
          }}
        >
          Log Positions
        </Button>
        */}

      {isSearchOpen && (
        <PlayerSearchOverlay
          open={isSearchOpen}
          handleClose={closeSearchOverlay}
          selectedPosition={selectedPosition}
        />
      )}
    </>
  );
};

export default Formation;
