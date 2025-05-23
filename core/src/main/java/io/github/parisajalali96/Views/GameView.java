package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import io.github.parisajalali96.Controllers.GameController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.PauseMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.*;
import io.github.parisajalali96.Models.Enums.AbilityType;

import java.io.IOException;
import java.util.List;

public class GameView implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private GameMap map;
    private BitmapFont font;

    //ability pop up
    private List<AbilityType> abilityOptions = null;
    private Stage abilityStage;
    private Skin skin;
    private boolean isPaused = false;

    //controller
    private final GameController controller = new GameController();
    {
        controller.setView(this);
    }

    //win pop up
    private Stage winStage;
    private boolean endGamePopupActive = false;

    //cheat code pop up
    private Stage cheatCodeStage;
    private boolean cheatCodeActive = false;

    //defaults
    private Stage defaultsStage;
    private float animationStateTime = 0f;
    private Table healthBar;
    private Array<Image> heartImages = new Array<>();
    private Label bulletCountLabel;
    private Label killsLabel;

    //time countdown
    private Label timeLabel;
    private Label surviveLabel;

    //level progress bar
    private ProgressBar levelBar;
    private Label levelLabel;

    //auto aim
    private boolean isAutoAimActive = false;
    private Enemy currentlyTargetedEnemy = null;

    @Override
    public void show() {
        GameAssetManager.playNextTrack();
        Game.setGameView(this);
        skin = GameAssetManager.getGameAssetManager().getSkin();
        abilityStage = new Stage();
        winStage = new Stage();
        cheatCodeStage = new Stage();
        defaultsStage = new Stage();
        healthBar = new Table();
        healthBar = new Table();
        healthBar.top().left().padTop(10).padLeft(10);
        defaultsStage.addActor(healthBar);
        Gdx.input.setInputProcessor(abilityStage);
        batch = new SpriteBatch();
        font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.7f;
        camera.update();

        map = new GameMap();
        timeLabel = new Label("", skin, "title");
        timeLabel.setAlignment(Align.right);
        timeLabel.setPosition(Gdx.graphics.getWidth(), 0);
        surviveLabel = new Label("Survive!", skin, "subtitle");
        surviveLabel.setAlignment(Align.right);

        for(TextureRegion t : GameAssetManager.getHeartAnimation().getKeyFrames()) {
            heartImages.add(new Image(new TextureRegion(t)));
        }
        initHealthBar();
        levelLabel = new Label("Level " + Game.getCurrentPlayer().getLevel(), skin, "subtitle");
        levelLabel.setAlignment(Align.center);
        defaultsStage.addActor(levelLabel);

    }

    public void initHealthBar() {
        int maxHearts = 5;
        healthBar.clear();
        heartImages.clear();

        Table heartsRow = new Table();
        for (int i = 0; i < maxHearts; i++) {
            Image heart = new Image(GameAssetManager.getEmptyHeart());
            heart.setSize(24, 24);
            heartsRow.add(heart).padRight(5);
            heartImages.add(heart);
        }
        healthBar.add(heartsRow).left().row();

        // Bullet row
        Image bulletIcon = new Image(GameAssetManager.getBulletTexture());
        bulletIcon.setSize(24, 24);
        bulletIcon.setScaling(Scaling.fit);

        bulletCountLabel = new Label("", skin);
        bulletCountLabel.setFontScale(1.2f);
        bulletCountLabel.setAlignment(Align.left);

        Table bulletRow = new Table();
        bulletRow.add(bulletIcon).padRight(5);
        bulletRow.add(bulletCountLabel).left();

        healthBar.add(bulletRow).left().row();

        // Kills row
        killsLabel = new Label("", skin);
        killsLabel.setFontScale(1.2f);
        killsLabel.setAlignment(Align.left);
        healthBar.add(killsLabel).left().padTop(3f).row();

        healthBar.pack();
        healthBar.setPosition(10, Gdx.graphics.getHeight() - healthBar.getHeight() - 10);
        createLevelProgressBar();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.125f, 0.102f, 0.141f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        updateHealthBar(delta);

        if (abilityOptions != null && abilityStage.getActors().size == 0) {
            isPaused = true;
            showAbilityOptions();
            //return;
        }

        if(endGamePopupActive || cheatCodeActive) {
            isPaused = true;
            //return;
        }

        if (!isPaused) {
            map.update(delta);
            Game.update(delta);
            Game.getCurrentPlayer().update(delta, this.camera);

            if (Gdx.input.isKeyJustPressed(KeyControl.reloadWeapon)) {
                Game.getCurrentPlayer().getWeapon().reload();
            } else if(Gdx.input.isKeyJustPressed(KeyControl.cheatCodeMenu)) {
                cheatCodeActive = true;
                cheatCodeMenuPopUp();
            } else if(Gdx.input.isKeyJustPressed(KeyControl.pauseGame)) {
                isPaused = true;
                Main.getMain().setScreen(new PauseMenu(new PauseMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            } else if(Gdx.input.isKeyJustPressed(KeyControl.autoAim)) {
                if(isAutoAimActive) isAutoAimActive = false;
                else isAutoAimActive = true;
            }

        }

        if(isAutoAimActive) autoAim();


        // Camera follows player
        camera.position.set(
            Game.getCurrentPlayer().getPosition().x + 32,
            Game.getCurrentPlayer().getPosition().y + 32,
            0
        );
        camera.update();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.draw(batch);
        Game.getCurrentPlayer().draw(batch);
        batch.end();

        String timeText = formatTime(Game.getCountdownTime());
        timeLabel.setText(timeText);
        timeLabel.setPosition(Gdx.graphics.getWidth() - 20, Gdx.graphics.getHeight() - 60);
        surviveLabel.setPosition(Gdx.graphics.getWidth() - 220, Gdx.graphics.getHeight() - 150);

        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        timeLabel.draw(batch, 1);
        surviveLabel.draw(batch, 1);
        batch.end();



        if (abilityStage != null && abilityStage.getActors().size > 0) {
            abilityStage.act(Gdx.graphics.getDeltaTime());
            abilityStage.draw();
        }

        if(endGamePopupActive) {
            winStage.act(Gdx.graphics.getDeltaTime());
            winStage.draw();
        }
        if(cheatCodeActive) {
            cheatCodeStage.act(Gdx.graphics.getDeltaTime());
            cheatCodeStage.draw();
        }


        float barX = levelBar.getX();
        float barY = levelBar.getY();
        float barWidth = levelBar.getWidth();
        float barHeight = levelBar.getHeight();

        levelLabel.setSize(barWidth, barHeight);
        levelLabel.setPosition(barX, barY);

        int currentXp = Game.getCurrentPlayer().getXp();
        int level = Game.getCurrentPlayer().getLevel();
        int xpForCurrentLevel = (level - 1) * 20;
        int xpForNextLevel = level * 20;

        int xpGainedInLevel = currentXp - xpForCurrentLevel;
        int xpNeededForLevelUp = xpForNextLevel - xpForCurrentLevel;

        float progress = (float) xpGainedInLevel / xpNeededForLevelUp;
        levelBar.setValue(progress);
        levelLabel.setText("Level " + level);



        defaultsStage.act(delta);
        defaultsStage.draw();

    }

    private String formatTime(float seconds) {
        int minutes = (int)(seconds / 60);
        int secs = (int)(seconds % 60);
        return String.format("%02d:%02d", minutes, secs);
    }

    public void showAbilityOptions() {
        abilityStage.clear();

        Table container = new Table();
        container.setFillParent(true);
        container.center();

        Table window = new Table(skin);
        window.setBackground(skin.newDrawable("window", Color.BLACK.cpy().mul(0.75f)));
        window.pad(40);

        float buttonSize = 140f;

        window.defaults().pad(10);

        Label levelUp = new Label("Level " + Game.getCurrentPlayer().getLevel(), skin, "title");
        levelUp.setAlignment(Align.center);
        window.add(levelUp).colspan(abilityOptions.size()).padBottom(5).row();

        Label title = new Label("Choose an Ability", skin);
        title.setAlignment(Align.center);
        window.add(title).colspan(abilityOptions.size()).padBottom(10).row();

        for (AbilityType ability : abilityOptions) {
            Texture icon = ability.getIcon();

            TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(icon));
            ImageButton button = new ImageButton(drawable);
            button.getImage().setScaling(Scaling.fit);
            button.getImage().setSize(buttonSize, buttonSize);

            Label nameLabel = new Label(ability.getName(), skin);
            nameLabel.setAlignment(Align.center);
            nameLabel.setWrap(true);
            nameLabel.setFontScale(0.9f);

            Table abilityCell = new Table();
            abilityCell.add(button).size(buttonSize, buttonSize).row();
            abilityCell.add(nameLabel).width(buttonSize + 20).padTop(5);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Game.getCurrentPlayer().addAbility(ability);
                    ability.useAbility();
                    abilityStage.clear();
                    Gdx.input.setInputProcessor(null);
                    abilityOptions = null;
                    isPaused = false;
                }
            });

            window.add(abilityCell).pad(10);
        }

        container.add(window).width(abilityOptions.size() * 180).height(280);
        abilityStage.addActor(container);

        Gdx.input.setInputProcessor(abilityStage);
    }

    //for win/lose status
    public void endGameWindow(boolean win) {
        endGamePopupActive = true;
        winStage.clear();

        Table container = new Table();
        container.setFillParent(true);
        container.center();

        Table window = new Table(skin);
        window.setBackground(skin.newDrawable("window", Color.BLACK.cpy().mul(0.85f)));
        window.pad(100);

        //status label
        String statusText = win ? "YOU WIN!" : "YOU LOSE!";
        if(win) GameAssetManager.playSfx("youWin");
        else GameAssetManager.playSfx("youLose");
        Label statusLabel = new Label(statusText, skin, "title");
        statusLabel.setAlignment(Align.center);
        window.add(statusLabel).colspan(2).padBottom(20).row();

        Label userNameLabel = new Label("Username: " + Game.getCurrentPlayer().getUser().getUsername(), skin);
        Label survivalTime = new Label("Survival Time: " + formatTime(Game.getSecondsPassed()), skin);
        Label killsLabel = new Label("Total Kills: " + Game.getCurrentPlayer().getKills(), skin);
        Label scoreLabel = new Label("Total Score: " + (int)(Game.getSecondsPassed()*Game.getCurrentPlayer().getKills()), skin);
        killsLabel.setAlignment(Align.center);
        scoreLabel.setAlignment(Align.center);

        window.add(userNameLabel).colspan(10).row();
        window.add(survivalTime).colspan(10).row();
        window.add(killsLabel).colspan(10).row();
        window.add(scoreLabel).colspan(10).row();

        TextButton exitButton = new TextButton("Continue", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });

        window.add(exitButton).padTop(30).colspan(2).center().row();

        container.add(window).width(600).height(400);
        winStage.addActor(container);
        container.toFront();
        Gdx.input.setInputProcessor(winStage);
    }

    //cheat code menu pop up
    public void cheatCodeMenuPopUp(){
        cheatCodeActive = true;
        cheatCodeStage.clear();

        Table container = new Table();
        container.setFillParent(true);
        container.center();

        Table window = new Table(skin);
        window.setBackground(skin.newDrawable("window", Color.BLACK.cpy().mul(0.85f)));
        window.pad(100);

        Label errorLabel = new Label("- NO SUCH CHEAT CODE -", skin);
        errorLabel.setAlignment(Align.center);
        errorLabel.setVisible(false);
        window.add(errorLabel).colspan(2).padBottom(20).row();

        Label title = new Label("Cheat Code Menu", skin, "title");
        title.setAlignment(Align.center);
        window.add(title).colspan(2).padBottom(20).row();

        Label text = new Label("Type the secret code:", skin);
        text.setAlignment(Align.center);
        window.add(text).colspan(2).padBottom(20).row();

        TextField cheatCodeInput = new TextField("", skin);
        cheatCodeInput.setAlignment(Align.center);
        window.add(cheatCodeInput).width(400).colspan(2).padBottom(5).row();


        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String code = cheatCodeInput.getText().toUpperCase().trim();
                boolean valid = true;

                switch (code) {
                    case "TIMEMACHINE":
                        controller.gameTimeCheatCode();
                        break;
                    case "LEVELBOOST":
                        controller.playerLevelCheatCode();
                        break;
                    case "CPR":
                        controller.playerHPCheatCode();
                        break;
                    case "BIGBOSS":
                        controller.bossFightCheatCode();
                        break;
                    case "STORMRIDER":
                        controller.doubleMaxSpeedCheatCode();
                        break;
                    default:
                        valid = false;
                        break;
                }

                if (valid) {
                    cheatCodeInput.clear();
                    Gdx.input.setInputProcessor(null);
                    abilityOptions = null;
                    cheatCodeActive = false;
                    isPaused = false;
                    cheatCodeStage.clear();
                } else {
                    errorLabel.setVisible(true);

                    cheatCodeStage.addAction(Actions.sequence(
                        Actions.delay(1f),
                        Actions.run(() -> {
                            cheatCodeStage.clear();
                            cheatCodeActive = false;
                            isPaused = false;
                            abilityOptions = null;
                            Gdx.input.setInputProcessor(null);
                        })
                    ));

                    cheatCodeInput.setText("");
                }

            }
        });


        window.add(confirmButton).padTop(30).colspan(2).center().row();

        container.add(window).width(950).height(500);
        cheatCodeStage.addActor(container);
        Gdx.input.setInputProcessor(cheatCodeStage);
    }

    //health bar
    public void updateHealthBar(float delta) {
        animationStateTime += delta;

        int currentHP = Game.getCurrentPlayer().getHealth();
        int maxHP = Game.getCurrentPlayer().getHero().getHP();
        int filledHearts = (int) Math.round((double) currentHP / maxHP * heartImages.size);

        Animation<TextureRegion> heartAnim = GameAssetManager.getHeartAnimation();
        TextureRegion emptyHeart = GameAssetManager.getEmptyHeart();

        for (int i = 0; i < heartImages.size; i++) {
            Image heart = heartImages.get(i);
            if (i < filledHearts) {
                heart.setDrawable(new TextureRegionDrawable(heartAnim.getKeyFrame(animationStateTime, true)));
            } else {
                heart.setDrawable(new TextureRegionDrawable(emptyHeart));
            }
        }

        int ammoLeft = Game.getCurrentPlayer().getWeapon().getAmmo();
        int maxAmmo = Game.getCurrentPlayer().getWeapon().getType().getAmmoMax();
        bulletCountLabel.setText(ammoLeft + "\\" + maxAmmo);

        killsLabel.setText("Kills: " + Game.getCurrentPlayer().getKills());
    }

    //level progress bar
    public void createLevelProgressBar() {

        levelBar = new ProgressBar(0f, 1f, 0.01f, false, skin, "default-horizontal");
        float width = Gdx.graphics.getWidth() * 0.5f;
        float height = 50;
        float x = (Gdx.graphics.getWidth() - width) / 2f;
        float y = Gdx.graphics.getHeight() - height - 10;

        levelBar.setSize(width, height);
        levelBar.setPosition(x, y);
        defaultsStage.addActor(levelBar);

    }
    public void setPaused(boolean paused) {
        isPaused = paused;
    }
    public boolean isPaused() {
        return isPaused;
    }

    public void autoAim() {
        float maxSnapDistance = 150f;
        Vector2 mouseScreenPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector3 mouseWorldPos3 = camera.unproject(new Vector3(mouseScreenPos.x, mouseScreenPos.y, 0));
        Vector2 mouseWorldPos = new Vector2(mouseWorldPos3.x, mouseWorldPos3.y);

        Enemy closestEnemy = null;
        float closestDistance = maxSnapDistance;

        for (Enemy enemy : Game.getMap().getEnemies()) {
            Vector2 enemyPos = enemy.getPosition();
            float dist = enemyPos.dst(mouseWorldPos);
            if (dist < closestDistance) {
                closestDistance = dist;
                closestEnemy = enemy;
            }
        }

        if (closestEnemy != null) {
            if (currentlyTargetedEnemy == null || !currentlyTargetedEnemy.equals(closestEnemy)) {
                currentlyTargetedEnemy = closestEnemy;

                Vector2 enemyPos = currentlyTargetedEnemy.getPosition();
                Vector3 enemyScreenPos3 = camera.project(new Vector3(enemyPos.x, enemyPos.y, 0));
                Gdx.input.setCursorPosition((int) enemyScreenPos3.x, (int) enemyScreenPos3.y);
            }
        } else {
            currentlyTargetedEnemy = null;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        Game.getCurrentPlayer().dispose();
    }

    public void setAbilityOptions(List<AbilityType> abilityOptions) {
        this.abilityOptions = abilityOptions;
    }

    public GameController getController() {
        return controller;
    }

}

