import axios from "axios";
import qs from "qs";

const api = axios.create({
  baseURL: "http://localhost:8081/api",
  paramsSerializer: (params) =>
    qs.stringify(params, { arrayFormat: "repeat" }),
});

// 1. Search Players Function
export const searchPlayers = (filters) => {
  const params = {
    name: filters.name || "", // Player name filter
    positions: filters.position, // Array of positions
    leagues: filters.league, // Array of leagues
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

  return api.get("/players", { params });
};

// 2. Get Player by ID
export const getPlayerById = (id) => api.get(`/players/${id}`);

// 3. Add Player to Formation
export const addPlayerToFormation = (position, playerId) =>
  api.post("/players/formation", { position, playerId });

// 4. Remove Player from Formation
export const removePlayerFromFormation = (position) =>
  api.delete("/players/formation", { params: { position } });

// 5. Get the Current Formation
export const getFormation = () => api.get("/players/formation");
