package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.*;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Player;

import java.io.IOException;

public class MainMenu implements Screen {
    private Stage stage;
    private final Skin skin;
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
        Player currentPlayer = Game.getCurrentPlayer();
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
        infoTable.add(username).left().padBottom(5).row();
        infoTable.add(score).left().padBottom(5).row();

        mainTable.add(infoTable).left().pad(20).row();

        mainTable.add(avatar).size(300, 300).padTop(10).row();

        Table rightSide = new Table();
        rightSide.add(menuSelector).padBottom(10).row();
        rightSide.add(logoutButton).row();

        mainTable.add(rightSide).top().left().padTop(10).row();

        stage.addActor(mainTable);

        menuSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (menuSelector.getSelected()) {
                    case "Profile": {
                        Main.getMain().setScreen(new ProfileMenu(new ProfileMenuController(),
                            GameAssetManager.getGameAssetManager().getSkin()));
                        break;
                    }
                    case "Settings": {
                        Main.getMain().setScreen(new SettingsMenu(new SettingsMenuController(),
                            GameAssetManager.getGameAssetManager().getSkin()));
                        break;
                    }
                    case "Pre-Game": {
                        Main.getMain().setScreen(new PreGameMenu(new PreGameMenuController(),
                            GameAssetManager.getGameAssetManager().getSkin()));
                        break;
                    }
                    case "ScoreBoard": {
                        try {
                            Main.getMain().setScreen(new ScoreBoardMenu(new ScoreBoardController(),
                                GameAssetManager.getGameAssetManager().getSkin()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case "Hint": {
                        Main.getMain().setScreen(new HintMenu(new HintMenuController(),
                            GameAssetManager.getGameAssetManager().getSkin()));
                        break;
                    }
                }

                menuSelector.setSelected("Select Menu");
            }
        });
        logoutButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.setCurrentPlayer(null);
                Main.getMain().setScreen(new LoginMenu(new LoginMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0.125f, 0.102f, 0.141f, 1f);
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
