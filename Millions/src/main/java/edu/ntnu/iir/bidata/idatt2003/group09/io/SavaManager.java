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
}
