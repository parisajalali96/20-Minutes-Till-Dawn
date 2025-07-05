package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.parisajalali96.Controllers.GameController;
import io.github.parisajalali96.Models.Enums.AbilityType;
import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.WeaponType;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private final User user;
    private transient Texture avatarTexture;
    private Hero hero;
    private Weapon weapon = new Weapon(WeaponType.REVOLVER);
    private int score;
    private int health ;
    private int level = 1;
    private int xp;
    private Vector2 position = new Vector2(400, 240);
    private int kills;

    //animation stuff
    private float shootCooldown = 0.25f;
    private float shootTimer = 0f;
    private float speed;
    private boolean facingRight = true;

    //weapon stuff
    private boolean isShooting = false;
    private float shootingDisplayTime = 0.1f;
    private float shootingTimer = 0f;


    // Animation fields
    private transient Animation<TextureRegion> walkAnimation;
    private float stateTime = 0f;
    private boolean isMoving = false;
    private transient TextureRegion idleFrame;

    //abilities
    private List<AbilityType> collectedAbilities = new ArrayList<>();
    private AbilityType currentAbility;
    private float abilityTimer = 0f;
    private float abilityTimeLine = 10f;

    //level upgrade
    private transient Animation<TextureRegion> levelUpAnimation;
    private float levelUpTimer = 0f;
    private float levelUpTimeLine = 10f;
    private boolean levelUpAnimationActive = false;
    private Vector2 levelUpPosition = new Vector2();



    public Player(User user) {
        this.user = user;
        setRandomHero();
        avatarTexture = hero.getTexture();
        health =  hero.getHP();
        speed = hero.getSpeed();

        TextureRegion[] walkFrames = hero.getWalkingFrames();
        walkAnimation = new Animation<>(0.1f, walkFrames);
        idleFrame = walkFrames[0];
        List<String> levelUpPaths = Hero.getLevelUpgardeAnimation();
        TextureRegion[] levelUpRegions = new TextureRegion[levelUpPaths.size()];

        for (int i = 0; i < levelUpPaths.size(); i++) {
            Texture frameTexture = new Texture(levelUpPaths.get(i));
            levelUpRegions[i] = new TextureRegion(frameTexture);
        }
        levelUpAnimation = new Animation<>(0.25f, levelUpRegions);
    }

    public void initPLayer(){
        TextureRegion[] walkFrames = hero.getWalkingFrames();
        walkAnimation = new Animation<>(0.1f, walkFrames);
        idleFrame = walkFrames[0];
        List<String> levelUpPaths = Hero.getLevelUpgardeAnimation();
        TextureRegion[] levelUpRegions = new TextureRegion[levelUpPaths.size()];

        for (int i = 0; i < levelUpPaths.size(); i++) {
            Texture frameTexture = new Texture(levelUpPaths.get(i));
            levelUpRegions[i] = new TextureRegion(frameTexture);
        }
        levelUpAnimation = new Animation<>(0.25f, levelUpRegions);
        weapon.initWeapon();
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



    public void addAbility(AbilityType ability) {
        currentAbility = ability;
        if(!collectedAbilities.contains(ability)){
            collectedAbilities.add(ability);
        }
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

        if (levelUpAnimationActive) {
            levelUpTimer += delta;

            weapon.update(delta);
            if (levelUpAnimation.isAnimationFinished(levelUpTimer)) {
                levelUpAnimationActive = false;
                Game.getGameView().setPaused(false);
                levelUpTimer = 0f;

                List<AbilityType> randomAbilities = GameController.get3RandomAbilities();
                Game.getGameView().setAbilityOptions(randomAbilities);
            }

            return;
        }


        //timer on abilities
        if(currentAbility != null) {
            abilityTimer += delta;
            if(abilityTimer >= abilityTimeLine) {
                abilityTimer = 0f;
                if(currentAbility == AbilityType.SPEEDY) speed *= (float) 1 /2;
                else if(currentAbility == AbilityType.DAMAGER) weapon.addDamage((int) (-weapon.getType().getDamage()*0.25));
                currentAbility = null;
            }
        }
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
        if (levelUpAnimationActive) {
            TextureRegion levelUpFrame = levelUpAnimation.getKeyFrame(levelUpTimer, true);
            float offsetX = -30f;
            batch.draw(levelUpFrame, levelUpPosition.x + offsetX, levelUpPosition.y);
        }

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

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills ++;
    }

    public int getXp(){
        return xp;
    }
    public void addXp(int xp) {
        this.xp += xp;
        goToNextLevel();
    }
    public void addHealth(int health) throws IOException {
        this.health += health;
        if(this.health <= 0) {
            this.health = 0;
            Game.getGameView().getController().endGame(false);
        }
    }

    public Rectangle getBounds() {
        TextureRegion currentFrame;
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        return new Rectangle(position.x, position.y,
            currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight());
    }

    public boolean canGoToNextLevel(){
        return xp >= level*20;
    }

    public void goToNextLevel(){
        if(canGoToNextLevel()) {
            level++;
           // Game.getGameView().setPaused(true);
            GameAssetManager.playSfx("levelUpUpgrade");
            levelUpAnimationActive = true;
            levelUpPosition.set(position);

        }
    }

    public void addSpeed(float speed) {
        this.speed += speed;
    }

    public float getSpeed() {
        return speed;
    }
    public int getLevel() {
        return level;
    }

    public List<AbilityType> getAbilities() {
        return collectedAbilities;
    }
    public void addScore(int score) {
        this.user.addTotalScore(score);
        this.score += score;
    }

    public void reset(){
        score = 0;
        health = hero.getHP();
        level = 1;
        xp = 0;
        kills = 0;
        weapon.setAmmo(weapon.getType().getAmmoMax());
        weapon.setProjectile(weapon.getType().getProjectile());
        weapon.setCurrentNumOfProjectiles(weapon.getType().getAmmoMax());
        collectedAbilities = new ArrayList<>();
    }

    public void setAvatar(Texture avatar) {
        this.avatarTexture = avatar;
    }
    public Texture getAvatar() {
        return avatarTexture;
    }



}

