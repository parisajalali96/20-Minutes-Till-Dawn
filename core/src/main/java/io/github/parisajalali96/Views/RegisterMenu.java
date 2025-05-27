package io.github.parisajalali96.Views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.LoginMenuController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.RegisterMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Result;

import java.io.IOException;

public class RegisterMenu extends Menu implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final RegisterMenuController controller;

    public final TextField usernameField;
    public final TextField passwordField;
    public final SelectBox<String> questionBox;
    public final TextField answerField;

    public final TextButton registerButton;
    public final TextButton guestButton;
    public final TextButton continueToLoginButton;

    public final Label gameTitle;

    public RegisterMenu(RegisterMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());

        this.usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        this.passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        this.questionBox = new SelectBox<>(skin);
        questionBox.setItems(
            "Who's your favourite 20 Minutes Till Dawn hero?",
            "What's your favourite color?",
            "What was the name of your first imaginary friend?",
            "What's a nickname you're usually given?"
        );

        this.answerField = new TextField("", skin);
        answerField.setMessageText("Your answer");

        this.registerButton = new TextButton("Register", skin);
        this.guestButton = new TextButton("Play as Guest", skin);
        this.continueToLoginButton = new TextButton("Go to Login", skin);

        this.gameTitle = new Label("Register", skin);

        controller.setView(this);
    }

    public void showResult(Result result) {
        Dialog dialog = new Dialog(result.isSuccess() ? "Success" : "Error", skin);
        dialog.text(result.getMessage()).button("OK", true).show(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(gameTitle).colspan(2).center().padBottom(20).row();

        Table formTable = new Table();
        formTable.add(usernameField).width(300).padBottom(10).row();
        formTable.add(passwordField).width(300).padBottom(20).row();
        formTable.add(new Label("Security Question:", skin)).left().padBottom(5).row();
        formTable.add(questionBox).width(300).padBottom(10).row();
        formTable.add(new Label("Answer:", skin)).left().padBottom(5).row();
        formTable.add(answerField).width(300).padBottom(20).row();

        Table buttonTable = new Table();
        buttonTable.add(registerButton).padBottom(10).row();
        buttonTable.add(guestButton).padBottom(10).row();
        buttonTable.add(continueToLoginButton).padBottom(10).row();

        table.add(formTable).padRight(50);
        table.add(buttonTable).top();

        stage.addActor(table);
        controller.addListeners();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.125f, 0.102f, 0.141f, 1f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}

