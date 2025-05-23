package io.github.parisajalali96.Controllers;

import com.badlogic.gdx.graphics.Texture;

import java.io.*;

public class GameInfoSaveManager implements Serializable {
    private static final String SAVE_FOLDER = "saves";

    private String saveName;
    private int killCount;
    private float secondsLeft;
    private String avatarImage;

    public GameInfoSaveManager(String saveName, int killCount, float secondsLeft, String avatarImage) {
        this.saveName = saveName;
        this.killCount = killCount;
        this.secondsLeft = secondsLeft;
        this.avatarImage = avatarImage;
    }

    public String getSaveName() {
        return saveName;
    }

    public int getKillCount() {
        return killCount;
    }

    public float getSecondsLeft() {
        return secondsLeft;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public static GameInfoSaveManager loadGameInfo(String name) throws IOException, ClassNotFoundException {
        File infoFile = new File(SAVE_FOLDER + "/" + name + "/info.dat");
        if (!infoFile.exists()) {
            throw new FileNotFoundException("No info.dat found for saved game: " + name);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(infoFile))) {
            return (GameInfoSaveManager) in.readObject();
        }
    }

}
