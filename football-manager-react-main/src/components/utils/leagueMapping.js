export const leagueNameMap = {
  "en.1": "English Premier League",
  "es.1": "Spain Primera Division",
  "de.1": "German 1. Bundesliga",
  "it.1": "Italian Serie A",
  "fr.1": "French Ligue 1",
  // Add more mappings as needed
};

// Optional: Inverse mapping if needed
export const inverseLeagueNameMap = Object.fromEntries(
  Object.entries(leagueNameMap).map(([key, value]) => [value, key])
);
