import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8081/api",
});

export const searchPlayers = (params) => api.get("/players", { params });
export const getPlayerById = (id) => api.get(`/players/${id}`);
export const addPlayerToFormation = (position, playerId) =>
  api.post("/players/formation", { position, playerId });
export const removePlayerFromFormation = (position) =>
  api.delete(`/players/formation`, { params: { position } });
export const getFormation = () => api.get("/players/formation");
