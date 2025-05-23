package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.Enums.AbilityType;
import io.github.parisajalali96.Models.Enums.EnemyType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Views.GameView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController {
    private GameView view;

    public void setView(GameView view) {
        this.view = view;
    }

    public static List<AbilityType> get3RandomAbilities() {

        List<AbilityType> available = new ArrayList<>(Arrays.asList(AbilityType.values()));

        Collections.shuffle(available);
        return available.subList(0, Math.min(3, available.size()));
    }

    public void endGame(boolean win) {
        Game.getCurrentPlayer().addScore((int) (Game.getSecondsPassed()*Game.getCurrentPlayer().getKills()));
        Game.getCurrentPlayer().getUser().addKills(Game.getCurrentPlayer().getKills());
        Game.getCurrentPlayer().getUser().setLongestSurvivalScore(Game.getSecondsPassed());
        Game.getCurrentPlayer().reset();
        view.endGameWindow(win);
        Game.resetGame();

    }

    //increases passed time one minute
    public void gameTimeCheatCode(){
        Game.addSecondsPassed(60f);
    }

    //add player level
    public void playerLevelCheatCode(){
        Game.getCurrentPlayer().addXp
            (Game.getCurrentPlayer().getLevel()*20 - Game.getCurrentPlayer().getXp());
    }

    //add HP
    public void playerHPCheatCode(){
        if(Game.getCurrentPlayer().getHealth() == 0) Game.getCurrentPlayer().
            addHealth(Game.getCurrentPlayer().getHero().getHP());
    }

    //initialize boss fight
    public void bossFightCheatCode(){
        Game.getMap().spawnEnemy(EnemyType.Elder, Game.getMap().getRandomSpawnPosition());
    }

    //extra cheat code (extra speed)
    public void doubleMaxSpeedCheatCode(){
        Game.getCurrentPlayer().addSpeed(Game.getCurrentPlayer().getSpeed());
    }





}
