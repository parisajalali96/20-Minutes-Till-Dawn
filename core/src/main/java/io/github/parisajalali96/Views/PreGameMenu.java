package io.github.parisajalali96.Views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.tools.javac.util.List;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.PreGameMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.Weapon;
import io.github.parisajalali96.Models.GameAssetManager;

public class PreGameMenu implements Screen{
    private Stage stage;
    private Skin skin;
    private Table table;
    private final PreGameMenuController controller;
    private final Label heroLabel;
    private final ScrollPane heroScrollPane;
    private final Label weaponLabel;
    private final ScrollPane weaponScrollPane;
    private final Label gameTimeLabel;
    private final SelectBox<String> gameTimeSelectBox;
    private final TextButton startGameButton;
    private final TextButton backButton;


    private Hero selectedHero;
    private Weapon selectedWeapon;
    private String selectedDuration = "20 minutes";

    public PreGameMenu(PreGameMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        heroLabel = new Label("Select Hero:", skin);
        heroScrollPane = createHeroSelection();
        weaponLabel = new Label("Select Weapon:", skin);
        weaponScrollPane = createWeaponSelection();
        gameTimeLabel = new Label("Select Game Duration:", skin);
        gameTimeSelectBox = new SelectBox<>(skin);
        startGameButton = new TextButton("Start Game", skin);
        backButton = new TextButton("Back", skin);
    }

    private ScrollPane createHeroSelection() {
        HorizontalGroup heroGroup = new HorizontalGroup();

        for (Hero hero : Hero.values()) {
            ImageButton heroButton = new ImageButton(new Image(hero.getTexture()).getDrawable());
            heroButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectedHero = hero;
                }
            });
            heroGroup.addActor(heroButton);
        }

        ScrollPane scrollPane = new ScrollPane(heroGroup, skin);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFadeScrollBars(false);
        return scrollPane;
    }

    private ScrollPane createWeaponSelection() {
        HorizontalGroup weaponGroup = new HorizontalGroup();

        for (Weapon weapon : Weapon.values()) {
            TextButton weaponButton = new TextButton(weapon.getName(), skin);
            weaponButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectedWeapon = weapon;
                    System.out.println("Selected weapon: " + weapon.getName());
                }
            });
            weaponGroup.addActor(weaponButton);
        }

        ScrollPane scrollPane = new ScrollPane(weaponGroup, skin);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFadeScrollBars(false);
        return scrollPane;
    }

    public void createTimeSelectBox() {
        gameTimeSelectBox.setItems("2 minutes", "5 minutes", "10 minutes", "20 minutes");
        gameTimeSelectBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedDuration = gameTimeSelectBox.getSelected();
            }
        });
    }

    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);

        table.add(heroLabel).pad(10).colspan(2).center().row();
        table.add(heroScrollPane).fillX().pad(10).colspan(2).row();

        table.add(weaponLabel).pad(10).colspan(2).center().row();
        table.add(weaponScrollPane).fillX().pad(10).colspan(2).row();

        table.add(gameTimeLabel).pad(10).colspan(2).center().row();

        createTimeSelectBox();
        table.add(gameTimeSelectBox).fillX().pad(10).colspan(2).row();

        startGameButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.startGame(selectedDuration, selectedHero, selectedWeapon);
            }
        });

        table.add(startGameButton).colspan(2).pad(10);

        backButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
        table.add(backButton).colspan(2).pad(10);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
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
