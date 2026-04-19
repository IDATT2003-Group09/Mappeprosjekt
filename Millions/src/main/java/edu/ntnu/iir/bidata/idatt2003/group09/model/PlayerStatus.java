package edu.ntnu.iir.bidata.idatt2003.group09.model;

public enum PlayerStatus {
    NOVICE("Novice"),
    INVESTOR("Investor"),
    SPECULATOR("Speculator");

    private final String displayName;

    PlayerStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isNovice(Player player, int weeksPlayed) {
        return true;
    }

    public boolean isInvestor(Player player, int weeksPlayed) {
        return player.getNetWorth().compareTo(new java.math.BigDecimal("120000")) >= 0 || weeksPlayed >= 10;
    }

    public boolean isSpeculator(Player player, int weeksPlayed) {
        return player.getNetWorth().compareTo(new java.math.BigDecimal("200000")) >= 0 || weeksPlayed >= 20;
    }

    public static String determineStatus(Player player, int weeksPlayed) {
        if (SPECULATOR.isSpeculator(player, weeksPlayed)) {
            return SPECULATOR.getDisplayName();
        } else if (INVESTOR.isInvestor(player, weeksPlayed)) {
            return INVESTOR.getDisplayName();
        } else {
            return NOVICE.getDisplayName();
        }
    }
}