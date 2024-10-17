// src/components/AuthTab.js

import React, { useState } from "react";
import { Box, Tabs, Tab } from "@mui/material";
import LoginScreen from "./LoginScreen";
import GameMainScreen from "./GameMainScreen"; 

const AuthTab = () => {
  const [selectedTab, setSelectedTab] = useState(0);

  const handleTabChange = (event, newValue) => {
    setSelectedTab(newValue);
  };

  return (
    <Box>
      <Tabs
        value={selectedTab}
        onChange={handleTabChange}
        indicatorColor="primary"
        textColor="primary"
        centered
      >
        <Tab label="Login" />
        <Tab label="Sign-Up" />
      </Tabs>
      <Box sx={{ padding: 3 }}>
        {selectedTab === 0 && <LoginScreen />}
        {selectedTab === 1 && <GameMainScreen />}
      </Box>
    </Box>
  );
};

export default AuthTab;
