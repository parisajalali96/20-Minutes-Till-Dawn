package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.Enums.GameTime;
import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.WeaponType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Result;
import io.github.parisajalali96.Models.Weapon;

public class PreGameMenuController {

    //set hero
    public Result setHero(Hero hero) {
        Game.getCurrentPlayer().setHero(hero);
        return new Result(true, "Hero set successfully!");
    }

    //choose weapon
    public Result chooseWeapon(WeaponType weapon) {
        Game.getCurrentPlayer().setWeapon(new Weapon(weapon));
        return new Result(true, "Weapon set successfully!");
    }

    //choose game time
    public Result chooseGameTime(GameTime gameTime) {
        Game.setTime(gameTime);
        return new Result(true, "Game time set successfully!");
    }

    //start game
    public Result startGame(String gameDuration, Hero hero, WeaponType weapon) {
        //TODO implement
        return new Result(true, "Game starting successfully!");
    }

}
