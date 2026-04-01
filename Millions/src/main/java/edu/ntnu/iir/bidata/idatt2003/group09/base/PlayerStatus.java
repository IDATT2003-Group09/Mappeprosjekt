package edu.ntnu.iir.bidata.idatt2003.group09.base;

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
}