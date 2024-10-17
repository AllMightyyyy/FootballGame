// src/App.js

import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "./components/MainLayout";
import ErrorBoundary from "./components/ErrorBoundary"; 
import { ThemeProvider } from "@mui/material/styles";
import theme from "./theme";
import Game from "./components/Game"; 
import ProtectedRoute from "./components/ProtectedRoute";

const App = () => {
  return (
    <Router>
      <ErrorBoundary>
        <ThemeProvider theme={theme}>
          <Routes>
            <Route path="/" element={<MainLayout />} />
            <Route
              path="/onboarding"
              element={
                <ProtectedRoute>
                  <Game />
                </ProtectedRoute>
              }
            />
            {/* Redirect any unknown routes to home */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </ThemeProvider>
      </ErrorBoundary>
    </Router>
  );
};

export default App;
