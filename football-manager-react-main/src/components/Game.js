// src/components/Game.js
import React, { useState } from "react";
import { Box } from "@mui/material";
import GameMainScreen from "./GameMainScreen"; 
import ManagerSetup from "./ManagerSetup";
import TeamSelection from "./TeamSelection";
import ConfirmationScreen from "./ConfirmationScreen";
import Formation from "./Formation";
import CurrentTeam from "./CurrentTeam";

const Game = () => {
  const [onboardingStep, setOnboardingStep] = useState(0);
  const [managerName, setManagerName] = useState("");
  const [selectedTeam, setSelectedTeam] = useState(null);

  const handleNextStep = () => {
    setOnboardingStep(onboardingStep + 1);
  };

  const handlePreviousStep = () => {
    setOnboardingStep(onboardingStep - 1);
  };

  const handleManagerNameChange = (name) => {
    setManagerName(name);
  };

  const handleTeamSelection = (team) => {
    setSelectedTeam(team);
  };

  const handleStartGame = () => {
    console.log("Starting game with manager:", managerName, "and team:", selectedTeam);
    setOnboardingStep(3);
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        backgroundColor: "#1e1e1e",
      }}
    >
      {onboardingStep === 0 && <GameMainScreen />} {/* Display GameMainScreen when step is 0 */}
      {onboardingStep === 1 && (
        <ManagerSetup
          managerName={managerName}
          onManagerNameChange={handleManagerNameChange}
          onNext={handleNextStep}
        />
      )}
      {onboardingStep === 2 && (
        <TeamSelection
          selectedTeam={selectedTeam}
          onTeamSelect={handleTeamSelection}
          onNext={handleNextStep}
          onPrevious={handlePreviousStep}
        />
      )}
      {onboardingStep === 3 && (
        <ConfirmationScreen
          managerName={managerName}
          selectedTeam={selectedTeam}
          onConfirm={handleStartGame}
          onPrevious={handlePreviousStep}
        />
      )}
      {onboardingStep === 4 && (
        <>
          <Formation />
          <CurrentTeam />
        </>
      )}
    </Box>
  );
};

export default Game;
