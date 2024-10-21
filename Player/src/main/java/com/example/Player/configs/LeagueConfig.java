package com.example.Player.configs;

import java.util.Map;

public class LeagueConfig {
    private Map<String, PromotionRelegationConfig> promotionRelegationPositions;

    public LeagueConfig(Map<String, PromotionRelegationConfig> promotionRelegationPositions) {
        this.promotionRelegationPositions = promotionRelegationPositions;
    }

    public static class PromotionRelegationConfig {
        private int championsLeagueStart;
        private int championsLeagueEnd;
        private int europaLeagueStart;
        private int europaLeagueEnd;
        private int conferenceLeagueStart;
        private int relegationStart;

        // Getters and setters

        public int getChampionsLeagueStart() {
            return championsLeagueStart;
        }

        public void setChampionsLeagueStart(int championsLeagueStart) {
            this.championsLeagueStart = championsLeagueStart;
        }

        public int getChampionsLeagueEnd() {
            return championsLeagueEnd;
        }

        public void setChampionsLeagueEnd(int championsLeagueEnd) {
            this.championsLeagueEnd = championsLeagueEnd;
        }

        public int getEuropaLeagueStart() {
            return europaLeagueStart;
        }

        public void setEuropaLeagueStart(int europaLeagueStart) {
            this.europaLeagueStart = europaLeagueStart;
        }

        public int getEuropaLeagueEnd() {
            return europaLeagueEnd;
        }

        public void setEuropaLeagueEnd(int europaLeagueEnd) {
            this.europaLeagueEnd = europaLeagueEnd;
        }

        public int getConferenceLeagueStart() {
            return conferenceLeagueStart;
        }

        public void setConferenceLeagueStart(int conferenceLeagueStart) {
            this.conferenceLeagueStart = conferenceLeagueStart;
        }

        public int getRelegationStart() {
            return relegationStart;
        }

        public void setRelegationStart(int relegationStart) {
            this.relegationStart = relegationStart;
        }
    }

    public PromotionRelegationConfig getPositionsForLeague(String leagueCode) {
        return promotionRelegationPositions.get(leagueCode);
    }
}
