package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;

public class SavaManager {

    private static final String FILE = "savegame.dat";

    public static void save(GameState state) {
        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(state);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE))) {

            return (GameState) in.readObject();

        } catch (Exception e) {
            System.out.println("Failed to load save: " + e.getMessage());
            return null;
        }
    }

    public static boolean saveExists() {
        return new File(FILE).exists();
    }
}
