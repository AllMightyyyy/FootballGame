// src/contexts/AuthContext.js

import React, { createContext, useEffect, useState } from "react";
import { api } from "../api/index";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    isAuthenticated: false,
    user: null,
    token: null,
    team: null,
  });

  useEffect(() => {
    // Check for token and user in localStorage
    const token = localStorage.getItem("token");
    const userData = localStorage.getItem("user");

    if (token && userData) {
      try {
        const user = JSON.parse(userData);
        setAuth({
          isAuthenticated: true,
          user,
          token,
          team: user.team || null,
        });
        api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      } catch (error) {
        console.error("Error parsing user data from localStorage:", error);
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
      const response = await api.post("/auth/login", credentials);

      if (!response.data || !response.data.token) {
        console.error("Unexpected response structure:", response.data);
        return {
          success: false,
          message: "Invalid response from server. Please contact support.",
        };
      }

      const { token } = response.data;
      localStorage.setItem("token", token);
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      const userResponse = await api.get("/auth/me");

      if (!userResponse.data) {
        console.error("Failed to fetch user information:", userResponse.data);
        return {
          success: false,
          message: "Failed to fetch user information.",
        };
      }

      const user = userResponse.data;
      const team = user.team || null;

      setAuth({
        isAuthenticated: true,
        user,
        token,
        team,
      });

      localStorage.setItem("user", JSON.stringify(user));

      return { success: true, hasTeam: !!team };
    } catch (error) {
      console.error("Login failed with error:", error);
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

      if (!response.data || !response.data.token) {
        console.error("Unexpected response structure:", response.data);
        return {
          success: false,
          message: "Invalid response from server. Please contact support.",
        };
      }

      const { token, user } = response.data;

      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user));
      setAuth({
        isAuthenticated: true,
        user,
        token,
        team: user.team || null,
      });

      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      return { success: true, hasTeam: !!user.team };
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
