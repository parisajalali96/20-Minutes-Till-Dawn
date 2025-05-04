package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Player;
import sun.tools.jconsole.Tab;

public class MainMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Player currentPlayer;
    private final SelectBox<String> menuSelector;
    private final Image avatar;
    private final Label username;
    private final Label score;
    private final TextButton logoutButton;
    public Table mainTable;
    private final MainMenuController controller;

    public MainMenu(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.currentPlayer = Game.getCurrentPlayer();
        menuSelector = new SelectBox<>(skin);
        menuSelector.setItems(
            "* menus *",
            "Settings",
            "Profile",
            "Pre-Game",
            "ScoreBoard",
            "Hint"
        );
        avatar = new Image(new TextureRegionDrawable
            (new TextureRegion(currentPlayer.getHero().getTexture())));

        username = new Label("Player: " + currentPlayer.getUsername(), skin);
        score = new Label("Score: " + currentPlayer.getScore(), skin);

        logoutButton = new TextButton("Logout", skin);

        this.mainTable = new Table(skin);
        controller.setView(this);
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);
        mainTable.top().padTop(30);

        Table infoTable = new Table(skin);
        infoTable.add(avatar).size(64, 64).padRight(20);
        infoTable.add(username).left().padBottom(5).row();
        infoTable.add().padRight(20);
        infoTable.add(score).left();
        mainTable.add(infoTable).left().pad(20).row();

        stage.addActor(mainTable);

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

}
