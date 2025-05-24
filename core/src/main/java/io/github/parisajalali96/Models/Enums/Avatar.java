package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.graphics.Texture;

public enum Avatar {
    SHANA("Images/Sprite/T_Shana_Portrait.png"),
    SCARLET("Images/Sprite/T_Scarlett_Portrait.png"),
    RAVEN("Images/Sprite/T_Raven_Portrait.png"),
    LUNA("Images/Sprite/T_Luna_Portrait.png"),
    LILITH("Images/Sprite/T_Lilith_Portrait.png"),
    HINA("Images/Texture2D/T_Hina_Portrait.png"),
    HASTUR("Images/Sprite/T_Hastur_Portrait.png"),
    DIAMOND("Images/Sprite/T_Diamond_Portrait.png"),
    DASHER("Images/Sprite/T_Dasher_Portrait.png"),
    ABBY("Images/Sprite/T_Abby_Portrait.png");

    private final String portraitPath;
    Avatar(String portraitPath) {
        this.portraitPath = portraitPath;
    }
    public Texture getTexture() {
        return new Texture(portraitPath);
    }
}
