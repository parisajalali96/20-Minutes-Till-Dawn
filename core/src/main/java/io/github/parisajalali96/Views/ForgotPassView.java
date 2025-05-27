package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.ForgotPassController;
import io.github.parisajalali96.Main;


public class ForgotPassView extends Menu implements Screen {
    public Stage stage;
    private final Skin skin;
    public final Label gameTitle;
    public final TextButton getQuestion;
    public final TextField securityQuestionAnswer;
    public final TextField usernameField;
    public final TextButton confirmAnswer;
    public final TextButton backButton;
    public Table table;
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
        backButton = new TextButton("Back", skin);
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
        table2.add(confirmAnswer).padBottom(20).row();
        table2.add(backButton).padBottom(10).colspan(2).row();
        table.add(table1).padRight(50);
        table.add(table2).top();
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
