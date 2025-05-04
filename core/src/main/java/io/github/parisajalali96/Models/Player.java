package io.github.parisajalali96.Models;

import io.github.parisajalali96.Models.Enums.Hero;
import models.Enums.Weapon;

import java.util.Random;

public class Player {
    private User user;
    private Hero hero;
    private Weapon weapon;
    private int score;

    public Player(User user) {
        this.user = user;
        setRandomHero();
    }

    public void setRandomHero(){
        Random rand = new Random();
        hero = Hero.values()[rand.nextInt(Hero.values().length)];
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }
    public Hero getHero() {
        return hero;
    }
    public String getPassword() {
        return user.getPassword();
    }
    public void setPassword(String password) {
        user.setPassword(password);
    }
    public String getUsername() {
        return user.getUsername();
    }
    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Weapon getWeapon(){
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }


}
