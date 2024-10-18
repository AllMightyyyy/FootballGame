// src/components/Game.js

import { Box } from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { AuthContext } from "../contexts/AuthContext";
import ConfirmationScreen from "./ConfirmationScreen";
import ManagerSetup from "./ManagerSetup";
import TeamSelection from "./TeamSelection";
import { leagueNameMap } from "./utils/leagueMapping";

const Game = () => {
  const { auth, updateTeam } = useContext(AuthContext);
  const [onboardingStep, setOnboardingStep] = useState(1);
  const [managerName, setManagerName] = useState("");
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [selectedLeague, setSelectedLeague] = useState("en.1");
  const navigate = useNavigate();

  const assignTeam = async () => {
    try {
      const standardLeagueName = leagueNameMap[selectedLeague]; // Translate league code
      const response = await api.post("/teams/assign", {
        league: standardLeagueName,
        teamName: selectedTeam,
      });
      alert(response.data.message);
      const teamResponse = await api.get("/teams/my");
      if (teamResponse.data.team) {
        updateTeam(teamResponse.data.team);
        handleNextStep();
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
        <TeamSelection
          selectedTeam={selectedTeam}
          onTeamSelect={setSelectedTeam}
          onNext={() => {
            // Assign team to user
            assignTeam();
          }}
          onPrevious={handlePreviousStep}
          selectedLeague={selectedLeague}
          setSelectedLeague={setSelectedLeague}
        />
      )}
      {onboardingStep === 3 && (
        <ConfirmationScreen
          managerName={managerName}
          selectedTeam={selectedTeam}
          onConfirm={handleStartGame}
          onPrevious={handlePreviousStep}
          selectedLeague={selectedLeague}
        />
      )}
    </Box>
  );
};

export default Game;
