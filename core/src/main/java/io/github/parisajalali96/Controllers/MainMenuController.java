package controllers;

import models.Enums.Menu;
import models.Game;
import models.Result;

public class MainMenuController {

    //set current menu
    public Result setCurrentMenu(Menu menu){
        if(menu == Menu.SettingsMenu || menu == Menu.ProfileMenu ||
                menu == Menu.PreGameMenu || menu == Menu.ScoreBoardMenu
        || menu == Menu.HintMenu) {
            Game.setCurrentMenu(menu);
            return new Result(true, "");
        }
        return new Result(false, "");
    }

    //continue saved game
    public Result continueSavedGame(){
        //TODO implement
        return null;
    }

    //show user info
    public Result showUserInfo(){
        //TODO implement
        return null;
    }

    //logout
    public Result logout(){
        Game.setCurrentPlayer(null);
        return new Result(true, "You're now logged out.");
    }


}
