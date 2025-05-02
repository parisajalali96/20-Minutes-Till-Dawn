package controllers;

import models.Enums.GameTime;
import models.Enums.Hero;
import models.Enums.Weapon;
import models.Game;
import models.Result;

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

}
