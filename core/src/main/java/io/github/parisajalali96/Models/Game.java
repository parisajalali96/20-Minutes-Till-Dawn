package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import models.Enums.GameTime;
import models.Enums.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {


    private static GameTime time = GameTime.TWO;
    private static Player currentPlayer;
    private static final ArrayList<Player> players = new ArrayList<>();
    private static Menu currentMenu;
    private static float secondsPassed = 0f;
    private static float countdown = time.getTotalSeconds();

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
    public static void addScore(int score) {
        Game.getCurrentPlayer().getUser().addTotalScore(score);
    }
    public static float getSecondsPassed() {
        return secondsPassed;
    }
    public static void addSecondsPassed(float seconds) {
        secondsPassed += seconds;
    }

    public static void update(float delta) {
        secondsPassed += delta;

        //countdown
        if (countdown > 0) {
            countdown -= delta;
            if (countdown < 0) countdown = 0;
        }

        //TODO implement end game
    }

    public static float getCountdownTime() {
        return countdown;
    }

    public static void resetCountdown(float seconds) {
        countdown = seconds;
    }


}
