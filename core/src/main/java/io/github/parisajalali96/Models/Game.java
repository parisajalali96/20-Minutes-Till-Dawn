package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.parisajalali96.Controllers.GameController;
import io.github.parisajalali96.Views.GameView;
import models.Enums.GameTime;
import models.Enums.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {


    private static GameTime time = GameTime.TWO;
    private static GameMap map = new GameMap();
    private static Player currentPlayer;
    private static final ArrayList<Player> players = new ArrayList<>();
    private static Menu currentMenu;
    private static float secondsPassed = 0f;
    private static float countdown = time.getTotalSeconds();
    private static GameView gameView = new GameView();

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
        addSecondsPassed(delta);

        //countdown
        if (getCountdownTime() < 0) {
            secondsPassed = time.getTotalSeconds();
            Game.getGameView().getController().endGame(true);
        }

        //TODO implement end game
    }

    public static float getCountdownTime() {
        return time.getTotalSeconds() - secondsPassed;
    }

    public static void resetCountdown(float seconds) {
        countdown = seconds;
    }
    public static GameMap getMap() {
        return map;
    }

    public static GameView getGameView() {
        return gameView;
    }

    public static void setGameView(GameView gameView) {
        Game.gameView = gameView;
    }




}
