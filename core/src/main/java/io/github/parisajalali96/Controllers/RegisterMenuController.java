package controllers;

import models.*;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class RegisterMenuController {

    //register user
    public Result registerUser(String username, String password) throws IOException {
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

    //get the user to answer a random security question
    public Result securityQuestion(User user){
        String[] questions = {"Who's your favourite 20 Minuets Till Dawn hero?",
                "What's your favourite color?",
                "What was the name of your first imaginary friend?",
                "What's a nickname you're usually given?"};

        Random random = new Random();
        int randomIndex = random.nextInt(questions.length);
        String randomQuestion = questions[randomIndex];

        user.setSQ(randomQuestion);
        //TODO get user answer
        return new Result(true, "");
    }

    
    //set security question answer
    public Result securityAnswer(User user, String answer){
        user.setSA(answer);
        return new Result(true, "Security question answered successfully!");
    }
}
