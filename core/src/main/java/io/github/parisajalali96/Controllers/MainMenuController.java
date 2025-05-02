package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Result;
import io.github.parisajalali96.Views.MainMenu;
import models.Enums.Menu;


public class MainMenuController {
    private MainMenu view;

    public void setView(MainMenu view) {
        this.view = view;
    }

    public void handleMainMenuButton() {
        Main main = new Main();
        if(view != null) {
            if(view.getPlayButton().isChecked() && view.getPlayButton().getText().equals("Play")) {
               // main.getScreen().dispose();
            }
        }
    }
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
