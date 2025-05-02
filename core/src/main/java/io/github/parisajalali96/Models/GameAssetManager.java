package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    private GameAssetManager gameAssetManager;
    private final Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

    public Skin getSkin() {
        return skin;
    }

    public GameAssetManager getGameAssetManager() {
        if(gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }
}
