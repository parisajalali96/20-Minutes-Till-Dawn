package io.github.parisajalali96;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Views.MainMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;
    private static GameAssetManager gameAssetManager;

    public static void setBatch(SpriteBatch batch) {
        Main.batch = batch;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }


    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        main.setScreen(new MainMenu(new MainMenuController(), gameAssetManager.getSkin()));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
