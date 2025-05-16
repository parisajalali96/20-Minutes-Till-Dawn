package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.WeaponType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Weapon {
    private WeaponType type;
    private Texture texture;
    private final List<Projectile> projectiles = new ArrayList<>();

    public Weapon(WeaponType type) {
        this.type = type;
        this.texture = type.getTexture();
    }

    public void shoot(Vector2 startPosition, Vector2 direction) {
        projectiles.add(new Projectile(startPosition, direction, texture));
    }

    public void update(float delta) {
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);
            if (!p.isActive()) {
                iter.remove();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Projectile p : projectiles) {
            p.draw(batch);
        }
    }

    public void dispose() {
        texture.dispose();
    }
}

