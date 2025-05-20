package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.EnemyType;

public class Enemy {
    private final EnemyType type;
    private int health;
    private boolean isAlive;
    private Vector2 position;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> spawnAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> deathAnimation;
    private float stateTime = 0f;
    private boolean isMoving = false;
    private final float NORMAL_SPEED = 60f;
    private final float SPEDUP_SPEED = 120f;
    private float speed = NORMAL_SPEED;
    private boolean firstShoot = false;
    private boolean isSpawned = false;
    private boolean spawnedFromRight = false;
    public boolean deathAnimationPlayed = false;
    private float elderSpeedupTimer = 0f;
    private boolean elderSpeedBoost = false;


    //for eye monster shooting
    private float shootingCoolDown = 3f;
    private float shootingTimer = 0f;

    //for attacking
    private float timeSinceLastAttack = 0f;

    public Enemy(EnemyType type, Vector2 position, Animation<TextureRegion> idleAnimation,
                 Animation<TextureRegion> spawnAnimation, Animation<TextureRegion> attackAnimation, Animation<TextureRegion> deathAnimation) {
        this.type = type;
        health = type.getHP();
        this.position = position;
        this.idleAnimation = idleAnimation;
        this.spawnAnimation = spawnAnimation;
        this.attackAnimation = attackAnimation;
        this.deathAnimation = idleAnimation;
        isAlive = true;
        spawnedFromRight = position.x > (float) Gdx.graphics.getWidth() / 2;
    }

    public void update(float delta) {
        stateTime += delta;
        timeSinceLastAttack += delta;
        if (!isAlive) {
            if (deathAnimation.isAnimationFinished(stateTime)) {
                deathAnimationPlayed = true;
            }
            return;
        }


        if(type == EnemyType.Elder) {
            if(!elderSpeedBoost) {
                elderSpeedupTimer += delta;
                if(elderSpeedupTimer >= 5f) {
                    elderSpeedBoost = true;
                    elderSpeedupTimer = 0f;
                    speed = SPEDUP_SPEED;
                }
            } else {
                elderSpeedupTimer += delta;
                if(elderSpeedupTimer >= 5f) {
                    elderSpeedBoost = false;
                    elderSpeedupTimer = 0f;
                    speed = NORMAL_SPEED;
                }
            }
            followPlayer(Game.getCurrentPlayer().getPosition(), delta);
        } else if(type == EnemyType.BrainMonster)
            followPlayer(Game.getCurrentPlayer().getPosition(), delta);
        else if(type == EnemyType.Eyebat) {
            shootingTimer += delta;
            if(shootingTimer >= shootingCoolDown) {
                shootingTimer = 0f;
                shootPlayer();
            }
            followPlayer(Game.getCurrentPlayer().getPosition(), delta);
        }
    }

    //for eyebat to shoot at player
    public void shootPlayer(){
        Vector2 target = Game.getCurrentPlayer().getPosition();
        Vector2 direction = new Vector2(target).sub(position).nor();
        Projectile enemyProjectile = new Projectile(1, this.position.cpy(), direction, EnemyType.getEyeBatTexture());
        Game.getMap().addEnemyProjectile(enemyProjectile);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame;
        if(!isAlive) {
            currentFrame = deathAnimation.getKeyFrame(stateTime, true);
        } else if(type != EnemyType.Eyebat && type != EnemyType.Elder && type != EnemyType.BrainMonster) {
            if(type == EnemyType.TentacleMonster ) {
                if (!isSpawned) {
                    currentFrame = spawnAnimation.getKeyFrame(stateTime);
                    if (spawnAnimation.isAnimationFinished(stateTime)) {
                        isSpawned = true;
                        stateTime = 0f;
                    }
                } else {
                    currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                }
            } else currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        }

        Vector2 playerPosition = Game.getCurrentPlayer().getPosition();
        float x = playerPosition.x - position.x;
        float y = playerPosition.y - position.y;
        float angle = (float) Math.toDegrees(Math.atan2(y, x));

        if (angle > 90) angle %= 90;
        if (angle < -90) angle %= -90;

        float originX = currentFrame.getRegionWidth() / 2f;
        float originY = currentFrame.getRegionHeight() / 2f;

        if(type == EnemyType.Tree || type == EnemyType.TentacleMonster) batch.draw(currentFrame, position.x, position.y);
        else {
            boolean flip = playerPosition.x < position.x;
            if (flip && !currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            } else if (!flip && currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
            batch.draw(
                currentFrame,
                position.x, position.y,
                originX, originY,
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                1f, 1f,
                angle
            );
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public EnemyType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }
    public void addHealth(int amount) {
        this.health += amount;
        if (this.health <= 0) {
            this.health = 0;
            isAlive = false;
        }
    }


    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void followPlayer(Vector2 playerPosition, float delta) {
        Vector2 direction = new Vector2(playerPosition).sub(position);

        if (direction.len() > 1f) {
            direction.nor();
            position.mulAdd(direction, speed * delta);
        }
    }

    public Rectangle getBounds() {
        TextureRegion currentFrame;

        if (type != EnemyType.Eyebat && type != EnemyType.Elder && type != EnemyType.BrainMonster) {
            if (type == EnemyType.TentacleMonster && !isSpawned) {
                currentFrame = spawnAnimation.getKeyFrame(stateTime);
            } else {
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
            }
        } else {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        }


        return new Rectangle(position.x, position.y,
            currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight());
    }

    public boolean canAttack(){
        return timeSinceLastAttack >= 1f;
    }

    public void resetCanAttack(){
        timeSinceLastAttack = 0f;
    }



}
