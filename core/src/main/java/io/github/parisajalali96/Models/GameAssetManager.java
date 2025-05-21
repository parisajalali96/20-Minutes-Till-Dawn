package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public static Animation<TextureRegion> getHeartAnimation(){
        TextureRegion[] hearts = new TextureRegion[3];
        hearts[0] = new TextureRegion(new Texture("Images/Sprite/HeartAnimation_0.png"));
        hearts[1] = new TextureRegion(new Texture("Images/Sprite/HeartAnimation_1.png"));
        hearts[2] = new TextureRegion(new Texture("Images/Sprite/HeartAnimation_2.png"));
        return new Animation<>(0.25f, hearts);
    }

    public static TextureRegion getEmptyHeart(){
        return new TextureRegion(new Texture("Images/Sprite/HeartAnimation_3.png"));
    }
}
