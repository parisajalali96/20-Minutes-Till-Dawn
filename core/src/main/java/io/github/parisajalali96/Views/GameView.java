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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import io.github.parisajalali96.Controllers.GameController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Enums.AbilityType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.GameMap;
import io.github.parisajalali96.Models.KeyControl;

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


    @Override
    public void show() {
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

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
            }

        }


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

        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        String timeText = formatTime(Game.getCountdownTime());
        font.draw(batch, timeText, Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 20);
        batch.end();

        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        String health = String.valueOf(Game.getCurrentPlayer().getHealth());
        font.draw(batch, health, 10, Gdx.graphics.getHeight() - 10);
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
        Label statusLabel = new Label(statusText, skin, "title");
        statusLabel.setAlignment(Align.center);
        window.add(statusLabel).colspan(2).padBottom(20).row();

        Label killsLabel = new Label("Total Kills: " + Game.getCurrentPlayer().getKills(), skin);
        Label scoreLabel = new Label("Total Score: " + Game.getCurrentPlayer().getScore(), skin);
        killsLabel.setAlignment(Align.center);
        scoreLabel.setAlignment(Align.center);

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
    public void updateHealthBar(float delta){
        animationStateTime += delta;

        healthBar.clear();

        int maxHearts = 5;
        int heartsToFill = (int) Math.round((double) Game.getCurrentPlayer().getHealth() / Game.getCurrentPlayer().getHero().getHP() * maxHearts);

        Animation<TextureRegion> hearts = GameAssetManager.getHeartAnimation();
        TextureRegion emptyHeart = GameAssetManager.getEmptyHeart();

        for (int i = 1; i <= maxHearts; i++) {
            Image heartImage;
            if (i <= heartsToFill) {
                TextureRegion currentFrame = hearts.getKeyFrame(animationStateTime, true);
                heartImage = new Image(currentFrame);
            } else {
                heartImage = new Image(emptyHeart);
            }

            heartImage.setSize(24, 24);
            healthBar.add(heartImage).padRight(5);
        }

        healthBar.pack();
        healthBar.setPosition(10, Gdx.graphics.getHeight() - healthBar.getHeight() - 10);
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

