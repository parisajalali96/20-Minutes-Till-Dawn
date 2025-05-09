package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.Weapon;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Result;
import models.Enums.GameTime;

public class PreGameMenuController {

    //set hero
    public Result setHero(Hero hero) {
        Game.getCurrentPlayer().setHero(hero);
        return new Result(true, "Hero set successfully!");
    }

    //choose weapon
    public Result chooseWeapon(Weapon weapon) {
        Game.getCurrentPlayer().setWeapon(weapon);
        return new Result(true, "Weapon set successfully!");
    }

    //choose game time
    public Result chooseGameTime(GameTime gameTime) {
        Game.setTime(gameTime);
        return new Result(true, "Game time set successfully!");
    }

    //start game
    public Result startGame(String gameDuration, Hero hero, Weapon weapon) {
        //TODO implement
        return new Result(true, "Game starting successfully!");
    }

}
