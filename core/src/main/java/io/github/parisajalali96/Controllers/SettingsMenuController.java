package io.github.parisajalali96.Controllers;


import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Result;
import io.github.parisajalali96.Views.SettingsMenu;

public class SettingsMenuController {
    private SettingsMenu view;

    public void setView(SettingsMenu view) {
        this.view = view;
    }
    //change music volume
    public Result changeMusicVolume(){
        //TODO implement
        return null;
    }

    //change music
    public void changeMusic(String musicName){
        GameAssetManager.getGameAssetManager().setMusic(musicName);
    }

    //change sfx state
    public void changeSfx(boolean sfx){
        GameAssetManager.getGameAssetManager().setSfxEnabled(sfx);
    }

    //change game controllers
    public Result changeGameControllers(){
        //TODO implement
        return null;
    }

    //add listeners
    public void addListeners() {}



}
