package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.*;
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
        return new Result(true, "User registered successfully!");
    }

    //skip and play as a guest
    public Result playAsAGuest(){
        User guestUser = new User("Guest Player", "96909690");
        Player guestPlayer = new Player(guestUser);
        Game.addPlayer(guestPlayer);
        return new Result(true, "You're now playing as a guest!");
    }

}
