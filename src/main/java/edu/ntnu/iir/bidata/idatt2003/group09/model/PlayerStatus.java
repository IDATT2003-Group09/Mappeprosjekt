package edu.ntnu.iir.bidata.idatt2003.group09.model;

import java.math.BigDecimal;

/**
 * Definerer statusnivåer for spilleren basert på fremgang i spillet.
 */
public enum PlayerStatus {
    NOVICE("Novice"),
    INVESTOR("Investor"),
    SPECULATOR("Speculator");

    private final String displayName;

    PlayerStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Henter visningsnavnet for statusen.
     *
     * @return navn brukt i UI
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sjekker om spilleren fortsatt er nybegynner.
     *
     * @param player aktiv spiller
     * @param weeksPlayed antall spilte uker
     * @return alltid {@code true} så lenge høyere nivå ikke er nådd
     */
    public boolean isNovice(Player player, int weeksPlayed) {
        return true;
    }

    /**
     * Sjekker om spilleren kvalifiserer som investor.
     *
     * @param player aktiv spiller
     * @param weeksPlayed antall spilte uker
     * @return {@code true} ved tilstrekkelig nettoverdi eller antall uker
     */
    public boolean isInvestor(Player player, int weeksPlayed) {
        return player.getNetWorth().compareTo(new BigDecimal("120000")) >= 0 || weeksPlayed >= 10;
    }

    /**
     * Sjekker om spilleren kvalifiserer som spekulant.
     *
     * @param player aktiv spiller
     * @param weeksPlayed antall spilte uker
     * @return {@code true} ved høy nettoverdi eller lang spilletid
     */
    public boolean isSpeculator(Player player, int weeksPlayed) {
        return player.getNetWorth().compareTo(new BigDecimal("200000")) >= 0 || weeksPlayed >= 20;
    }

    /**
     * Bestemmer hvilket statusnavn spilleren skal ha akkurat nå.
     *
     * @param player aktiv spiller
     * @param weeksPlayed antall spilte uker
     * @return statusnavn for spilleren
     */
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