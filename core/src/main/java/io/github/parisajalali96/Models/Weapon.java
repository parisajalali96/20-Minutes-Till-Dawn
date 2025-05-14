package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.WeaponType;

public class Weapon {
    private WeaponType type;
    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private Texture texture;
    private boolean active;

    public Weapon(WeaponType type) {
        this.type = type;
        this.texture = type.getTexture();
    }

    public void setProjectile(Vector2 startPosition, Vector2 direction){
        this.position = new Vector2(400, 240);
        this.speed = 500f;
        this.velocity = direction.nor().scl(speed);
        this.active = true;
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

    public void dispose() {
        texture.dispose();
    }
}
