package io.github.parisajalali96.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.*;
import io.github.parisajalali96.Views.ForgotPassView;
import io.github.parisajalali96.Views.LoginMenu;
import io.github.parisajalali96.Views.MainMenu;
import io.github.parisajalali96.Views.RegisterMenu;

import java.io.IOException;

public class LoginMenuController {
    private LoginMenu view;


    public void setView(LoginMenu view) {
        this.view = view;
    }

    //login as user
    public Result loginUser(String username, String password) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        if(user == null) return new Result(false, "User not found!");
        else if(!user.getPassword().equals(password)) return new Result(false, "Wrong password!");
        Player currentPlayer = null;
        User currentUser = UserStorage.findUserByUsername(username);
        currentPlayer = new Player(currentUser);
        Game.addPlayer(currentPlayer);
        Game.setCurrentPlayer(currentPlayer);
        return new Result(true, "Successfully logged in!");
    }

    //add listeners
    public void addListeners() {
        view.loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = view.usernameField.getText();
                String password = view.passwordField.getText();
                Result result = null;
                try {
                    result = loginUser(username, password);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                view.showResult(result);
                if(result.isSuccess()) {
                    Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                        GameAssetManager.getGameAssetManager().getSkin()));
                }
            }
        });

        view.forgotPasswordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new ForgotPassView(new ForgotPassController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });

        view.backButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new RegisterMenu(new RegisterMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
    }


}
