package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.HintMenuController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Player;

public class HintMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private final TextButton exitButton;
    private final HintMenuController controller;
    public HintMenu(HintMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        exitButton = new TextButton("Exit", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.add(exitButton).pad(20).row();
        stage.addActor(table);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(), skin));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.125f, 0.102f, 0.141f, 1f);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
