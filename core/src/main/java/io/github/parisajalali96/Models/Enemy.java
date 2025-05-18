package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.EnemyType;

public class Enemy {
    private final EnemyType type;
    private Vector2 position;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> spawnAnimation;
    private Animation<TextureRegion> attackAnimation;
    private float stateTime = 0f;
    private boolean isMoving = false;

    public Enemy(EnemyType type, Vector2 position, Animation<TextureRegion> idleAnimation,
                 Animation<TextureRegion> spawnAnimation, Animation<TextureRegion> attackAnimation) {
        this.type = type;
        this.position = position;
        this.idleAnimation = idleAnimation;
        this.spawnAnimation = spawnAnimation;
        this.attackAnimation = attackAnimation;
    }

    public void update(float delta) {
        stateTime += delta;
        // TODO: Add movement or behavior logic here
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public EnemyType getType() {
        return type;
    }
}
