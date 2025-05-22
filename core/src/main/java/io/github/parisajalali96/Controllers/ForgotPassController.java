package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.Result;
import io.github.parisajalali96.Models.User;
import io.github.parisajalali96.Models.UserStorage;
import io.github.parisajalali96.Views.ForgotPassView;

import java.io.IOException;

public class ForgotPassController {
    private ForgotPassView view;

    public void setView(ForgotPassView view) {
        this.view = view;
    }

    //forgot password
    public Result forgotPassword(String username, String SA) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        if(user == null) return new Result(false, "User not found!");
        else if(!user.getSA().equals(SA)) return new Result(false, "Wrong answer!");
        else return new Result(true, "You can change your password!");
    }

    //get security question
    public String getQuestion(String username) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        return user.getSQ();
    }

    //change pass controller
    public Result changePassword(String newPass, String username) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*)(_]).{8,}$";
        assert user != null;
        if(user.getPassword().equals(newPass))
            return new Result(true, "New password must be different!");
        else if(!newPass.matches(passwordRegex))
            return new Result(false, "New password is too weak!");
        user.setPassword(newPass);
        return new Result(true, "Password changed successfully!");
    }
}
