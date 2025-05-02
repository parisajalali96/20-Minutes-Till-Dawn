package controllers;

import models.*;

import java.io.IOException;

public class LoginMenuController {

    //login as user
    public Result loginUser(String username, String password) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        if(user == null) return new Result(false, "User not found!");
        else if(!user.getPassword().equals(password)) return new Result(false, "Wrong password!");
        Player currentPlayer = null;
        for(Player p : Game.getPlayers()){
            if(p.getUser().equals(user)) currentPlayer = p;
        }
        Game.setCurrentPlayer(currentPlayer);
        return new Result(true, "Successfully logged in!");
    }

    //forgot password
    public Result forgotPassword(String username, String SA) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        if(user == null) return new Result(false, "User not found!");
        else if(!user.getSQ().equals(SA)) return new Result(false, "Wrong answer!");
        else return new Result(true, "Your password is : " + user.getPassword());
    }

}
