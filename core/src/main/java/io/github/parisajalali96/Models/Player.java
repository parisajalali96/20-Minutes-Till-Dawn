package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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

    //animation stuff
    private float shootCooldown = 0.25f;
    private float shootTimer = 0f;
    private float speed = 200f;
    private boolean facingRight = true;

    //weapon stuff
    private boolean isShooting = false;
    private float shootingDisplayTime = 0.1f;
    private float shootingTimer = 0f;


    // Animation fields
    private Animation<TextureRegion> walkAnimation;
    private float stateTime = 0f;
    private boolean isMoving = false;
    private TextureRegion idleFrame;

    public Player(User user) {
        this.user = user;
        setRandomHero();

        // Build walk animation from hero
        TextureRegion[] walkFrames = hero.getWalkingFrames();
        walkAnimation = new Animation<>(0.1f, walkFrames);
        idleFrame = walkFrames[0]; // Default idle frame
    }

    public void setRandomHero() {
        Random rand = new Random();
        hero = Hero.values()[rand.nextInt(Hero.values().length)];
    }

    public int getHealth() {
        return health;
    }

    public void increaseHealth(int amount) {
        health += amount;
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

    public String getUsername(){
        return user.getUsername();
    }

    public void setHero(Hero hero) {
        this.hero = hero;
        TextureRegion[] walkFrames = hero.getWalkingFrames();
        walkAnimation = new Animation<>(0.1f, walkFrames);
        idleFrame = walkFrames[0];
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void update(float delta, OrthographicCamera camera) {
        float moveX = 0f;
        float moveY = 0f;

        if (Gdx.input.isKeyPressed(KeyControl.up)) {
            moveY += speed * delta;
        }
        if (Gdx.input.isKeyPressed(KeyControl.down)) {
            moveY -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(KeyControl.left)) {
            moveX -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(KeyControl.right)) {
            moveX += speed * delta;
        }

        isMoving = moveX != 0 || moveY != 0;

        position.x += moveX;
        position.y += moveY;

        if (moveX > 0) {
            facingRight = true;
        } else if (moveX < 0) {
            facingRight = false;
        }


        if (isMoving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }

        shootTimer -= delta;

        if (Gdx.input.isButtonPressed(KeyControl.shootWeapon) && shootTimer <= 0f) {
            shootTimer = shootCooldown;
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);
            Vector2 playerCenter = new Vector2(position.x + 32, position.y);
            Vector2 direction = new Vector2(mousePos.x, mousePos.y).sub(playerCenter).nor();
            weapon.shoot(playerCenter, direction);
            isShooting = true;
            shootingTimer = shootingDisplayTime;
        }
        weapon.update(delta);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = isMoving
            ? walkAnimation.getKeyFrame(stateTime, true)
            : idleFrame;

        if ((facingRight && currentFrame.isFlipX()) || (!facingRight && !currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, position.x, position.y);

        weapon.draw(batch, position, facingRight);
    }


    public void dispose() {
        hero.getMovementTexture().dispose();
        weapon.dispose();
    }
    public void setUsername(String username) {
        this.user.setUsername(username);
    }
    public String getPassword(){
        return user.getPassword();
    }
    public void setPassword(String password) {
        this.user.setPassword(password);
    }
    public Vector2 getPosition() {
        return position;
    }

}

