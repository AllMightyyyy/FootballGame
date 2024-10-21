// src/contexts/AuthContext.js

import React, { createContext, useEffect, useState } from "react";
import { api } from "../api/index";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    isAuthenticated: false,
    user: null,
    token: null,
    team: null, // New field to store team information
  });

  useEffect(() => {
    // Check for token and user in localStorage
    const token = localStorage.getItem("token");
    const userData = localStorage.getItem("user"); // Get raw data

    if (token && userData) {
      try {
        const user = JSON.parse(userData); // Only parse if data is not null
        setAuth({
          isAuthenticated: true,
          user,
          token,
          team: user.team || null,
        });
        api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      } catch (error) {
        console.error("Error parsing user data from localStorage:", error);
        // If parsing fails, clear out the localStorage and reset state
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setAuth({
          isAuthenticated: false,
          user: null,
          token: null,
          team: null,
        });
      }
    }
  }, []);

  const login = async (credentials) => {
    try {
      // Make login request
      const response = await api.post("/auth/login", credentials);
      console.log("Login Response:", response); // Log the full response for debugging

      // Verify the response structure and store the token
      if (!response.data || !response.data.token) {
        console.error("Unexpected response structure:", response.data);
        return {
          success: false,
          message: "Invalid response from server. Please contact support.",
        };
      }

      const { token } = response.data;
      localStorage.setItem("token", token);

      // Set the token in the api instance for subsequent requests
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      // Additional fetch to get user details using the token
      const userResponse = await api.get("/auth/me");
      console.log("User Response:", userResponse); // Log user response for debugging

      if (!userResponse.data) {
        console.error("Failed to fetch user information:", userResponse.data);
        return {
          success: false,
          message: "Failed to fetch user information.",
        };
      }

      const user = userResponse.data; // Directly assign data since it's the user object
      const team = user.team || null;

      // Update the state with user info and team info if available
      setAuth({
        isAuthenticated: true,
        user,
        token,
        team,
      });

      // Save user information in localStorage
      localStorage.setItem("user", JSON.stringify(user));

      return { success: true, hasTeam: !!team };
    } catch (error) {
      console.error("Login failed with error:", error);
      // Ensure message extraction is always reliable
      const message =
        error.response?.data?.message ||
        "Login failed due to an unknown error.";
      return {
        success: false,
        message,
      };
    }
  };

  const register = async (userData) => {
    try {
      const response = await api.post("/auth/register", userData);
      const { token, user } = response.data;
      console.log("Registration successful. Response data:", response.data);
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user));
      setAuth({
        isAuthenticated: true,
        user,
        token,
        team: user.team || null,
      });
      // Set the token in the api instance
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      // Fetch additional user info, including team status
      const teamResponse = await api.get("/auth/me");
      const userInfo = teamResponse.data.user;
      const team = teamResponse.data.team;

      if (team) {
        setAuth({
          isAuthenticated: true,
          user: userInfo,
          token,
          team,
        });
        return { success: true, hasTeam: true };
      } else {
        return { success: true, hasTeam: false };
      }
    } catch (error) {
      console.error("Registration failed:", error);
      return {
        success: false,
        message: error.response?.data?.message || "Registration failed.",
      };
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setAuth({
      isAuthenticated: false,
      user: null,
      token: null,
      team: null,
    });
    // Remove the token from the api instance
    delete api.defaults.headers.common["Authorization"];
  };

  const updateTeam = (team) => {
    setAuth((prev) => ({
      ...prev,
      team,
      user: { ...prev.user, team }, // Update user data with team
    }));
    // Update user in localStorage
    const updatedUser = { ...auth.user, team };
    localStorage.setItem("user", JSON.stringify(updatedUser));
  };

  return (
    <AuthContext.Provider value={{ auth, login, register, logout, updateTeam }}>
      {children}
    </AuthContext.Provider>
  );
};
