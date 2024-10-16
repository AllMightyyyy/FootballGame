// src/api/index.js

import axios from "axios";
import qs from "qs";
import { leagueNameMap } from "../components/utils/leagueMapping"; // Import the mapping

const api = axios.create({
  baseURL: "http://localhost:8081/api", // Base URL for API
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: "repeat" }),
});

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
  // Map frontend league codes to backend standard names
  const mappedLeagues = filters.league.map(
    (leagueCode) => leagueNameMap[leagueCode] || leagueCode
  );

  const params = {
    name: filters.name || "", // Player name filter
    positions: filters.position, // Array of positions
    leagues: mappedLeagues, // Array of mapped leagues
    clubs: filters.club, // Array of clubs
    nations: filters.nation, // Array of nations
    minOverall: filters.rating[0] || 0, // Minimum rating
    maxOverall: filters.rating[1] || 99, // Maximum rating
    minHeight: filters.height[0] || 150, // Minimum height
    maxHeight: filters.height[1] || 215, // Maximum height
    minWeight: filters.weight[0] || 40, // Minimum weight
    maxWeight: filters.weight[1] || 120, // Maximum weight
    excludePositions: filters.excludeSelected.position || false, // Exclude selected positions
    excludeLeagues: filters.excludeSelected.league || false, // Exclude selected leagues
    excludeClubs: filters.excludeSelected.club || false, // Exclude selected clubs
    excludeNations: filters.excludeSelected.nation || false, // Exclude selected nations
    page: filters.page || 1, // Pagination: current page
    size: filters.size || 10, // Pagination: page size
    sortBy: filters.sortBy || "overall", // Sort by field
    sortOrder: filters.sortOrder || "asc", // Sort order
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

// Export the Axios instance for potential direct use
export default api;
