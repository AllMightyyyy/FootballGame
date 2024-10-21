// src/api/index.js

import axios from "axios";
import qs from "qs";

export const api = axios.create({
  baseURL: "http://localhost:8081/api", // Base URL for API
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: "repeat" }),
});

// Request Interceptor to add Authorization header
api.interceptors.request.use(
  (config) => {
    // Retrieve the token from localStorage
    const token = localStorage.getItem("token");

    console.log("Intercepted request. Token:", token);

    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 1. Search Players Function
export const searchPlayers = async (filters) => {
  const params = {
    name: filters.name || "",
    positions: filters.position,
    leagues: filters.league,
    clubs: filters.club,
    nations: filters.nation,
    minOverall: filters.rating[0] || 0,
    maxOverall: filters.rating[1] || 99,
    minHeight: filters.height[0] || 150,
    maxHeight: filters.height[1] || 215,
    minWeight: filters.weight[0] || 40,
    maxWeight: filters.weight[1] || 120,
    excludePositions: filters.excludeSelected.position || false,
    excludeLeagues: filters.excludeSelected.league || false,
    excludeClubs: filters.excludeSelected.club || false,
    excludeNations: filters.excludeSelected.nation || false,
    page: filters.page || 1,
    size: filters.size || 50, // Increased size for better UX
    sortBy: filters.sortBy || "overall",
    sortOrder: filters.sortOrder || "asc",
  };

  try {
    const response = await api.get("/players", { params });
    return response.data;
  } catch (error) {
    console.error("Error fetching players:", error);
    throw error;
  }
};

// 2. Get Player by ID
export const getPlayerById = async (id) => {
  try {
    const response = await api.get(`/players/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching player with ID ${id}:`, error);
    throw error;
  }
};

// 3. Add Player to Formation
export const addPlayerToFormation = async (position, playerId) => {
  try {
    const response = await api.post("/players/formation", {
      position,
      playerId,
    });
    return response.data;
  } catch (error) {
    console.error(`Error adding player to formation:`, error);
    throw error;
  }
};

// 4. Remove Player from Formation
export const removePlayerFromFormation = async (position) => {
  try {
    const response = await api.delete("/players/formation", {
      params: { position },
    });
    return response.data;
  } catch (error) {
    console.error(`Error removing player from formation:`, error);
    throw error;
  }
};

// 5. Get the Current Formation
export const getFormation = async () => {
  try {
    const response = await api.get("/players/formation");
    return response.data;
  } catch (error) {
    console.error(`Error fetching formation:`, error);
    throw error;
  }
};

// 6. Assign Team to User
export const assignTeam = async (leagueCode, teamName) => {
  try {
    const response = await api.post("/teams/assign", {
      leagueCode, // Send league code directly
      teamName,
    });
    return response.data;
  } catch (error) {
    console.error("Error assigning team:", error);
    throw error;
  }
};

// 7. Get Assigned Team
export const getAssignedTeam = async () => {
  try {
    const response = await api.get("/teams/my");
    return response.data;
  } catch (error) {
    console.error("Error fetching assigned team:", error);
    throw error;
  }
};

// 8. Get Team Form ( Last 5 Matches )
export const getTeamForm = async (teamName) => {
  try {
    const response = await api.get(`/teams/${teamName}/form`);
    return response.data.form; // Assuming backend returns { form: ["W", "D", "L", "W", "W"] }
  } catch (error) {
    console.error(`Error fetching team form for ${teamName}:`, error);
    throw error;
  }
};

// 9. Get Next Opponent
export const getNextOpponent = async (teamName) => {
  try {
    const response = await api.get(`/teams/${teamName}/next-opponent`);
    return response.data.nextOpponent; // Assuming backend returns { nextOpponent: "Team Name" }
  } catch (error) {
    console.error(`Error fetching next opponent for ${teamName}:`, error);
    throw error;
  }
};

// 10. Get League Qualification Positions
export const getLeagueQualifications = async (leagueCode) => {
  try {
    const response = await api.get(`football/league/${leagueCode}/positions`);
    return response.data; // Assuming backend returns { "Champions League": [...], "Europa League": [...], "Relegation": [...] }
  } catch (error) {
    console.error(
      `Error fetching qualification positions for league ${leagueCode}:`,
      error
    );
    throw error;
  }
};
