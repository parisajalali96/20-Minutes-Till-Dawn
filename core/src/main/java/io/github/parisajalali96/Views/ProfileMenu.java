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
import io.github.parisajalali96.Controllers.LoginMenuController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.ProfileMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Player;
import io.github.parisajalali96.Models.Result;

import java.io.IOException;

public class ProfileMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private final Player player = Game.getCurrentPlayer();
    private final TextButton exitButton;
    private final TextButton changeUsernameButton;
    private final TextButton changePasswordButton;
    private final TextButton deleteAccountButton;
    private final ProfileMenuController controller;
    private Table table;
    //private final TextButton changeAvatarButton;

    public ProfileMenu(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        exitButton = new TextButton("Exit", skin);
        changeUsernameButton = new TextButton("Change Username", skin);
        changePasswordButton = new TextButton("Change Password", skin);
        deleteAccountButton = new TextButton("Delete Account", skin);
        controller.setView(this);
    }


    public void changeUsernameButtonListener(){
        changeUsernameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Window window = new Window("Change Username", skin);
                Label usernameLabel = new Label("Enter new username:", skin);
                TextField usernameTextField = new TextField("", skin);
                TextButton changeUsername = new TextButton("Apply Changes", skin);
                TextButton backButton = new TextButton("Exit", skin);
                window.setMovable(true);
                window.setResizable(false);

                //change user button listener
                changeUsername.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        String newUsername = usernameTextField.getText();
                        try {
                            Result result = controller.changeUsername(newUsername);
                            showResult(result);
                            window.remove();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                backButton.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        window.remove();
                    }
                });
                window.add(usernameLabel).pad(20).row();
                window.add(usernameTextField).width(300).pad(10).row();
                window.add(changeUsername).pad(20).row();
                window.add(backButton).pad(20).row();
                window.pad(20);
                window.pack();
                window.setSize(700, 500);
                window.setPosition(
                    (Gdx.graphics.getWidth() - window.getWidth()) / 2,
                    (Gdx.graphics.getHeight() - window.getHeight()) / 2
                );

                stage.addActor(window);

            }
        });
    }

    public void changePasswordButtonListener(){
        changePasswordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Window window = new Window("Change Password", skin);
                Label passwordLabel = new Label("Enter new password:", skin);
                TextField newPasswordTextField = new TextField("", skin);
                TextButton changePassword = new TextButton("Apply Changes", skin);
                TextButton backButton = new TextButton("Exit", skin);

                window.setMovable(true);
                window.setResizable(false);

                changePassword.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        String newPassword = newPasswordTextField.getText();
                        try {
                            showResult(controller.changePassword(newPassword));
                            window.remove();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                backButton.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        window.remove();
                    }
                });

                window.add(passwordLabel).pad(20).row();
                window.add(newPasswordTextField).width(300).pad(10).row();
                window.add(changePassword).pad(20).row();
                window.add(backButton).pad(20).row();
                window.pad(20);
                window.pack();
                window.setSize(700, 500);
                window.setPosition(
                    (Gdx.graphics.getWidth() - window.getWidth()) / 2,
                    (Gdx.graphics.getHeight() - window.getHeight()) / 2
                );

                stage.addActor(window);

            }

        });
    }

    public void deleteAccountButtonListener(){
        deleteAccountButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Window window = new Window("Delete Account", skin);
                Label deleteAccountLabel = new Label("Are you sure you want to delete your account?", skin);
                TextButton deleteAccountButton = new TextButton("Yes, delete", skin);
                TextButton backButton = new TextButton("Back", skin);
                window.setMovable(true);
                window.setResizable(false);
                deleteAccountButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        try {
                            showResult(controller.deleteUser());
                            window.remove();
                            Main.getMain().setScreen(new LoginMenu(new LoginMenuController(),
                                GameAssetManager.getGameAssetManager().getSkin()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                backButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        window.remove();
                    }
                });

                window.add(deleteAccountLabel).pad(20).row();
                window.add(deleteAccountButton).pad(20).row();
                window.add(backButton).pad(20).row();
                window.pad(20);
                window.pack();
                window.setSize(700, 500);
                window.setPosition(
                    (Gdx.graphics.getWidth() - window.getWidth()) / 2,
                    (Gdx.graphics.getHeight() - window.getHeight()) / 2
                );

                stage.addActor(window);
            }
        });
    }

    public void exitButtonListener(){
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        changeUsernameButtonListener();
        changePasswordButtonListener();
        deleteAccountButtonListener();
        exitButtonListener();

        table = new Table();
        table.setFillParent(true);
        table.add(changeUsernameButton).pad(10).row();
        table.add(changePasswordButton).pad(10).row();
        table.add(deleteAccountButton).pad(10).row();
        table.add(exitButton).pad(10).row();
        stage.addActor(table);

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
