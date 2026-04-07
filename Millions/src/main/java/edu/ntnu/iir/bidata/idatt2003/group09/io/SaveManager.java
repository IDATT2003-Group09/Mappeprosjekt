package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class SaveManager {

    private static final String DEFAULT_FILE = "savegame.dat";
    private static final String SAVE_FILE_PREFIX = "savegame";
    private static final String SAVE_FILE_SUFFIX = ".dat";

    public static void save(GameState state) {
        save(state, DEFAULT_FILE);
    }

    public static void save(GameState state, String fileName) {
        String targetFile = normalizeSaveFileName(fileName);
        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(targetFile))) {
            out.writeObject(state);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState load() {
        return load(DEFAULT_FILE);
    }

    public static GameState load(String fileName) {
        String sourceFile = normalizeSaveFileName(fileName);
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(sourceFile))) {

            return (GameState) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load save: " + e.getMessage());
            return null;
        }
    }

    public static List<String> listSaveFiles() {
        File currentDirectory = new File(".");
        String[] fileNames = currentDirectory.list((dir, name) ->
                name.startsWith(SAVE_FILE_PREFIX) && name.endsWith(SAVE_FILE_SUFFIX));

        if (fileNames == null || fileNames.length == 0) {
            return List.of();
        }

        return Arrays.stream(fileNames)
                .sorted()
                .toList();
    }

    public static boolean saveExists() {
        return new File(DEFAULT_FILE).exists();
    }

    public static String normalizeSaveFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return DEFAULT_FILE;
        }

        String normalized = fileName.trim();
        normalized = normalized.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (!normalized.startsWith(SAVE_FILE_PREFIX)) {
            normalized = SAVE_FILE_PREFIX + "-" + normalized;
        }

        if (!normalized.endsWith(SAVE_FILE_SUFFIX)) {
            normalized = normalized + SAVE_FILE_SUFFIX;
        }

        return normalized;
    }
}
