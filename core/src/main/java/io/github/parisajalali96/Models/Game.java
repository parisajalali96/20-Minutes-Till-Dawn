package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import models.Enums.GameTime;
import models.Enums.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {


    private static GameTime time = GameTime.TWENTY;
    private static Player currentPlayer;
    private static ArrayList<Player> players = new ArrayList<>();
    private static Menu currentMenu;

    public static List<User> getRegisteredUsers() throws IOException {
        return UserStorage.loadUsers();
    }

    public static Menu getCurrentMenu() {
        return currentMenu;
    }
    public static void setCurrentMenu(Menu currentMenu) {
        Game.currentMenu = currentMenu;
    }
    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }
    public static GameTime getTime() {
        return time;
    }
    public static void setTime(GameTime time) {
        Game.time = time;
    }
}
