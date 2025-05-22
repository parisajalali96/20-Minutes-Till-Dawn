package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.graphics.Texture;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Player;
import io.github.parisajalali96.Models.Weapon;

public enum AbilityType {

    VITALITY("Vitality", "Images/Sprite/Icon_SoothingWarmth.png", "Increases player HP")
        {
            //increase health
            @Override
            public void useAbility(){
                Game.getCurrentPlayer().addHealth(1);
            }
        },
    DAMAGER("Damager", "Images/Sprite/Icon_Assassin.png", "Increases weapon damage" + "\n" + "by 25% for 10 seconds")
        {
            //increase weapon damage for 10 seconds
            @Override
            public void useAbility(){
                Weapon weapon = Game.getCurrentPlayer().getWeapon();
                weapon.addDamage((int) (weapon.getType().getDamage()*0.25));
            }
        },
    PROCREASE("Procrease", "Images/Sprite/Icon_Recharge.png", "Adds to weapon's projectiles")
        {
            //increase weapon projectile
            @Override
            public void useAbility(){
                Weapon weapon = Game.getCurrentPlayer().getWeapon();
                weapon.addProjectile(1);
            }
        },
    AMOCREASE("Amocrease", "Images/Sprite/Icon_Sniper.png", "Adds to weapon's ammo")
        {
            //increase ammo
            @Override
            public void useAbility(){
                Weapon weapon = Game.getCurrentPlayer().getWeapon();
                weapon.addAmmo(5);
            }
        },
    SPEEDY("Speedy", "Images/Sprite/Icon_Electro_Affinity.png", "Doubles player's speed" + "\n" +" for 10 seconds" )
        {
            //double speed for 10 seconds
            @Override
            public void useAbility(){
                Player currentPlayer = Game.getCurrentPlayer();
                currentPlayer.addSpeed(currentPlayer.getSpeed());
            }
        };

    private final String name;
    private final String imagePath;
    private final String description;
    AbilityType(String name, String imagePath, String description) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
    }
    public abstract void useAbility();
    public String getName() {
        return name;
    }
    public Texture getIcon(){
        return new Texture(imagePath);
    }
    public String getDescription(){
        return name.toUpperCase() + "\n" + description;
    }
}
