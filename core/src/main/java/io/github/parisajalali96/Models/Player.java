package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.WeaponType;

import java.util.Random;

public class Player {
    private final User user;
    private Hero hero;
    private Weapon weapon = new Weapon(WeaponType.REVOLVER);
    private int score;
    private int health;
    private int level;
    private int xp;
    private Vector2 position = new Vector2(400, 240);
    private TextureRegion sprite;
    private float shootCooldown = 0.25f;
    private float shootTimer = 0f;

    private float speed = 200f;

    public Player(User user) {
        this.user = user;
        setRandomHero();
        TextureRegion[][] regions = TextureRegion.split(hero.getMovementTexture(), 32, 32);
        sprite = regions[4][2];

    }

    public void setRandomHero(){
        Random rand = new Random();
        hero = Hero.values()[rand.nextInt(Hero.values().length)];
    }

    public int getHealth() {
        return health;
    }
    public void increaseHealth(int health) {
        health += health;
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

    public void update(float delta) {
        float moveX = 0f;
        float moveY = 0f;

        if (Gdx.input.isKeyPressed(KeyControl.up)) {
            moveY += speed * delta;  // Move up
        }
        if (Gdx.input.isKeyPressed(KeyControl.down)) {
            moveY -= speed * delta;  // Move down
        }

        if (Gdx.input.isKeyPressed(KeyControl.left)) {
            moveX -= speed * delta;  // Move left
        }
        if (Gdx.input.isKeyPressed(KeyControl.right)) {
            moveX += speed * delta;  // Move right
        }

        position.x += moveX;
        position.y += moveY;
        shootTimer -= delta;

        if (Gdx.input.isButtonPressed(KeyControl.shootWeapon) && shootTimer <= 0f) {
            shootTimer = shootCooldown;

            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            Vector2 direction = new Vector2(mousePos.x - position.x, mousePos.y - position.y);
            weapon.setProjectile(position.cpy(), direction);
        }
    }


    public void draw(SpriteBatch batch) {
        batch.draw(hero.getMovementTexture(), position.x, position.y);
        weapon.draw(batch);

    }

    public void dispose() {
        hero.getMovementTexture().dispose();
        weapon.dispose();
    }


}
