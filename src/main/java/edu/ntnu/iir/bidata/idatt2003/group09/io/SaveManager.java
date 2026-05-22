package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for saving, loading, listing, and deleting game save files.
 */
public class SaveManager {

    private static final Logger LOGGER = Logger.getLogger(SaveManager.class.getName());

    private static final String DEFAULT_FILE = "savegame.dat";
    private static final String SAVE_FILE_PREFIX = "savegame";
    private static final String SAVE_FILE_SUFFIX = ".dat";
    private static final File SAVE_DIR;
        static {
            String appData = System.getenv("APPDATA");
            if (appData == null) appData = System.getProperty("user.home");
            SAVE_DIR = new File(appData, "Millions");
            SAVE_DIR.mkdirs(); 
        }

    static File getSaveDir() {
        return SAVE_DIR;
    }

    static File overrideSaveDir;

    private static File getEffectiveSaveDir() {
        return overrideSaveDir != null ? overrideSaveDir : SAVE_DIR;
    }

    /**
     * Saves the given GameState to the default save file.
     *
     * @param state the GameState to save
     */
    public static void save(GameState state) {
        save(state, DEFAULT_FILE);
    }

    /**
     * Saves the given GameState to the specified file name.
     *
     * @param state    the GameState to save
     * @param fileName the file name to save to
     */
    public static void save(GameState state, String fileName) {
        String targetFile = normalizeSaveFileName(fileName);
        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(targetFile))) {
            out.writeObject(state);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save game to file: " + targetFile, e);
        }
    }

    /**
     * Loads the GameState from the default save file.
     *
     * @return the loaded GameState, or null if loading fails
     */
    public static GameState load() {
        return load(DEFAULT_FILE);
    }

    /**
     * Loads the GameState from the specified file name.
     *
     * @param fileName the file name to load from
     * @return the loaded GameState, or null if loading fails
     */
    public static GameState load(String fileName) {
        String sourceFile = normalizeSaveFileName(fileName);
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(sourceFile))) {

            return (GameState) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Failed to load save from file: " + sourceFile, e);
            return null;
        }
    }

    /**
     * Lists all available save files in the current directory.
     *
     * @return a sorted list of save file names
     */
    public static List<String> listSaveFiles() {
        String[] fileNames = getEffectiveSaveDir().list((dir, name) ->
                name.startsWith(SAVE_FILE_PREFIX) && name.endsWith(SAVE_FILE_SUFFIX));

        if (fileNames == null || fileNames.length == 0) {
            return List.of();
        }

        return Arrays.stream(fileNames)
                .sorted()
                .toList();
    }

    /**
     * Checks if the default save file exists.
     *
     * @return true if the default save file exists, false otherwise
     */
    public static boolean saveExists() {
        return new File(getEffectiveSaveDir(), DEFAULT_FILE).exists();
    }

    /**
     * Normalizes the save file name to ensure it has the correct prefix and suffix, and is safe for file systems.
     *
     * @param fileName the input file name
     * @return the normalized file name
     */
    public static String normalizeSaveFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return new File(getEffectiveSaveDir(), DEFAULT_FILE).getAbsolutePath();
        }

        String normalized = new File(fileName).getName().trim();

        normalized = normalized.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (!normalized.startsWith(SAVE_FILE_PREFIX)) {
            normalized = SAVE_FILE_PREFIX + "-" + normalized;
        }

        if (!normalized.endsWith(SAVE_FILE_SUFFIX)) {
            normalized += SAVE_FILE_SUFFIX;
        }
        return new File(getEffectiveSaveDir(), normalized).getAbsolutePath();
    }

    /**
     * Checks if a save file with the given name exists.
     *
     * @param fileName the file name to check
     * @return true if the file exists, false otherwise
     */
    public static boolean doesSaveFileExist(String fileName) {
        String targetFile = normalizeSaveFileName(fileName);

        return new File(targetFile).exists();
    } 

    /**
     * Deletes the save file with the given name, if it exists.
     *
     * @param fileName the file name to delete
     */
    public static void deleteSaveFile(String fileName) {
        String targetFile = normalizeSaveFileName(fileName);
        File file = new File(targetFile);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.log(Level.WARNING, "Failed to delete save file: " + targetFile);
            }
        }
    }

    /**
     * Returns the number of existing save files.
     *
     * @return the count of save files
     */
    public static int numberOfSaveFiles() {
        return listSaveFiles().size();
    }
}
