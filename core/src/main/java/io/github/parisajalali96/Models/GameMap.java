package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.parisajalali96.Models.Enums.EnemyState;
import io.github.parisajalali96.Models.Enums.EnemyType;
import io.github.parisajalali96.Models.Enums.TileTexture;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 1L;
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
    private Texture fogGradientTexture;


    public GameMap() {
        Game.setMap(this);
        initTiles();
        fogGradientTexture = createFogOfWarTexture();

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

    public void initMap(){
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                tiles[x][y].initTile();
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

    public void update(float delta) throws IOException {
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


        // removing dead enemies
        Iterator<Enemy> enemyCleanupIter = enemies.iterator();
        while (enemyCleanupIter.hasNext()) {
            Enemy e = enemyCleanupIter.next();
            if (e.isDead()) {
                Game.getCurrentPlayer().addKill();
                drops.add(new XpDrop(new Vector2(e.getPosition().x, e.getPosition().y)));
                enemyCleanupIter.remove();
            }
        }

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

                    if (enemy.getHealth() <= 0 && enemy.getState() == EnemyState.ALIVE) {
                        enemy.die();
                        if (enemy.getType() == EnemyType.Eyebat)
                            GameAssetManager.playSfx("batDeath");
                    }

                    break;
                }
            }

        }

        Iterator<XpDrop> xpIter = drops.iterator();
        while (xpIter.hasNext()) {
            XpDrop dot = xpIter.next();
            dot.update(delta);
            if (dot.isPickedUpBy(player)) {
                GameAssetManager.playSfx("xpDrop");
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
                GameAssetManager.playSfx("monsterAttack");
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

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //batch.begin();
        float fogWidth = fogGradientTexture.getWidth();
        float fogHeight = fogGradientTexture.getHeight();

        batch.setColor(1, 1, 1, 1);

        batch.draw(fogGradientTexture,
            playerPos.x - fogWidth / 2f,
            playerPos.y - fogHeight / 2f,
            fogWidth,
            fogHeight
        );




    }

    public Vector2 getRandomSpawnPosition() {
        Vector2 playerPos = Game.getCurrentPlayer().getPosition();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();


        float margin = 10f;

        float leftX = playerPos.x - screenWidth / 2f + margin;
        float rightX = playerPos.x + screenWidth / 2f - margin;
        float bottomY = playerPos.y - screenHeight / 2f + margin;
        float topY = playerPos.y + screenHeight / 2f - margin;

        int edge = (int) (Math.random() * 4);
        float x, y;

        switch (edge) {
            case 0: // top edge
                x = leftX + (float) Math.random() * (rightX - leftX);
                y = topY;
                break;
            case 1: // bottom edge
                x = leftX + (float) Math.random() * (rightX - leftX);
                y = bottomY;
                break;
            case 2: // left edge
                x = leftX;
                y = bottomY + (float) Math.random() * (topY - bottomY);
                break;
            case 3: // right edge
                x = rightX;
                y = bottomY + (float) Math.random() * (topY - bottomY);
                break;
            default:
                x = playerPos.x;
                y = playerPos.y;
                break;
        }

        x = MathUtils.clamp(x, 0, MAP_WIDTH * TILE_SIZE);
        y = MathUtils.clamp(y, 0, MAP_HEIGHT * TILE_SIZE);

        return new Vector2(x, y);
    }


    public List<Enemy> getEnemies() {
        return enemies;
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

    private Texture createFogOfWarTexture() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);

        float centerX = width / 2f;
        float centerY = height / 2f;

        float maxDist = (float) Math.sqrt(centerX * centerX + centerY * centerY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float dx = x - centerX;
                float dy = y - centerY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                float visibility = 0.5f;
                float alpha = MathUtils.clamp(dist / (maxDist * visibility), 0f, 1f);
                alpha = alpha * alpha;


                pixmap.setColor(0f, 0f, 0f, alpha);
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }





}

