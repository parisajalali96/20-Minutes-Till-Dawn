package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private boolean active = true;
    private float speed = 500f;

    public Projectile(Vector2 start, Vector2 direction, Texture texture) {
        this.position = new Vector2(start);
        this.velocity = direction.nor().scl(speed);
        this.texture = texture;
    }

    public void update(float delta) {
        if (!active) return;
        position.mulAdd(velocity, delta);

        if (position.x < 0 || position.x > 800 || position.y < 0 || position.y > 600) {
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, position.x, position.y);
        }
    }

    public boolean isActive() {
        return active;
    }
}

