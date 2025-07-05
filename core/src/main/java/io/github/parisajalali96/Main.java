package io.github.parisajalali96;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.parisajalali96.Controllers.RegisterMenuController;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Views.RegisterMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;

    public static Main getMain() {
        return main;
    }

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
        main.setScreen(new RegisterMenu(new RegisterMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
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
