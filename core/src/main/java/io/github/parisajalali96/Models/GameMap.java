package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.parisajalali96.Models.Enums.EnemyType;
import io.github.parisajalali96.Models.Enums.TileTexture;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameMap {
    private static final int MAP_WIDTH = 100;
    private static final int MAP_HEIGHT = 100;
    private static final int TILE_SIZE = 32;

    private Tile[][] tiles = new Tile[MAP_WIDTH][MAP_HEIGHT];
    private List<Enemy> enemies = new ArrayList<>();
    private List<XpDrop> drops = new ArrayList<>();
    private List<Projectile> enemyProjectiles = new ArrayList<>();

    private float spawnTimer = 0f;
    private boolean treesSpawned = false;

    // fog of war effect
    float visibilityRadius = 200f;

    public GameMap() {
        initTiles();
    }

    private void initTiles() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                boolean walkable = true;
                TextureRegion texture = TileTexture.NORMAL.getTexture();
                tiles[x][y] = new Tile(texture, walkable, x, y);
            }
        }
    }

    public void spawnEnemy(EnemyType type, Vector2 position) {
        Animation<TextureRegion> idleAnimation = EnemySpawner.loadIdleAnimation(type);
        Animation<TextureRegion> spawnAnimation = EnemySpawner.loadSpawnAnimation(type);
        Animation<TextureRegion> attackAnimation = EnemySpawner.loadAttackAnimation(type);
        Animation<TextureRegion> deathAnimation = EnemySpawner.loadDeathAnimation();

        enemies.add(new Enemy(type, position, idleAnimation, spawnAnimation, attackAnimation, deathAnimation));
    }

    public void update(float delta) {
        Game.addSecondsPassed(delta);

        if (!treesSpawned) {
            for (int i = 0; i < 50; i++) {
                int tileX = (int) (Math.random() * tiles.length);
                int tileY = (int) (Math.random() * tiles[0].length);
                if (!tiles[tileX][tileY].isOccupied())
                    spawnEnemyOnTile(EnemyType.Tree, tileX, tileY);
            }
            treesSpawned = true;
        }

        spawnTimer += delta;
        if (spawnTimer >= 1f) {
            spawnTimer = 0;
            for (EnemyType type : EnemyType.values()) {
                int countToSpawn = type.getSpawnCount((int) Game.getSecondsPassed());
                for (int i = 0; i < countToSpawn; i++) {
                    if (type == EnemyType.TentacleMonster) {
                        int tileX = (int) (Math.random() * tiles.length);
                        int tileY = (int) (Math.random() * tiles[0].length);
                        if (!tiles[tileX][tileY].isOccupied())
                            spawnEnemyOnTile(EnemyType.TentacleMonster, tileX, tileY);
                    } else {
                        Vector2 spawnPos = getRandomSpawnPosition();
                        spawnEnemy(type, spawnPos);
                    }
                }
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }

        Player player = Game.getCurrentPlayer();
        Weapon weapon = player.getWeapon();
        weapon.update(delta);


        // killing enemies
        Iterator<Projectile> bulletIter = weapon.getProjectiles().iterator();
        while (bulletIter.hasNext()) {
            Projectile bullet = bulletIter.next();
            Rectangle bulletBounds = bullet.getBounds();

            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                if (bulletBounds.overlaps(enemy.getBounds())) {
                    enemy.addHealth(-bullet.getDamage());
                    bulletIter.remove();

                    if (!enemy.isAlive()) {
                        Game.getCurrentPlayer().addKill();
                        drops.add(new XpDrop(new Vector2(enemy.getPosition().x, enemy.getPosition().y)));
                        enemyIter.remove();
                    }
                    break;
                }
            }
        }

        Iterator<XpDrop> xpIter = drops.iterator();
        while (xpIter.hasNext()) {
            XpDrop dot = xpIter.next();
            if (dot.isPickedUpBy(player)) {
                dot.addXp();
                xpIter.remove();
            }
        }

        Iterator<Projectile> iter = enemyProjectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);

            if (p.getBounds().overlaps(Game.getCurrentPlayer().getBounds())) {
                Game.getCurrentPlayer().addHealth(-p.getDamage());
                iter.remove();
            }
        }

        //adding damage for when enemies hit player
        Iterator<Enemy> enemyIterator  = enemies.iterator();
        while(enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if(enemy.getBounds().overlaps(Game.getCurrentPlayer().getBounds()) && enemy.canAttack()) {
                Game.getCurrentPlayer().addHealth(-1);
                enemy.resetCanAttack();
            }
        }

    }

    public void draw(SpriteBatch batch) {
        Vector2 playerPos = Game.getCurrentPlayer().getPosition();
        // Draw tiles
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile = tiles[x][y];
                if (tile != null) {
                    tile.render(batch);
//                    Vector2 tilePos = tile.getPosition(); // tile's pixel position
//                    if (tilePos.dst(playerPos) <= visibilityRadius) {
//                        tile.render(batch);
//                    }
                }
            }
        }

        // Draw enemies on top
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }

        //draw dropped xps when enemy dies
        for(XpDrop xpDrop : drops) {
            xpDrop.draw(batch);
        }

        //draw enemy projectiles
        for(Projectile p : enemyProjectiles) {
            p.draw(batch);
        }


    }

    public Vector2 getRandomSpawnPosition() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float margin = 10f;

        int edge = (int) (Math.random() * 4);
        float x, y;

        switch (edge) {
            case 0:
                x = (float) Math.random() * screenWidth;
                y = screenHeight - margin;
                break;
            case 1:
                x = (float) Math.random() * screenWidth;
                y = margin;
                break;
            case 2:
                x = margin;
                y = (float) Math.random() * screenHeight;
                break;
            case 3:
                x = screenWidth - margin;
                y = (float) Math.random() * screenHeight;
                break;
            default:
                x = screenWidth / 2;
                y = screenHeight / 2;
                break;
        }

        return new Vector2(x, y);
    }

    public void dispose() {
    }

    public void spawnEnemyOnTile(EnemyType type, int tileX, int tileY) {
        float x = tileX * TILE_SIZE;
        float y = tileY * TILE_SIZE;
        spawnEnemy(type, new Vector2(x, y));
    }

    public void addEnemyProjectile(Projectile p) {
        enemyProjectiles.add(p);
    }

}

