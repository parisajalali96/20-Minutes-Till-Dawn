package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public enum WeaponType {
    REVOLVER("Revolver", "Images/Sprite/RevolverStill.png", List.of("Images/Sprite/RevolverReload_0.png", "Images/Sprite/RevolverReload_1.png", "Images/Sprite/RevolverReload_2.png"), 20 ,1,1,6),
    SHOTGUN("Shotgun", "Images/Sprite/T_Shotgun_SS_0.png", List.of("Images/Sprite/T_Shotgun_SS_1.png", "Images/Sprite/T_Shotgun_SS_2.png", "Images/Sprite/T_Shotgun_SS_3.png"), 10,4,1,2),
    SMGDUAL("SMGDual", "Images/Sprite/T_DualSMGs_Icon.png", List.of("Images/Sprite/SMGReload_0.png", "Images/Sprite/SMGReload_1.png", "Images/Sprite/SMGReload_2.png", "Images/Sprite/SMGReload_3.png"), 8,1,2,24);

    private final String name;
    private final String weaponImagePath;
    private final List<String> weaponReloadStagesPath;
    private final int damage;
    private final int projectile;
    private final int timeReload;
    private final int ammoMax;

    static String bulletImagePath = "Images/Texture2D/T_SmallCircle.png";
    WeaponType(String name, String weaponImagePath, List<String> weaponReloadStagesPath, int damage, int projectile, int timeReload, int ammoMax) {
        this.name = name;
        this.weaponImagePath = weaponImagePath;
        this.weaponReloadStagesPath = weaponReloadStagesPath;
        this.damage = damage;
        this.projectile = projectile;
        this.timeReload = timeReload;
        this.ammoMax = ammoMax;
    }
    public String getName() {
        return name;
    }
    public Texture getTexture() {
        return new Texture(Gdx.files.internal(weaponImagePath));
    }
    public List<String> getReloadStagesTextures() {
        return weaponReloadStagesPath;
    }
    public static Texture getBulletTexture() {
        return new Texture(Gdx.files.internal(bulletImagePath));
    }

    public int getDamage(){
        return damage;
    }

    public int getTimeReload() {
        return timeReload;
    }
    public int getProjectile() {
        return projectile;
    }
    public int getAmmoMax() {
        return ammoMax;
    }
    public String getDescription(){
        return name + "\n" + "Damage: " + damage + "\n" + "Projectile: "
            + projectile + "\n" + "Reload Time: " + timeReload + "\n" + "Max Ammo: " + ammoMax;
    }
}
