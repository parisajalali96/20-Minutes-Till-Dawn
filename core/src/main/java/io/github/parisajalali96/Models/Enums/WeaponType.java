package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public enum WeaponType {
    REVOLVER("Revolver", "Images/Sprite/RevolverStill.png", List.of("Images/Sprite/RevolverReload_0.png", "Images/Sprite/RevolverReload_1.png", "Images/Sprite/RevolverReload_2.png"), 10 ),
    SHOTGUN("Shotgun", "Images/Sprite/T_Shotgun_SS_0.png", List.of("Images/Sprite/T_Shotgun_SS_1.png", "Images/Sprite/T_Shotgun_SS_2.png", "Images/Sprite/T_Shotgun_SS_3.png"), 10),
    SMGDUAL("SMGDual", "Images/Sprite/T_DualSMGs_Icon.png", List.of("Images/Sprite/SMGReload_0.png", "Images/Sprite/SMGReload_1.png", "Images/Sprite/SMGReload_2.png", "Images/Sprite/SMGReload_3.png"), 10);

    private final String name;
    private final String weaponImagePath;
    private final List<String> weaponReloadStagesPath;
    private final int damage;

    static String bulletImagePath = "Images/Texture2D/T_SmallCircle.png";
    WeaponType(String name, String weaponImagePath, List<String> weaponReloadStagesPath, int damage) {
        this.name = name;
        this.weaponImagePath = weaponImagePath;
        this.weaponReloadStagesPath = weaponReloadStagesPath;
        this.damage = damage;
    }
    public String getName() {
        return name;
    }
    public Texture getTexture() {
        return new Texture(Gdx.files.internal(weaponImagePath));
    }
    public List<Texture> getReloadStagesTextures() {
        List<Texture> textureList = new ArrayList<>();
        for(String s: weaponReloadStagesPath) {
            textureList.add(new Texture(Gdx.files.internal(s)));
        }
        return textureList;
    }
    public static Texture getBulletTexture() {
        return new Texture(Gdx.files.internal(bulletImagePath));
    }

    public int getDamage(){
        return damage;
    }

}
