package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public enum Hero {
    SHANA("Images/Texture2D/T_Shana_Portrait.png", "Images/Texture2D/T_Shana.png"),
    DIAMOND("Images/Texture2D/T_Diamond_Portrait.png", "Images/Texture2D/T_Diamond #7829.png"),
    SCARLET("Images/Texture2D/T_Scarlett_Portrait.png", "Images/Texture2D/T_Scarlett.png"),
    LILITH("Images/Texture2D/T_Lilith_Portrait.png", "Images/Texture2D/T_Lilith.png"),
    DASHER("Images/Texture2D/T_Dasher_Portrait.png", "Images/Texture2D/T_Dasher.png");

    private final String portraitTexturePath;
    private final String movementTexturePath;

    Hero(String portraitTexturePath, String movementTexturePath) {
        this.portraitTexturePath = portraitTexturePath;
        this.movementTexturePath = movementTexturePath;
    }
    public Texture getTexture() {
        return new Texture(Gdx.files.internal(portraitTexturePath));
    }
    public Texture getMovementTexture() {
        return new Texture(Gdx.files.internal(movementTexturePath));
    }


}
