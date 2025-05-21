package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
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


    @Override
    public void show() {
        Game.setGameView(this);
        skin = GameAssetManager.getGameAssetManager().getSkin();
        abilityStage = new Stage();
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


        if (abilityOptions != null && abilityStage.getActors().size == 0) {
            isPaused = true;
            showAbilityOptions();
            return;
        }

        if (!isPaused) {
            map.update(delta);
            Game.update(delta);
            Game.getCurrentPlayer().update(delta, this.camera);

            if (Gdx.input.isKeyJustPressed(KeyControl.reloadWeapon)) {
                Game.getCurrentPlayer().getWeapon().reload();
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
        window.setBackground(skin.newDrawable("window", Color.BLACK.cpy().mul(0.85f)));
        window.pad(100);

        float buttonSize = 140f;
        float popupWidth = abilityOptions.size() * (buttonSize + 40);
        float popupHeight = 320f;

        window.defaults().pad(40);

        Label levelUp = new Label("Level " + Game.getCurrentPlayer().getLevel(), skin, "title");
        levelUp.setAlignment(Align.center);
        window.add(levelUp).colspan(abilityOptions.size()).padBottom(10).row();

        Label title = new Label("Choose an Ability", skin);
        title.setAlignment(Align.center);
        window.add(title).colspan(abilityOptions.size()).padBottom(15).row();

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
            abilityCell.add(nameLabel).width(buttonSize + 20).padTop(10);

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

            window.add(abilityCell).pad(15);
        }

        container.add(window).width(abilityOptions.size() * 140).height(250);
        abilityStage.addActor(container);

        Gdx.input.setInputProcessor(abilityStage);
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

}

