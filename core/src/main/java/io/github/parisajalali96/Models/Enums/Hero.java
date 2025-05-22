package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Hero {
    // new format: rows {rowIndex, frameCount}
    SHANA("Images/Texture2D/T_Shana_Portrait.png", "Images/Texture2D/T_Shana.png", 32, 32, new int[][]{{0, 6},{1,4}, {2,8}}, 20, 230),
    DIAMOND("Images/Texture2D/T_Diamond_Portrait.png", "Images/Texture2D/T_Diamond #7829.png", 32, 32, new int[][]{{0, 6},{1,4}, {2,8}}, 35, 100),
    SCARLET("Images/Texture2D/T_Scarlett_Portrait.png", "Images/Texture2D/T_Scarlett.png", 32, 32, new int[][]{{0, 6}, {1,4}}, 15, 200),
    LILITH("Images/Texture2D/T_Lilith_Portrait.png", "Images/Texture2D/T_Lilith.png", 32, 32, new int[][]{{0, 6},{1,4}, {2,8}},25,150 ),
    DASHER("Images/Texture2D/T_Dasher_Portrait.png", "Images/Texture2D/T_Dasher.png", 32, 32, new int[][]{{0, 3}, {1, 4}},10,250);

    private final String portraitTexturePath;
    private final String movementTexturePath;
    private final int frameWidth;
    private final int frameHeight;
    private final int[][] animationRowsAndCounts;
    private Texture movementTexture;
    private final int HP;
    private final int speed;

    Hero(String portraitTexturePath, String movementTexturePath, int frameWidth, int frameHeight, int[][] animationRowsAndCounts, int HP, int speed) {
        this.portraitTexturePath = portraitTexturePath;
        this.movementTexturePath = movementTexturePath;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.animationRowsAndCounts = animationRowsAndCounts;
        this.HP = HP;
        this.speed = speed;
    }

    public Texture getTexture() {
        return new Texture(Gdx.files.internal(portraitTexturePath));
    }

    public Texture getMovementTexture() {
        if (movementTexture == null) {
            movementTexture = new Texture(Gdx.files.internal(movementTexturePath));
        }
        return movementTexture;
    }

    public TextureRegion[] getWalkingFrames() {
        TextureRegion[][] tmp = TextureRegion.split(getMovementTexture(), frameWidth, frameHeight);

        List<TextureRegion> frames = new ArrayList<>();
        for (int[] rowInfo : animationRowsAndCounts) {
            int row = rowInfo[0];
            int count = rowInfo[1];
            for (int i = 0; i < count; i++) {
                frames.add(tmp[row][i]);
            }
        }

        return frames.toArray(new TextureRegion[0]);
    }

    public static Hero getRandomHero() {
        Hero[] values = values();
        return values[new Random().nextInt(values.length)];
    }

    public int getHP() {
        return HP;
    }
    public int getSpeed() {
        return speed;
    }
    public String getDescription(){
        return name() + "\n" + "HP: " + getHP() + "\n" + "Speed: " + getSpeed();
    }
}
