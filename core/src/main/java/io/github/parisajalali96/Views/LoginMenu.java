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


public class LoginMenu extends Menu implements Screen {
    private Stage stage;
    private final Skin skin;
    public final TextField usernameField;
    public final TextField passwordField;
    public final TextButton loginButton;
    public final TextButton forgotPasswordButton;
    public final Label gameTitle;
    public final TextButton backButton;
    public final Table table;
    public final LoginMenuController controller;

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
        controller.addListeners();
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
