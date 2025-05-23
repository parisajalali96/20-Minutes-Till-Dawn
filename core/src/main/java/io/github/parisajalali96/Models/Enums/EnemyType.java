package io.github.parisajalali96.Models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.parisajalali96.Models.Game;

import java.util.List;

public enum EnemyType {
    Tree("Tree", List.of("Images/Sprite/T_TreeMonster_0.png"),List.of("Images/Sprite/T_TreeMonster_1.png", "Images/Sprite/T_TreeMonster_2.png"), List.of("Images/Sprite/T_TreeMonsterWalking.png", "Images/Texture2D/T_TreeMonsterWalking.png"), 0, 1) {
        @Override
        public int getSpawnCount(float secondsPassed) {
            return 0;
        }
    },
    TentacleMonster("Tentacle Monster", List.of("Images/Sprite/TentacleIdle0.png", "Images/Sprite/TentacleIdle1.png", "Images/Sprite/TentacleIdle2.png", "Images/Sprite/TentacleIdle3.png"), List.of("Images/Sprite/TentacleSpawn0.png", "Images/Sprite/TentacleSpawn1.png", "Images/Sprite/TentacleSpawn2.png"),List.of("Images/Sprite/T_TentacleAttack.png"),0, 25) {
        @Override
        public int getSpawnCount(float secondsPassed) {
            if(secondsPassed % 3 == 0) return (int) (secondsPassed / 30);
            else return 0;
        }
    },
    Eyebat("Eyebat", List.of(), List.of(),List.of("Images/Sprite/T_EyeBat_0.png", "Images/Sprite/T_EyeBat_1.png", "Images/Sprite/T_EyeBat_2.png", "Images/Sprite/T_EyeBat_3.png"), 3, 50) {
        @Override
        public int getSpawnCount(float secondsPassed) {
            float time = (int)(secondsPassed / 10f) * 10f;
            return Math.max(0, (int)(2*time - Game.getTime().getTotalSeconds() + 30) / 50);
        }

    },
    Elder("Elder", List.of(), List.of(), List.of("Images/Sprite/ElderBrain.png"), 5, 400)
        {
            private boolean hasSpawned = false;

            @Override
            public int getSpawnCount(float secondsPassed) {
                if (!hasSpawned && secondsPassed >= Game.getTime().getTotalSeconds()/2) {
                    hasSpawned = true;
                    return 1;
                }
                return 0;
            }
        },
    BrainMonster("Brain Monster", List.of(), List.of(), List.of("Images/Sprite/BrainMonster_0.png", "Images/Sprite/BrainMonster_1.png", "Images/Sprite/BrainMonster_2.png", "Images/Sprite/BrainMonster_3.png"), 1, 25) {
        @Override
        public int getSpawnCount(float secondsPassed) {
            if(secondsPassed % 3 == 0) return (int) (secondsPassed / 30);
            else return 0;
        }
    };


    private final String name;
    private final List<String> idleTexturePaths;
    private final List<String> spawnTexturePaths;
    private final List<String> attackTexurePaths;
    private static final List<String> deathTexturePaths = List.of("Images/Sprite/DeathFX_0.png", "Images/Sprite/DeathFX_1.png",
        "Images/Sprite/DeathFX_2.png", "Images/Sprite/DeathFX_3.png");
    private static final String eyeBatProjectileTexturePath = "Images/Sprite/EyeMonsterProjecitle.png";
    private final int damageRate;
    private final int HP;
    EnemyType(String name, List<String> idleTexturePaths, List<String> spawnTexturePaths, List<String> attackTexturePaths, int damageRate, int HP) {
        this.name = name;
        this.damageRate = damageRate;
        this.idleTexturePaths = idleTexturePaths;
        this.spawnTexturePaths = spawnTexturePaths;
        this.attackTexurePaths = attackTexturePaths;
        this.HP = HP;
    }

    public abstract int getSpawnCount(float secondsPassed);

    public List<String> getIdleTexturePaths() {
        return idleTexturePaths;
    }
    public List<String> getSpawnTexturePaths() {
        return spawnTexturePaths;
    }
    public List<String> getAttackTexturePaths(){
        return attackTexurePaths;
    }

    public static List<String> getDeathTexturePaths() {
        return deathTexturePaths;
    }

    public int getHP(){
        return HP;
    }

    public static Texture getEyeBatTexture() {
        return new Texture(eyeBatProjectileTexturePath);
    }

}
