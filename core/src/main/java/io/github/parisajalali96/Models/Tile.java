package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.TileTexture;

import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int TILE_SIZE = 32;
    private transient TextureRegion texture;
    private boolean walkable;
    private int x, y;
    private boolean isOccupied;

    public Tile(TextureRegion texture, boolean walkable, int x, int y) {
        this.texture = texture;
        this.walkable = walkable;
        this.x = x;
        this.y = y;
        isOccupied = false;
    }

    public void initTile(){
        this.texture = new TextureRegion(TileTexture.NORMAL.getTexture());
    }


    public void render(SpriteBatch batch) {

        batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE);
    }

    public boolean isWalkable() {
        return walkable;
    }
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    public boolean isOccupied() {
        return isOccupied;
    }
    public Vector2 getPosition() {
        return new Vector2(x * TILE_SIZE, y * TILE_SIZE);
    }
}
