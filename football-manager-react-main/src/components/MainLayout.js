// src/components/MainLayout.js

import React, { useState, useContext } from "react";
import {
  AppBar,
  Box,
  Tab,
  Tabs,
  Toolbar,
  Typography,
  Button,
} from "@mui/material";
import SearchPlayersTab from "./SearchPlayersTab";
import PlayerTableTab from "./PlayerTableTab";
import LiveStandingsTab from "./LiveStandingsTab";
import AuthTab from "./AuthTab"; // Handles Login and Sign-Up
import ManageTab from "./ManageTab"; // New Import
import { AuthContext } from "../contexts/AuthContext";
import { LeagueContext } from "../contexts/LeagueContext";

const MainLayout = () => {
  const { auth, logout } = useContext(AuthContext);
  const [selectedTab, setSelectedTab] = useState(0);

  const leagues = useContext(LeagueContext);

  // Define tabs with consistent indices
  const tabs = [
    { label: "Player Table", content: <PlayerTableTab />, protected: false },
    { label: "Live Standings", content: <LiveStandingsTab />, protected: false },
    { label: "Squad Builder", content: <SearchPlayersTab />, protected: false }, // Squad Builder is public
    {
      label: auth.isAuthenticated ? "Manage" : "Login/Sign-Up",
      content: auth.isAuthenticated ? <ManageTab /> : <AuthTab />,
      protected: false,
    },
  ];

  const handleTabChange = (event, newValue) => {
    setSelectedTab(newValue);
  };

  return (
    <Box sx={{ width: "100%" }}>
      {/* AppBar with Logo and Logout Button */}
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Football Manager
          </Typography>
          {auth.isAuthenticated && (
            <Button color="inherit" onClick={logout}>
              Logout
            </Button>
          )}
        </Toolbar>
      </AppBar>

      {/* Tabs Navigation */}
      <Tabs
        value={selectedTab}
        onChange={handleTabChange}
        indicatorColor="primary"
        textColor="inherit"
        centered
      >
        {tabs.map((tab, index) => (
          <Tab
            key={index}
            label={tab.label}
            disabled={tab.protected && !auth.isAuthenticated}
          />
        ))}
      </Tabs>

      {/* Tab Content */}
      <Box sx={{ padding: 3 }}>
        {tabs[selectedTab].protected && !auth.isAuthenticated ? (
          <Typography variant="h6" color="error">
            Please log in to access this feature.
          </Typography>
        ) : (
          tabs[selectedTab].content
        )}
      </Box>
    </Box>
  );
};

export default MainLayout;
