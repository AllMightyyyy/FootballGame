export const leagueNameMap = {
  "en.1": "English Premier League 2024/25",
  "es.1": "Spain Primera Division 2024/25",
  "de.1": "German 1. Bundesliga 2024/25",
  "it.1": "Italian Serie A 2024/25",
  "fr.1": "French Ligue 1 2024/25",
  // Add more mappings as needed
};

// Optional: Inverse mapping if needed
export const inverseLeagueNameMap = Object.fromEntries(
  Object.entries(leagueNameMap).map(([key, value]) => [value, key])
);
