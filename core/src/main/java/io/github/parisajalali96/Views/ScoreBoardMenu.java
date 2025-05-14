package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.ScoreBoardController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.User;
import io.github.parisajalali96.Models.UserStorage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ScoreBoardMenu implements Screen {
    private Stage stage;
    private final Skin skin;
    private Table table;
    private final ScoreBoardController controller;
    private final TextButton exitButton;
    private List<User> users;

    public ScoreBoardMenu(ScoreBoardController controller, Skin skin) throws IOException {
        this.controller = controller;
        this.skin = skin;
        this.users = UserStorage.loadUsers();
        exitButton = new TextButton("Exit", skin);
        controller.setView(this);
    }

    private void refreshTable() {
        table.clear();

        Label usernameHeader = new Label("Username", skin, "header");
        Label scoreHeader = new Label("Total Score", skin, "header");
        Label killsHeader = new Label("Kills", skin, "header");
        Label survivalHeader = new Label("Survival", skin, "header");

        table.add(usernameHeader).pad(50);
        table.add(scoreHeader).pad(50);
        table.add(killsHeader).pad(50);
        table.add(survivalHeader).pad(50);
        table.row();

        usernameHeader.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                users.sort(Comparator.comparing(User::getUsername).reversed());
                refreshTable();
            }
        });
        scoreHeader.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                users.sort(Comparator.comparingInt(User::getTotalScore).reversed());
                refreshTable();
            }
        });
        killsHeader.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                users.sort(Comparator.comparingInt(User::getNumOfKills).reversed());
                refreshTable();
            }
        });
        survivalHeader.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                users.sort(Comparator.comparingInt(User::getLongestSurvivalScore).reversed());
                refreshTable();
            }
        });

        for ( int i = 0; i < users.size(); i++ ) {
            User user = users.get(i);
            Label usernameLabel = new Label( String.valueOf(i + 1) + ". " + user.getUsername(), skin);
            Label totalScoreLabel = new Label(String.valueOf(user.getTotalScore()), skin);
            Label killsLabel = new Label(String.valueOf(user.getNumOfKills()), skin);
            Label survivalLabel = new Label(String.valueOf(user.getLongestSurvivalScore()), skin);

            if (i < 3) {
                Color highlightColor = new Color(1f, 0.2f, 0.8f, 1f);
                if(user.getUsername().equals(Game.getCurrentPlayer().getUsername()))
                    highlightColor = new Color(1, 0.8f, 0, 1);
                usernameLabel.setColor(highlightColor);
                totalScoreLabel.setColor(highlightColor);
                killsLabel.setColor(highlightColor);
                survivalLabel.setColor(highlightColor);
            }

            table.add(usernameLabel).padRight(50);
            table.add(totalScoreLabel).padLeft(20).padRight(50);
            table.add(killsLabel).padLeft(20).padRight(50);
            table.add(survivalLabel).padLeft(20);
            table.row();

        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.top().padTop(40);
        refreshTable();
        table.row();
        table.add(exitButton).colspan(5).center().padTop(20);

        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        Main.getBatch().begin();
        Main.getBatch().end();

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
