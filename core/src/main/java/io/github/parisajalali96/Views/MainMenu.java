package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private final Image avatar;
    private final Label username;
    private final Label score;
    private final TextButton logoutButton;
    public Table mainTable;
    private final MainMenuController controller;
    private final String[] menuNames = {"Settings", "Profile", "Pre-Game", "Scoreboard", "Talent", "Continue Saved Game"};

    public MainMenu(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        Player currentPlayer = Game.getCurrentPlayer();
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

        mainTable.clear();
        mainTable.setFillParent(true);

        Table infoTable = new Table(skin);
        infoTable.add(username).center().padBottom(5).row();
        infoTable.add(score).left().padBottom(5).row();
        mainTable.add(infoTable).right().pad(20).colspan(2).expandX().fillX().row();

        Table menuTable = new Table(skin);
        for (int i = 0; i < menuNames.length; i++) {
            Label menuLabel = new Label(menuNames[i], skin, "subtitle");

            int finalI = i;
            menuLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        handleMenuSelection(menuNames[finalI]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    menuLabel.setColor(1f, 0.2f, 0.6f, 1f);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    menuLabel.setColor(Color.WHITE);
                }
            });

            menuTable.add(menuLabel).left().padBottom(10).row();
        }

        Table centerRow = new Table(skin);
        centerRow.add(menuTable).top().left().padRight(20);
        centerRow.add(avatar).size(400, 400).padLeft(20);
        mainTable.add(centerRow).expand().padTop(10).row();

        logoutButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Game.setCurrentPlayer(null);
                Main.getMain().setScreen(new LoginMenu(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
        mainTable.add(logoutButton);
        logoutButton.setPosition((float) Gdx.graphics.getWidth() /2, (float) Gdx.graphics.getHeight() / 2);

        stage.addActor(mainTable);
    }




    private void handleMenuSelection(String menuName) throws IOException {
        switch (menuName) {
            case "Settings": {
                Main.getMain().setScreen(new SettingsMenu(new SettingsMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
                break;
            }
            case "Profile": {
                Main.getMain().setScreen(new ProfileMenu(new ProfileMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
                break;
            }
            case "Pre-Game": {
                Main.getMain().setScreen(new PreGameMenu(new PreGameMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
                break;
            }
            case "Scoreboard": {
                Main.getMain().setScreen(new ScoreBoardMenu(new ScoreBoardController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
                break;
            }
            case "Talent": {
                Main.getMain().setScreen(new HintMenu(new HintMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
                break;
            }
            case "Continue Saved Game": {
                break;
            }
        }
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
