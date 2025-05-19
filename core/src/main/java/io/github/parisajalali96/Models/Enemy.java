package io.github.parisajalali96.Models;

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
    private float stateTime = 0f;
    private boolean isMoving = false;
    private float speed = 60f;
    private boolean firstShoot = false;
    private boolean isSpawned = false;

    public Enemy(EnemyType type, Vector2 position, Animation<TextureRegion> idleAnimation,
                 Animation<TextureRegion> spawnAnimation, Animation<TextureRegion> attackAnimation) {
        this.type = type;
        health = type.getHP();
        this.position = position;
        this.idleAnimation = idleAnimation;
        this.spawnAnimation = spawnAnimation;
        this.attackAnimation = attackAnimation;
        isAlive = true;
    }

    public void update(float delta) {
        stateTime += delta;
        if(type == EnemyType.Eyebat || type == EnemyType.Elder || type == EnemyType.BrainMonster) followPlayer(Game.getCurrentPlayer().getPosition(), delta);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame;
        if(type != EnemyType.Eyebat && type != EnemyType.Elder && type != EnemyType.BrainMonster) {
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
        batch.draw(currentFrame, position.x, position.y);
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




}
