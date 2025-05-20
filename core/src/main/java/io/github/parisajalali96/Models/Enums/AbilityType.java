package io.github.parisajalali96.Models.Enums;

import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.Player;
import io.github.parisajalali96.Models.Weapon;

public enum AbilityType {

    VITALITY("Vitality")
        {
            //increase health
            @Override
            public void useAbility(){
                Game.getCurrentPlayer().addHealth(1);
            }
        },
    DAMAGER("Damager")
        {
            //increase weapon damage for 10 seconds
            @Override
            public void useAbility(){
                Weapon weapon = Game.getCurrentPlayer().getWeapon();
                weapon.addDamage((int) (weapon.getType().getDamage()*0.25));
            }
        },
    PROCREASE("Procrease")
        {
            //increase weapon projectile
            @Override
            public void useAbility(){
                Weapon weapon = Game.getCurrentPlayer().getWeapon();
                weapon.addProjectile(1);
            }
        },
    AMOCREASE("Amocrease")
        {
            //increase ammo
            @Override
            public void useAbility(){
                Weapon weapon = Game.getCurrentPlayer().getWeapon();
                weapon.addAmmo(5);
            }
        },
    SPEEDY("Speedy")
        {
            //double speed for 10 seconds
            @Override
            public void useAbility(){
                Player currentPlayer = Game.getCurrentPlayer();
                currentPlayer.addSpeed(currentPlayer.getSpeed());
            }
        };

    private final String name;
    AbilityType(String name) {
        this.name = name;
    }
    public abstract void useAbility();
}
