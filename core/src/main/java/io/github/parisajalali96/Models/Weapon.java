package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.WeaponType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Weapon implements Serializable {
    private static final long serialVersionUID = 1L;
    private WeaponType type;
    private transient Texture texture;
    private transient Texture bulletTexture;
    private final List<Projectile> projectiles = new ArrayList<>();
    private int currentNumOfProjectiles;
    private int damage;
    private int projectile;
    private int ammo;

    //for reload
    private transient Animation<TextureRegion> reloadAnimation;
    private float reloadAnimTimer = 0f;
    private boolean reloading = false;
    private float reloadTimer = 0f;
    private float reloadTime;


    public Weapon(WeaponType type) {
        this.type = type;
        this.texture = type.getTexture();
        this.bulletTexture = WeaponType.getBulletTexture();
        currentNumOfProjectiles = type.getAmmoMax();
        reloadTime = type.getTimeReload();

        List<String> reloadAnimationPaths = type.getReloadStagesTextures();
        TextureRegion[] reloadRegions = new TextureRegion[reloadAnimationPaths.size()];

        for (int i = 0; i < reloadAnimationPaths.size(); i++) {
            Texture frameTexture = new Texture(reloadAnimationPaths.get(i));
            reloadRegions[i] = new TextureRegion(frameTexture);
        }
        reloadAnimation = new Animation<>(0.25f, reloadRegions);

        damage = type.getDamage();
        projectile = type.getProjectile();
        ammo = type.getAmmoMax();
    }

    public void initWeapon(){
        this.type = type;
        this.texture = type.getTexture();
        this.bulletTexture = WeaponType.getBulletTexture();
        currentNumOfProjectiles = type.getAmmoMax();
        reloadTime = type.getTimeReload();

        List<String> reloadAnimationPaths = type.getReloadStagesTextures();
        TextureRegion[] reloadRegions = new TextureRegion[reloadAnimationPaths.size()];

        for (int i = 0; i < reloadAnimationPaths.size(); i++) {
            Texture frameTexture = new Texture(reloadAnimationPaths.get(i));
            reloadRegions[i] = new TextureRegion(frameTexture);
        }
        reloadAnimation = new Animation<>(0.25f, reloadRegions);

        damage = type.getDamage();
        projectile = type.getProjectile();
        ammo = type.getAmmoMax();
    }

    public void shoot(Vector2 startPosition, Vector2 direction) {
        //only shoot if weapon has enough ammo
        for(int i = 0; i < type.getProjectile(); i ++) {
            if (reloading) return;
            if(currentNumOfProjectiles <= 0) {
                if(Game.isAutoReloadActive()) {
                    reload();
                }
                return;
            }
            currentNumOfProjectiles--;
            projectiles.add(new Projectile(type.getDamage(), startPosition, direction, bulletTexture));
            GameAssetManager.playSfx("shot");
        }
    }

    public void reload(){
        if(!reloading){
            GameAssetManager.playSfx("gunReload");
            reloading = true;
            reloadTimer = 0f;
        }
    }

    public void update(float delta) {

        if(reloading){
            reloadTimer += delta;
            reloadAnimTimer += delta;
            if(reloadTimer >= reloadTime){
                currentNumOfProjectiles = ammo;
                reloading = false;
                reloadAnimTimer = 0f;
            }
            return;
        }
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);
            if (!p.isActive()) {
                iter.remove();
            }
        }
    }

    public void draw(SpriteBatch batch, Vector2 playerPosition, boolean facingRight) {
        float weaponX = facingRight ? playerPosition.x + 12 : playerPosition.x;
        float weaponY = playerPosition.y + 5;

        if (reloading) {
            TextureRegion frame = reloadAnimation.getKeyFrame(reloadAnimTimer, false);
            if (facingRight) {
                batch.draw(frame, weaponX, weaponY);
            } else {
                batch.draw(frame, weaponX + frame.getRegionWidth(), weaponY, -frame.getRegionWidth(), frame.getRegionHeight());
            }
        } else {
            if (facingRight) {
                batch.draw(texture, weaponX, weaponY);
            } else {
                batch.draw(texture, weaponX + texture.getWidth(), weaponY, -texture.getWidth(), texture.getHeight());
            }
        }


        for (Projectile p : projectiles) {
            p.draw(batch);
        }
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void dispose() {
        texture.dispose();
        bulletTexture.dispose();
    }

    public void addDamage(int damage) {
        this.damage += damage;
    }
    public void addProjectile(int projectile) {
        this.projectile += projectile;
    }
    public void addAmmo(int ammo) {
        this.ammo += ammo;
    }

    public WeaponType getType() {
        return type;
    }
    public int getAmmo() {
        return currentNumOfProjectiles;
    }
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
    public void setProjectile(int projectile) {
        this.projectile = projectile;
    }
    public void setCurrentNumOfProjectiles(int currentNumOfProjectiles) {
        this.currentNumOfProjectiles = currentNumOfProjectiles;
    }
}

