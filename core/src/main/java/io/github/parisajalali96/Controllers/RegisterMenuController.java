package io.github.parisajalali96.Controllers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.*;
import io.github.parisajalali96.Views.LoginMenu;
import io.github.parisajalali96.Views.MainMenu;
import io.github.parisajalali96.Views.RegisterMenu;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class RegisterMenuController {
    private RegisterMenu view;

    public void setView(RegisterMenu view) {
        this.view = view;
    }

    //register user
    public Result registerUser(String username, String password, String SQ, String SA) throws IOException {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*)(_]).{8,}$";
        //errors
        for(User user : Game.getRegisteredUsers()) {
            if(user.getUsername().equals(username)) {
                return new Result(false, "Username is already taken!");
            }
        }
        if(!password.matches(passwordRegex))
            return new Result(false, "Password format is weak!");

        User newUser = new User(username, password);
        newUser.setSQ(SQ);
        newUser.setSA(SA);
        UserStorage.registerUser(newUser);
        Player newPlayer = new Player(newUser);
        Game.addPlayer(newPlayer);
        return new Result(true, "User registered successfully!");
    }

    //skip and play as a guest
    public Result playAsAGuest(){
        User guestUser = new User("Guest Player", "96909690");
        Player guestPlayer = new Player(guestUser);
        Game.addPlayer(guestPlayer);
        Game.setCurrentPlayer(guestPlayer);
        return new Result(true, "You're now playing as a guest!");
    }

    //add listeners
    public void addListeners() {
        view.registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = view.usernameField.getText();
                String password = view.passwordField.getText();
                String question = view.questionBox.getSelected();
                String answer = view.answerField.getText();

                try {
                    Result result = registerUser(username, password, question, answer);
                    view.showResult(result);
                    if (result.isSuccess()) {
                        Main.getMain().setScreen(new LoginMenu(
                            new LoginMenuController(),
                            GameAssetManager.getGameAssetManager().getSkin()
                        ));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        view.guestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = playAsAGuest();
                view.showResult(result);
                if (result.isSuccess()) {
                    Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                        GameAssetManager.getGameAssetManager().getSkin()));
                }
            }
        });

        view.continueToLoginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new LoginMenu(
                    new LoginMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()
                ));
            }
        });
    }

}
