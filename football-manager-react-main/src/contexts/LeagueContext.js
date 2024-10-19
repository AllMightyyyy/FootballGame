// src/contexts/LeagueContext.js

import React, { createContext, useState, useEffect } from "react";
import api from "../api";
import { inverseLeagueNameMap } from "../components/utils/leagueMapping";

export const LeagueContext = createContext();

export const LeagueProvider = ({ children }) => {
  const [leagues, setLeagues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLeagues = async () => {
      try {
        const response = await api.get("/football/standings"); // Adjust endpoint if necessary
        // Assuming the response is an array of LeagueDTO
        const leaguesData = response.data.map((league) => ({
          code: inverseLeagueNameMap[league.name], // Map full name back to code
          name: league.name,
          season: "2024/25", // Or extract from league.name if possible
        }));
        setLeagues(leaguesData);
      } catch (err) {
        setError("Failed to fetch leagues.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchLeagues();
  }, []);

  return (
    <LeagueContext.Provider value={leagues}>
      {loading ? (
        <div>Loading leagues...</div>
      ) : error ? (
        <div style={{ color: "red" }}>{error}</div>
      ) : (
        children
      )}
    </LeagueContext.Provider>
  );
};
