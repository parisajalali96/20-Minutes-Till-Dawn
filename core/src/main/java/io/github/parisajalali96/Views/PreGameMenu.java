package io.github.parisajalali96.Views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.PreGameMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Enums.GameTime;
import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.WeaponType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Weapon;

public class PreGameMenu implements Screen{
    private Stage stage;
    private Skin skin;
    private Table table;
    private final PreGameMenuController controller;
    private final Label heroLabel;
    private final Label weaponLabel;
    private final Label gameTimeLabel;
    private final SelectBox<String> gameTimeSelectBox;
    private final TextButton startGameButton;
    private final TextButton backButton;

    private int selectedDuration = 20;

    //new pregame
    private Window heroInfoWindow;
    private Label heroInfoLabel;
    private TextButton chooseHeroButton;
    private Window weaponInfoWindow;
    private Label weaponInfoLabel;
    private TextButton chooseWeaponButton;
    private TextButton chooseGameTimeButton;
    private Hero selectedHero;
    private WeaponType selectedWeapon;
    private Image chosenHeroImage;

    public PreGameMenu(PreGameMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        heroLabel = new Label("Select Hero:", skin);
        weaponLabel = new Label("Select Weapon:", skin);
        gameTimeLabel = new Label("Select Game Duration:", skin);
        gameTimeSelectBox = new SelectBox<>(skin);
        startGameButton = new TextButton("Start Game", skin);
        backButton = new TextButton("Back", skin);
    }


