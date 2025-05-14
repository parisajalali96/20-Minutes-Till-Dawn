package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.*;
import io.github.parisajalali96.Views.LoginMenu;
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


}
