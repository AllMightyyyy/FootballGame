// src/components/Game.js

import { Box } from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { assignTeam as apiAssignTeam, getAssignedTeam } from "../api";
import { AuthContext } from "../contexts/AuthContext";
import { LeagueProvider } from "../contexts/LeagueContext";
import ConfirmationScreen from "./ConfirmationScreen";
import ManagerSetup from "./ManagerSetup";
import TeamSelection from "./TeamSelection";

const Game = () => {
  const { auth, updateTeam } = useContext(AuthContext);
  const [onboardingStep, setOnboardingStep] = useState(1);
  const [managerName, setManagerName] = useState("");
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [selectedLeague, setSelectedLeague] = useState("en.1");
  const navigate = useNavigate();

  const handleAssignTeam = async () => {
    try {
      if (selectedTeam) {
        const response = await apiAssignTeam(selectedLeague, selectedTeam);
        alert(response.message);

        // Fetch the assigned team
        const teamResponse = await getAssignedTeam();
        if (teamResponse.team) {
          updateTeam(teamResponse.team);
        }

        // Finalize onboarding and navigate to the main game interface
        navigate("/");
      } else {
        // If no team is selected, allow skipping
        navigate("/");
      }
    } catch (error) {
      console.error("Error assigning team:", error);
      alert(error.response?.data?.message || "Failed to assign team.");
    }
  };

  useEffect(() => {
    if (!auth.isAuthenticated) {
      navigate("/login");
    } else if (auth.team) {
      navigate("/");
    }
  }, [auth, navigate]);

  const handleNextStep = () => {
    setOnboardingStep((prev) => prev + 1);
  };

  const handlePreviousStep = () => {
    setOnboardingStep((prev) => prev - 1);
  };

  const handleManagerNameChange = (name) => {
    setManagerName(name);
  };

  const handleTeamSelection = (team) => {
    setSelectedTeam(team);
  };

  const handleStartGame = () => {
    console.log(
      "Starting game with manager:",
      managerName,
      "and team:",
      selectedTeam
    );
    navigate("/");
  };

  console.log("selectedTeam:", selectedTeam);
  console.log("setSelectedTeam function:", setSelectedTeam);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        backgroundColor: "#1e1e1e",
        padding: 3,
      }}
    >
      {onboardingStep === 1 && (
        <ManagerSetup
          managerName={managerName}
          onManagerNameChange={handleManagerNameChange}
          onNext={handleNextStep}
        />
      )}
      {onboardingStep === 2 && (
        <LeagueProvider>
          <TeamSelection
            selectedTeam={selectedTeam}
            onTeamSelect={setSelectedTeam}
            onNext={handleAssignTeam}
            onPrevious={handlePreviousStep}
            selectedLeague={selectedLeague}
            setSelectedLeague={setSelectedLeague}
            setSelectedTeam={setSelectedTeam}
          />
        </LeagueProvider>
      )}
      {onboardingStep === 3 && (
        <ConfirmationScreen
          managerName={managerName}
          selectedTeam={selectedTeam}
          onConfirm={handleStartGame}
          onPrevious={handlePreviousStep}
          selectedLeague={selectedLeague}
          allowSkip={true} // Allow skipping team assignment
        />
      )}
    </Box>
  );
};

export default Game;
