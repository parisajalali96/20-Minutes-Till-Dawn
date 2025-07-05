package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameMap;
import io.github.parisajalali96.Models.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaveGameManager {
    private static final String SAVE_FOLDER = "saves";
    private static final String INDEX_FILE = "saves/index.txt";

    public static void saveGame(Player player, GameMap map, String name) {
        try {
            File dir = new File(SAVE_FOLDER, name);
            dir.mkdirs();

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(dir, "player.dat")))) {
                out.writeObject(player);
            }

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(dir, "map.dat")))) {
                out.writeObject(map);
            }

            GameInfoSaveManager info = new GameInfoSaveManager(
                name,
                player.getKills(),
                Game.getCountdownTime(),
                player.getHero().getPortraitTexturePath()
            );

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(dir, "info.dat")))) {
                out.writeObject(info);
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateIndex(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void updateIndex(String name) throws IOException {
        File index = new File(INDEX_FILE);
        List<String> saves = new ArrayList<>();

        if (index.exists()) {
            saves.addAll(Files.readAllLines(index.toPath()));
        }

        if (!saves.contains(name)) {
            saves.add(name);
            Files.write(index.toPath(), saves);
        }
    }

    public static List<String> getSavedGames() throws IOException {
        File index = new File(INDEX_FILE);
        if (!index.exists()) return new ArrayList<>();
        return Files.readAllLines(index.toPath());
    }

    public static Player loadPlayer(String name) throws IOException, ClassNotFoundException {
        File file = new File(SAVE_FOLDER + "/" + name, "player.dat");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Player) in.readObject();
        }
    }

    public static GameMap loadMap(String name) throws IOException, ClassNotFoundException {
        File file = new File(SAVE_FOLDER + "/" + name, "map.dat");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (GameMap) in.readObject();
        }
    }

    public static void loadGame(String name) throws IOException, ClassNotFoundException {
        File dir = new File(SAVE_FOLDER, name);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException("Save folder not found: " + name);
        }

        Player loadedPlayer;
        GameMap loadedMap;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(dir, "player.dat")))) {
            loadedPlayer = (Player) in.readObject();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(dir, "map.dat")))) {
            loadedMap = (GameMap) in.readObject();
        }

        GameInfoSaveManager info = GameInfoSaveManager.loadGameInfo(name);
        Game.setCurrentPlayer(loadedPlayer);
        loadedPlayer.initPLayer();
        Game.setMap(loadedMap);
        loadedMap.initMap();
        Game.getGameView().setPaused(false);
        Game.setCountdown(info.getSecondsLeft());
        Main.getMain().setScreen(Game.getGameView());

    }

}

