package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.EnemyType;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private List<Enemy> enemies = new ArrayList<>();

    public void spawnEnemy(EnemyType type, Vector2 position) {
        Animation<TextureRegion> idleAnimation = EnemySpawner.loadIdleAnimation(type);
        Animation<TextureRegion> spawnAnimation = EnemySpawner.loadSpawnAnimation(type);
        Animation<TextureRegion> attackAnimation = EnemySpawner.loadAttackAnimation(type);

        enemies.add(new Enemy(type, position, idleAnimation, spawnAnimation, attackAnimation));
    }

    public void update(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
    }

    public void draw(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }
    }
}
