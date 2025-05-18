package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileTexture {
    NORMAL("Images/Sprite/T_ForestTiles_0.png");

    private final String path;
    TileTexture(String path) {
        this.path = path;
    }

    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(path), 0, 0, 100, 100);
    }
}
