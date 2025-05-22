package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.HintMenuController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Enums.AbilityType;
import io.github.parisajalali96.Models.Enums.Hero;
import io.github.parisajalali96.Models.Enums.WeaponType;

import java.util.function.BiConsumer;


public class HintMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private final TextButton exitButton;
    private final Label heroInfoLabel;
    private final Label weaponInfoLabel;
    private final Label abilityInfoLabel;
    private final Label cheatCodesInfoLabel;
    private final HintMenuController controller;
    private final TextButton windowCloseButton;

    private ScrollPane scrollPane;
    private Table scrollContent;


    public HintMenu(HintMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        exitButton = new TextButton("Exit", skin);
        heroInfoLabel = new Label("Heroes", skin, "title");
        weaponInfoLabel = new Label("Weapons", skin, "title");
        abilityInfoLabel = new Label("Abilities", skin, "title");
        cheatCodesInfoLabel = new Label("Cheat Codes", skin, "title");
        windowCloseButton = new TextButton("Close", skin);

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Table buttonTable = new Table();
        buttonTable.add(heroInfoLabel).pad(10).row();
        buttonTable.add(weaponInfoLabel).pad(10).row();
        buttonTable.add(abilityInfoLabel).pad(10).row();
        buttonTable.add(cheatCodesInfoLabel).pad(10).row();
        buttonTable.add(exitButton).pad(10);

        scrollContent = new Table();
        scrollContent.top().left();
        scrollContent.pad(10);

        scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setForceScroll(true, false);


        table.add(buttonTable).top().padTop(20).row();
        Window infoWindow = new Window("Information", skin);
        infoWindow.setMovable(false);
        infoWindow.setResizable(false);
        infoWindow.setVisible(false);
        Table headerTable = new Table();
        headerTable.add().expandX();
        headerTable.add(windowCloseButton).padTop(5);
        headerTable.add().expandX();
        infoWindow.add(headerTable).colspan(2).expandX().top().row();

        windowCloseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                infoWindow.setVisible(false);
            }
        });

        infoWindow.setSize(Gdx.graphics.getWidth(), 400);
        infoWindow.setPosition(
            (Gdx.graphics.getWidth() - infoWindow.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - infoWindow.getHeight()) / 2f
        );

        infoWindow.add(scrollPane).expand().fill().pad(10);

        stage.addActor(infoWindow);

        exitButton.addListener(event -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(), skin));
            }
            return false;
        });

        heroInfoLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                infoWindow.setVisible(true);
                setHeroInfo();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                heroInfoLabel.setColor(1f, 0.2f, 0.6f, 1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                heroInfoLabel.setColor(Color.WHITE);
            }
        });

        weaponInfoLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                infoWindow.setVisible(true);
                setWeaponInfo();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                weaponInfoLabel.setColor(1f, 0.2f, 0.6f, 1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                weaponInfoLabel.setColor(Color.WHITE);
            }
        });

        abilityInfoLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                infoWindow.setVisible(true);
                setAbilityInfo();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                abilityInfoLabel.setColor(1f, 0.2f, 0.6f, 1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                abilityInfoLabel.setColor(Color.WHITE);
            }
        });

        cheatCodesInfoLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                infoWindow.setVisible(true);
                setCheatCodesInfo();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                cheatCodesInfoLabel.setColor(1f, 0.2f, 0.6f, 1f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                cheatCodesInfoLabel.setColor(Color.WHITE);
            }
        });

    }

    private void setCheatCodesInfo() {
        scrollContent.clear();

        BiConsumer<String, String> addCheatCode = (title, info) -> {
            Label titleLabel = new Label(title, skin, "subtitle");
            titleLabel.setColor(1f, 0.2f, 0.6f, 1f);
            Label infoLabel = new Label(info, skin);

            Table cheatTable = new Table();
            cheatTable.add(titleLabel).padBottom(5).row();
            cheatTable.add(infoLabel);

            scrollContent.add(cheatTable).pad(20).left();
        };

        addCheatCode.accept("TIMEMACHINE", "Blink and you’ll miss it — 60 seconds skipped.");
        addCheatCode.accept("LEVELBOOST", "Power surge engaged. Game on!");
        addCheatCode.accept("CPR", "Defy death, one heal at a time.");
        addCheatCode.accept("BIGBOSS", "Ready or not, here comes the boss.");
        addCheatCode.accept("STORMRIDER", "Catch me if you can.");

    }


    private void setHeroInfo(){
        scrollContent.clear();
        for(Hero hero : Hero.values()){
            Image heroImage = new Image(new TextureRegionDrawable(hero.getTexture()));
            Label heroInfo = new Label(hero.getDescription(), skin);
            scrollContent.add(heroImage).size(200, 200).padRight(20);
            scrollContent.add(heroInfo).width(400).padRight(20);
            scrollContent.padBottom(30);
        }

    }

    private void setWeaponInfo(){
        scrollContent.clear();
        for(WeaponType weaponType : WeaponType.values()){
            Image weaponImage = new Image(new TextureRegionDrawable(weaponType.getTexture()));
            Label weaponInfo = new Label(weaponType.getDescription(), skin);
            scrollContent.add(weaponImage).size(200, 200).padRight(20);
            scrollContent.add(weaponInfo).width(400).padRight(20);
            scrollContent.padBottom(30);
        }
    }

    private void setAbilityInfo(){
        scrollContent.clear();
        for(AbilityType abilityType : AbilityType.values()){
            Image abilityImage = new Image(new TextureRegionDrawable(abilityType.getIcon()));
            Label abilityInfo = new Label(abilityType.getDescription(), skin);
            scrollContent.add(abilityImage).size(200, 200).padRight(20);
            scrollContent.add(abilityInfo).width(400).padRight(20);
            scrollContent.padBottom(30);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.125f, 0.102f, 0.141f, 1f);
        Main.getBatch().begin();
        Main.getBatch().end();
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