    public void startGame() {
        startGameButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new GameView());
            }
        });
    }

    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createHeroInfoWindow(Game.getCurrentPlayer().getHero().getTexture());
        createHeroGrid();

        chosenHeroImage = new Image();
        chosenHeroImage.setSize(500, 500);
        chosenHeroImage.setScaling(Scaling.fit);
        chosenHeroImage.setVisible(false);

        chosenHeroImage.setPosition(
            Gdx.graphics.getWidth() - chosenHeroImage.getWidth() - 50,
            Gdx.graphics.getHeight() / 2f - chosenHeroImage.getHeight() / 2f
        );

        stage.addActor(chosenHeroImage);

        createWeaponInfoWindow(Game.getCurrentPlayer().getWeapon().getType().getTexture());
        heroInfoWindow.setVisible(false);
        weaponInfoWindow.setVisible(false);
        createWeaponGrid();
        createGameDurationSelector();

        // Add buttons to the stage
        stage.addActor(startGameButton);
        stage.addActor(backButton);

        startGameButton.setPosition(10, 10);

        backButton.setPosition(
            stage.getWidth() - backButton.getWidth() - 10,
            10
        );

        startGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new MainMenu(
                    new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()
                ));
            }
        });

    }

    private void createHeroGrid() {
        Table heroTable = new Table();
        Label selectedHeroLabel = new Label("Select Hero:", skin);
        heroTable.add(selectedHeroLabel);
        heroTable.setFillParent(true);
        heroTable.top().padTop(20);

        for (Hero hero : Hero.values()) {
            Image heroImage = new Image(new TextureRegionDrawable(hero.getTexture()));
            heroImage.setSize(64, 64);
            heroImage.setScaling(Scaling.fit);

            heroImage.addListener(new InputListener() {
                @Override
                public boolean mouseMoved(InputEvent event, float x, float y) {
                    showHeroInfoWindow(hero);
                    return true;
                }
            });

            heroTable.add(heroImage)
                .padLeft(30)
                .padRight(30)
                .padBottom(10)
                .size(64, 64);
        }

        stage.addActor(heroTable);
    }

    private void createWeaponGrid(){
        Table weaponTable = new Table();
        Label selectWeaponLabel = new Label("Select Weapon:", skin);
        weaponTable.add(selectWeaponLabel).colspan(WeaponType.values().length).row();

        weaponTable.setFillParent(true);
        weaponTable.top().padTop(150);

        for(WeaponType weaponType : WeaponType.values()){
            Image weaponImage = new Image(new TextureRegionDrawable(weaponType.getTexture()));
            weaponImage.setSize(200, 200);
            weaponImage.setScaling(Scaling.fit);

            weaponImage.addListener(new InputListener() {
                @Override
                public boolean mouseMoved(InputEvent event, float x, float y) {
                    showWeaponInfoWindow(weaponType);
                    return true;
                }
            });

            weaponTable.add(weaponImage)
                .padLeft(30)
                .padRight(30)
                .padBottom(10)
                .size(64, 64);
        }

        stage.addActor(weaponTable);
    }



    private void showHeroInfoWindow(Hero hero) {
        selectedHero = hero;
        if (heroInfoWindow != null) {
            heroInfoWindow.remove();
        }
        createHeroInfoWindow(hero.getTexture());
        heroInfoLabel.setText(hero.getDescription());
    }

    private void showWeaponInfoWindow(WeaponType weapon) {
        selectedWeapon = weapon;
        if(weaponInfoWindow!=null){
            weaponInfoWindow.remove();
        }
        createWeaponInfoWindow(weapon.getTexture());
        weaponInfoLabel.setText(weapon.getDescription());
    }



    public void createWeaponInfoWindow(Texture weaponTexture){
        weaponInfoWindow = new Window("Weapon Info", skin);

        Image weaponImage = new Image(new TextureRegionDrawable(weaponTexture));
        //weaponImage.setSize(200, 200);
        weaponImage.setScaling(Scaling.fit);

        weaponInfoLabel = new Label("", skin);
        chooseWeaponButton = new TextButton("Choose Weapon", skin);

        chooseWeaponButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedWeapon != null) {
                    Game.getCurrentPlayer().setWeapon(new Weapon(selectedWeapon));
                    weaponInfoWindow.setVisible(false);
                }
            }
        });

        weaponInfoWindow.add(weaponImage)
            .size(100, 100)
            .center()
            .padBottom(10)
            .row();        weaponInfoWindow.add(weaponInfoLabel).left().padBottom(10).row();
        weaponInfoWindow.add(chooseWeaponButton).padBottom(10).row();

        weaponInfoWindow.setSize(500, 500);
        weaponInfoWindow.setVisible(true);
        weaponInfoWindow.setPosition(
            (Gdx.graphics.getWidth() - weaponInfoWindow.getWidth()) / 2f,
            ((Gdx.graphics.getHeight() - weaponInfoWindow.getHeight()) / 2f) - 100);

        stage.addActor(weaponInfoWindow);
    }

    public void createHeroInfoWindow(Texture heroTexture) {
        heroInfoWindow = new Window("Hero Info", skin);

        Image heroImage = new Image(new TextureRegionDrawable(heroTexture));
        heroImage.setSize(200, 200);
        heroImage.setScaling(Scaling.fit);

        heroInfoLabel = new Label("", skin);
        chooseHeroButton = new TextButton("Choose Hero", skin);

        chooseHeroButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedHero != null) {
                    Game.getCurrentPlayer().setHero(selectedHero);
                    heroInfoWindow.setVisible(false);
                    chosenHeroImage.setDrawable(new TextureRegionDrawable(selectedHero.getTexture()));
                    chosenHeroImage.setVisible(true);
                }
            }
        });

        heroInfoWindow.add(heroImage).center().padBottom(10).row();
        heroInfoWindow.add(heroInfoLabel).left().padBottom(10).row();
        heroInfoWindow.add(chooseHeroButton).padBottom(10).row();

        heroInfoWindow.setSize(400, 500);
        heroInfoWindow.setVisible(true);
        heroInfoWindow.setPosition(
            (Gdx.graphics.getWidth() - heroInfoWindow.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - heroInfoWindow.getHeight()) / 2f
        );

        stage.addActor(heroInfoWindow);
    }


    public void createGameDurationSelector(){
        Table durationTable = new Table();
        Label gameDurationLabel = new Label("Select Game Duration:", skin);

        durationTable.setFillParent(true);
        durationTable.top().padTop(400);
        durationTable.add(gameDurationLabel).colspan(3).padBottom(20).row();

        int[] durations = {2,5,10,20};
        for(int i = 0; i < durations.length; i++){
            final Label durationLabel = new Label(durations[i] + "", skin, "title");

            int finalI = i;
            durationLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedDuration = durations[finalI];
                    Game.setTime(GameTime.getGameTime(selectedDuration));
                    updateDurationSelection(durationLabel);
                }
            });
            durationTable.add(durationLabel).padLeft(15).padRight(15);
        }
        stage.addActor(durationTable);
    }

    private void updateDurationSelection(Label selectedLabel) {
        for (Actor actor : selectedLabel.getParent().getChildren()) {
            if (actor instanceof Label) {
                ((Label) actor).setColor(Color.WHITE);
            }
        }

        selectedLabel.setColor(new Color(1f, 0.2f, 0.6f, 1f));
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.125f, 0.102f, 0.141f, 1f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
