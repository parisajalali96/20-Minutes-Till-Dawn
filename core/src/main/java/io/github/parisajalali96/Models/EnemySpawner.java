package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.parisajalali96.Models.Enums.EnemyType;

import java.util.ArrayList;
import java.util.List;

public class EnemySpawner {

    public static Animation<TextureRegion> loadIdleAnimation(EnemyType type) {
        List<TextureRegion> frames = new ArrayList<>();
        for (String path : type.getIdleTexturePaths()) {
            Texture texture = new Texture(Gdx.files.internal(path));
            frames.add(new TextureRegion(texture));
        }
        TextureRegion[] framesArray = frames.toArray(new TextureRegion[0]);
        return new Animation<>(0.25f, framesArray);
    }


    public static Animation<TextureRegion> loadSpawnAnimation(EnemyType type) {
        List<TextureRegion> frames = new ArrayList<>();
        for (String path : type.getSpawnTexturePaths()) {
            Texture texture = new Texture(Gdx.files.internal(path));
            frames.add(new TextureRegion(texture));
        }
        TextureRegion[] framesArray = frames.toArray(new TextureRegion[0]);
        return new Animation<>(0.25f, framesArray);
    }

    public static Animation<TextureRegion> loadAttackAnimation(EnemyType type) {
        List<TextureRegion> frames = new ArrayList<>();
        for (String path : type.getAttackTexturePaths()) {
            Texture texture = new Texture(Gdx.files.internal(path));
            frames.add(new TextureRegion(texture));
        }
        TextureRegion[] framesArray = frames.toArray(new TextureRegion[0]);
        return new Animation<>(0.25f, framesArray);
    }

    public static Animation<TextureRegion> loadDeathAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (String path : EnemyType.getDeathTexturePaths()) {
            Texture texture = new Texture(Gdx.files.internal(path));
            frames.add(new TextureRegion(texture));
        }
        TextureRegion[] framesArray = frames.toArray(new TextureRegion[0]);
        return new Animation<>(0.25f, framesArray);
    }
}
