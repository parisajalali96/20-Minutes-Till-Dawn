package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Main;
import sun.tools.jconsole.Tab;

public class MainMenu implements Screen {
    private Stage stage;
    private final TextButton playButton;
    private final Label gameTitle;
    private final TextField textField;
    public Table table;
    private final MainMenuController controller;

    public MainMenu(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.playButton = new TextButton("Play", skin);
        this.gameTitle = new Label("this is a title", skin);
        this.textField = new TextField("this is a text field", skin);
        this.table = new Table(skin);
        controller.setView(this);
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        table.row().pad(10,0,0,10);
        table.add(gameTitle);
        table.row().pad(10,0,0,10);
        table.add(textField);
        table.row().pad(10,0,0,10);
        table.add(playButton);
        table.row().pad(10,0,0,10);
        stage.addActor(table);
        controller.handleMainMenuButton();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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

    public TextButton getPlayButton() {
        return playButton;
    }
}
