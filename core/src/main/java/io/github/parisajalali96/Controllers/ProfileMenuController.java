package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Player;
import io.github.parisajalali96.Models.Result;
import io.github.parisajalali96.Models.UserStorage;
import models.Enums.Hero;

import java.io.IOException;

public class ProfileMenuController {

    //change username
    public Result changeUsername(String username) throws IOException {
        Player currentPlayer = Game.getCurrentPlayer();
        if(UserStorage.findUserByUsername(username) != null)
            return new Result(false, "Username is already taken!");
        currentPlayer.setUsername(username);
        return new Result(true, "Username changed successfully!");
    }

    //change password
    public Result changePassword(String password) throws IOException {
        Player currentPlayer = Game.getCurrentPlayer();
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*)(_]).{8,}$";
        if(currentPlayer.getPassword().equals(password))
            return new Result(true, "New password must be different!");
        else if(!password.matches(passwordRegex))
            return new Result(false, "New password is too weak!");
        currentPlayer.setPassword(password);
        return new Result(true, "Password changed successfully!");
    }

    //delete account
    public Result deleteUser() throws IOException {
        Player currentPlayer = Game.getCurrentPlayer();
        UserStorage.deleteUserByUsername(currentPlayer.getUsername());
        Game.setCurrentPlayer(null);
        Game.getPlayers().remove(currentPlayer);
        Game.getRegisteredUsers().remove(currentPlayer);
        return new Result(true, "User deleted successfully! You are now logged out!");
    }


    //change avatar
    public Result changeAvatar(Hero hero) {
        Game.getCurrentPlayer().setHero(hero);
        return new Result(true, "Avatar changed successfully!");
    }


}
