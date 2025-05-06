package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    private Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
    private Music music;
    private boolean sfxEnabled;

    public float getMusicVolume() {
       // return music.getVolume();
        return 10;
    }

    public void setMusicVolume(float volume) {
        //music.setVolume(volume);
    }

    public boolean isSfxEnabled() {
        //TODO implement
        return sfxEnabled;
    }

    public String getMusic() {
        return "music";
    }

    public void setMusic(String music) {
        //TODO implement this
    }

    public void setSfxEnabled(boolean sfxEnabled) {
        this.sfxEnabled = sfxEnabled;
    }

    public Skin getSkin() {
        return skin;
    }

    public static GameAssetManager getGameAssetManager() {
        if(gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }
}
