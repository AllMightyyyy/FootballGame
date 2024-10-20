// src/contexts/LeagueContext.js

import React, { createContext, useState, useEffect } from "react";
import { api } from '../api/index';

export const LeagueContext = createContext();

export const LeagueProvider = ({ children }) => {
  const [leagues, setLeagues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLeagues = async () => {
      try {
        const response = await api.get("/teams/all-leagues"); // Correct endpoint
        // Assuming the response is an array of leagues with code, name, and season
        const leaguesData = response.data.map((league) => ({
          code: league.code,
          name: league.name,
          season: league.season,
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
