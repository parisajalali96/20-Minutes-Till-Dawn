package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.ForgotPassController;
import io.github.parisajalali96.Controllers.LoginMenuController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.RegisterMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Player;
import io.github.parisajalali96.Models.Result;

import java.io.IOException;


public class LoginMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private final TextField usernameField;
    private final TextField passwordField;
    private final TextButton loginButton;
    private final TextButton forgotPasswordButton;
    private final Label gameTitle;
    private final TextButton backButton;
    private Table table;
    private final LoginMenuController controller;

    public LoginMenu(LoginMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        this.gameTitle = new Label("Login", skin);
        this.loginButton = new TextButton("Login", skin);
        this.backButton = new TextButton("Back", skin);
        forgotPasswordButton = new TextButton("Forgot Password?", skin);
        this.table = new Table();
        controller.setView(this);

    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        table.add(gameTitle).colspan(2).padBottom(20).row();
        table.add(usernameField).width(300).padBottom(10).colspan(2).row();
        table.add(passwordField).width(300).padBottom(20).colspan(2).row();

        Table buttonRow = new Table();
        buttonRow.add(loginButton).padRight(10);
        buttonRow.add(backButton);
        table.add(buttonRow).colspan(2).padBottom(15).row();

        table.add(forgotPasswordButton).colspan(2).center();

        stage.addActor(table);

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                Result result = null;
                try {
                    result = controller.loginUser(username, password);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                showResult(result);
                if(result.isSuccess()) {
                    Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                        GameAssetManager.getGameAssetManager().getSkin()));
                }
            }
        });

        forgotPasswordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new ForgotPassView(new ForgotPassController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });

        backButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new RegisterMenu(new RegisterMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });

    }

    private void showResult(Result result) {
        Dialog dialog = new Dialog(result.isSuccess() ? "Success" : "Error", skin);
        Label label = new Label(result.getMessage(), skin);
        label.setWrap(true);
        label.setAlignment(Align.center);
        dialog.getContentTable().add(label).width(300).pad(20);
        dialog.button("OK");
        dialog.show(stage);
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
