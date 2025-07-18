package io.github.parisajalali96.Models;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class Projectile implements Serializable {
    private int damage;
    private Vector2 position;
    private Vector2 velocity;
    private transient Texture texture;
    private boolean active = true;
    private float speed = 500f;

    public Projectile(int damage, Vector2 start, Vector2 direction, Texture texture) {
        this.damage = damage;
        this.position = new Vector2(start);
        this.velocity = direction.nor().scl(speed);
        this.texture = texture;
    }

    public void update(float delta) {
        if (!active) return;
        position.mulAdd(velocity, delta);

//        if (position.x < 0 || position.x > Gdx.graphics.getWidth() || position.y < 0 || position.y > Gdx.graphics.getHeight()) {
//            active = false;
//        }
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, position.x, position.y);
        }
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public int getDamage() {
        return damage;
    }

}

