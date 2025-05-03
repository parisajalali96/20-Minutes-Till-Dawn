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
import io.github.parisajalali96.Controllers.RegisterMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Result;

import java.io.IOException;

public class RegisterMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private final TextField usernameField;
    private final TextField passwordField;
    private final Label gameTitle;
    private final TextButton registerButton;
    private final TextButton guestButton;

    private Table table;
    private final RegisterMenuController controller;

    public RegisterMenu(RegisterMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.usernameField = new TextField("Username", skin);
        this.passwordField = new TextField("Password", skin);
        this.gameTitle = new Label("Register", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        registerButton = new TextButton("Register", skin);
        guestButton = new TextButton("Play as Guest", skin);
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
        table.add(registerButton).width(250).padBottom(10).row();
        table.add(guestButton).width(350).row();


        stage.addActor(table);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                try {
                    showResult(controller.registerUser(username, password));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        guestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle guest login
                showResult(controller.playAsAGuest());
            }
        });
    }

    public void showResult(Result result) {
        if (!result.isSuccess()) {
            Dialog errorDialog = new Dialog("Error", skin);
            errorDialog.text(result.getMessage());
            errorDialog.button("OK", true);
            errorDialog.show(stage);
        } else {
            Dialog successDialog = new Dialog("Success", skin);
            successDialog.text(result.getMessage());
            successDialog.button("OK", true);
            successDialog.show(stage);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
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
