package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Centralizes save-file loading and presentation data for UI screens.
 */
public class SaveGameService {

    public List<SaveGameInfo> listSaveGames() {
        return SaveManager.listSaveFiles().stream()
            .map(this::toSaveGameInfo)
            .toList();
    }

    public int countSaveFiles() {
        return SaveManager.numberOfSaveFiles();
    }

    public boolean doesSaveFileExist(String fileName) {
        return SaveManager.doesSaveFileExist(fileName);
    }

    public void deleteSaveFile(String fileName) {
        SaveManager.deleteSaveFile(fileName);
    }

    public GameState load(String fileName) {
        return SaveManager.load(fileName);
    }

    private SaveGameInfo toSaveGameInfo(String fileName) {
        String displayName = cleanName(fileName);
        String netWorthText = "";
        String weekText = "";
        String difficultyText = "";
        boolean lost = false;

        try {
            GameState state = SaveManager.load(fileName);
            if (state != null) {
                BigDecimal netWorth = state.getNetWorth();
                netWorthText = netWorth == null ? "" : String.format("$%.2f", netWorth);
                weekText = String.format("Week: %d", state.getWeek());
                difficultyText = state.getDifficulty() == null ? "" : state.getDifficulty();
                lost = state.isLost();
            } else {
                weekText = "Could not read save";
            }
        } catch (Exception e) {
            weekText = "Could not read save";
        }

        return new SaveGameInfo(fileName, displayName, netWorthText, weekText, difficultyText, lost);
    }

    private String cleanName(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        String fileOnly = new File(filename).getName();

        int lastDotIndex = fileOnly.lastIndexOf('.');
        String nameWithoutExtension =
                lastDotIndex != -1
                        ? fileOnly.substring(0, lastDotIndex)
                        : fileOnly;

        if (nameWithoutExtension.startsWith("savegame-")) {
            return nameWithoutExtension.substring("savegame-".length());
        }

        return nameWithoutExtension;
    }
}