package io.github.parisajalali96.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    private Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
    private Music music;
    private static boolean sfxEnabled = true;
    private static ArrayList<Music> playList = new ArrayList<>();
    private static int currentMusicIndex = 0;
    private static Map<String, Sound> sfxMap = new HashMap<>();

    private GameAssetManager() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("Images/Texture2D/T_CursorSprite.png"));
        int hotspotX = 0, hotspotY = 0;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, hotspotX, hotspotY);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

        playList.add(Gdx.audio.newMusic(Gdx.files.internal("AudioClip/Pretty Dungeon LOOP.wav")));
        playList.add(Gdx.audio.newMusic(Gdx.files.internal("AudioClip/Wasteland Combat Loop.wav")));

        sfxMap.put("click", Gdx.audio.newSound(Gdx.files.internal("AudioClip/UI Click 36.wav")));
        sfxMap.put("gunReload", Gdx.audio.newSound(Gdx.files.internal("AudioClip/Weapon_Shotgun_Reload.wav")));
        sfxMap.put("youWin", Gdx.audio.newSound(Gdx.files.internal("AudioClip/You Win (2).wav")));
        sfxMap.put("youLose", Gdx.audio.newSound(Gdx.files.internal("AudioClip/You Lose (4).wav")));
        sfxMap.put("batDeath", Gdx.audio.newSound(Gdx.files.internal("AudioClip/Bat_Death_02.wav")));
        sfxMap.put("monsterAttack", Gdx.audio.newSound(Gdx.files.internal("AudioClip/Monster_2_Attack_Quick_01_WITH_ECHO.wav")));
        sfxMap.put("shot", Gdx.audio.newSound(Gdx.files.internal("AudioClip/single_shot.wav")));
        sfxMap.put("levelUpUpgrade", Gdx.audio.newSound(Gdx.files.internal("AudioClip/Special & Powerup (10).wav")));
        sfxMap.put("xpDrop", Gdx.audio.newSound(Gdx.files.internal("AudioClip/Crystal Reward Tick.wav")));

        playNextTrack();
    }

    public void setMusicVolume(float volume) {
        //music.setVolume(volume);
    }

    public boolean isSfxEnabled() {
        //TODO implement
        return sfxEnabled;
    }

    public String getMusic() {
        return "music";
    }

    public void setMusic(String music) {
        //TODO implement this
    }

    public void setSfxEnabled(boolean sfxEnabled) {
        this.sfxEnabled = sfxEnabled;
    }

    public Skin getSkin() {
        return skin;
    }

    public static GameAssetManager getGameAssetManager() {
        if(gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    public static Animation<TextureRegion> getHeartAnimation(){
        TextureRegion[] hearts = new TextureRegion[3];
        hearts[0] = new TextureRegion(new Texture("Images/Sprite/HeartAnimation_0.png"));
        hearts[1] = new TextureRegion(new Texture("Images/Sprite/HeartAnimation_1.png"));
        hearts[2] = new TextureRegion(new Texture("Images/Sprite/HeartAnimation_2.png"));
        return new Animation<>(0.25f, hearts);
    }

    public static TextureRegion getEmptyHeart(){
        return new TextureRegion(new Texture("Images/Sprite/HeartAnimation_3.png"));
    }

    public static TextureRegion getBulletTexture(){
        return new TextureRegion(new Texture("Images/Sprite/T_AmmoIcon.png"));
    }

    public static ShaderProgram getShader(){
        String vertexShaderCode = Gdx.files.internal("shader.glsl").readString();
        String fragmentShaderCode = Gdx.files.internal("gray.glsl").readString();

        ShaderProgram grayscaleShader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);

        if (!grayscaleShader.isCompiled()) {
            System.err.println("Shader compilation failed:\n" + grayscaleShader.getLog());
        }
        return grayscaleShader;
    }

    public static void playNextTrack() {
        if (currentMusicIndex >= playList.size()) currentMusicIndex = 0;

        Music currentMusic = playList.get(currentMusicIndex);
        currentMusic.play();

        currentMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                currentMusicIndex++;
                playNextTrack();
            }
        });
    }

    public static void playSfx(String name) {
        if (!sfxEnabled) return;

        Sound sound = sfxMap.get(name);
        if (sound != null) {
            sound.play(1.0f);
        } else {
            Gdx.app.log("GameAssetManager", "SFX not found: " + name);
        }
    }


    public float getMusicVolume() {
        return 10;
    }
}
