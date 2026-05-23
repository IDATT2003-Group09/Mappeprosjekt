package edu.ntnu.iir.bidata.idatt2003.group09.io;

/**
 * Summary information for a save file, suitable for presentation in the UI.
 *
 * @param fileName the backing save file name
 * @param displayName the human-friendly name shown in the UI
 * @param netWorthText formatted net worth display text
 * @param weekText formatted week display text
 * @param difficultyText difficulty label shown in the UI
 * @param lost whether the game was already lost when saved
 */
public record SaveGameInfo(
    String fileName,
    String displayName,
    String netWorthText,
    String weekText,
    String difficultyText,
    boolean lost
) {
}