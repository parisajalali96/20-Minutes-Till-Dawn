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
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Result;

import java.io.IOException;

public class ForgotPassView implements Screen {
    private Stage stage;
    private Skin skin;
    private final Label gameTitle;
    private final TextButton getQuestion;
    private final TextField securityQuestionAnswer;
    private final TextField usernameField;
    private final TextButton confirmAnswer;
    private final TextButton backButton;
    private Table table;
    private final ForgotPassController controller;

    public ForgotPassView(ForgotPassController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        gameTitle = new Label("Forgot Password", skin);
        getQuestion = new TextButton("Get Question", skin);
        securityQuestionAnswer = new TextField("", skin);
        securityQuestionAnswer.setMessageText("Your answer");
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        confirmAnswer = new TextButton("Confirm", skin);
        backButton = new TextButton("Back to Login", skin);
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

        Table table1 = new Table();
        table1.add(usernameField).width(300).padBottom(20).row();
        table1.add(getQuestion).width(300).padBottom(10).colspan(2).row();
        table1.add(securityQuestionAnswer).width(300).padBottom(10).colspan(2).row();

        Table table2 = new Table();
        table2.add(confirmAnswer).width(300).padBottom(20).row();
        table2.add(backButton).width(300).padBottom(10).colspan(2).row();

        table.add(table1).padRight(50);
        table.add(table2).top();
        stage.addActor(table);

        getQuestion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog dialog = new Dialog("Security Question", skin);
                try {
                    String username = usernameField.getText();
                    if(username.isEmpty()) showResult(new Result(false, "Username field is empty!"));
                    else {
                        Label label = new Label(controller.getQuestion(username), skin);
                        label.setWrap(true);
                        label.setAlignment(Align.center);
                        dialog.getContentTable().add(label).width(300).pad(20);
                        dialog.button("OK");
                        dialog.show(stage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        confirmAnswer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String answer = securityQuestionAnswer.getText();
                String username = usernameField.getText();
                if(username.isEmpty()) showResult(new Result(false, "Username field is empty!"));
                else {
                    try {
                        Result result = controller.forgotPassword(username, answer);
                        showResult(result);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new LoginMenu(
                    new LoginMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()
                ));
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
