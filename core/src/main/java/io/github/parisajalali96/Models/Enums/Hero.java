package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public enum Hero {
    SHANA("Images/Texture2D/T_Shana_Portrait.png"),
    DIAMOND("Images/Texture2D/T_Diamond_Portrait.png"),
    SCARLET("Images/Texture2D/T_Scarlett_Portrait.png"),
    LILITH("Images/Texture2D/T_Lilith_Portrait.png"),
    DASHER("Images/Texture2D/T_Dasher_Portrait.png");

    private final String texturePath;
    Hero(String texturePath) {
        this.texturePath = texturePath;
    }
    public Texture getTexture() {
        return new Texture(Gdx.files.internal(texturePath));
    }

}
