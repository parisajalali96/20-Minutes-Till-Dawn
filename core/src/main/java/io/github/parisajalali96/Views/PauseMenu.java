package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.GameController;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.PauseMenuController;
import io.github.parisajalali96.Controllers.SaveGameManager;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Enums.AbilityType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;

import java.io.IOException;
import java.util.function.BiConsumer;

public class PauseMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private final Label menuTitle;
    private final Label resumeLabel;
    private final Label showCheatCodesLabel;
    private final Label abilitiesLabel;
    private final Label giveUpLabel;
    private final Label saveAndQuitLabel;
    private Window pauseMenuWindow;
    private PauseMenuController controller;
    private Window scrollPaneWindow;
    private Table scrollContent;
    private ScrollPane scrollPane;

    public PauseMenu(PauseMenuController controller, Skin skin) {
        this.skin = skin;
        this.controller = controller;
        menuTitle = new Label("Pause Menu", skin);
        resumeLabel = new Label("Resume Game", skin, "subtitle");
        showCheatCodesLabel = new Label("Cheat Codes", skin, "subtitle");
        abilitiesLabel = new Label("Abilities", skin, "subtitle");
        saveAndQuitLabel = new Label("Save & Quit", skin, "subtitle");
        giveUpLabel = new Label("Give Up", skin, "subtitle");
        scrollContent = new Table();
        scrollPane = new ScrollPane(scrollContent);


    }


    private void setAbilitiesScrollPane() {
        scrollContent.clear();
        scrollContent.top();
        for (AbilityType abilityType : Game.getCurrentPlayer().getAbilities()) {
            Image abilityImage = new Image(new TextureRegionDrawable(abilityType.getIcon()));
            Label abilityInfo = new Label(abilityType.getDescription(), skin);

            Table abilityRow = new Table();
            abilityRow.add(abilityImage).size(64).padRight(10);
            abilityRow.add(abilityInfo).width(400).expandX().fillX().left();

            scrollContent.add(abilityRow).padBottom(20).left().row();
        }
        scrollPane.setVisible(true);
        scrollContent.pack();
    }

    private void setShowCheatCodesScrollPane() {
        scrollContent.clear();
        scrollContent.top().left();

        BiConsumer<String, String> addCheatCode = (title, info) -> {
            Label titleLabel = new Label(title, skin, "subtitle");
            titleLabel.setColor(1f, 0.2f, 0.6f, 1f);
            Label infoLabel = new Label(info, skin);

            Table cheatTable = new Table();
            cheatTable.left();
            cheatTable.add(titleLabel).padBottom(5).center().row();
            cheatTable.add(infoLabel).center();

            scrollContent.add(cheatTable).pad(20).center().row();
        };

        addCheatCode.accept("TIMEMACHINE", "Blink and you’ll miss it — 60 seconds skipped.");
        addCheatCode.accept("LEVELBOOST", "Power surge engaged. Game on!");
        addCheatCode.accept("CPR", "Defy death, one heal at a time.");
        addCheatCode.accept("BIGBOSS", "Ready or not, here comes the boss.");
        addCheatCode.accept("STORMRIDER", "Catch me if you can.");

        scrollPane.setVisible(true);
        scrollContent.pack();
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        pauseMenuWindow = new Window("Game Paused", skin);
        pauseMenuWindow.setModal(true);
        pauseMenuWindow.setMovable(false);
        pauseMenuWindow.setSize(600, 600);
        pauseMenuWindow.setPosition(
            (Gdx.graphics.getWidth() - pauseMenuWindow.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - pauseMenuWindow.getHeight()) / 2f
        );

        pauseMenuWindow.defaults().pad(10).width(400);
        pauseMenuWindow.add(resumeLabel).row();
        pauseMenuWindow.add(showCheatCodesLabel).row();
        pauseMenuWindow.add(abilitiesLabel).row();
        pauseMenuWindow.add(saveAndQuitLabel).row();
        pauseMenuWindow.add(giveUpLabel).row();

        scrollContent = new Table();
        scrollContent.top().left();

        scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        scrollPaneWindow = new Window("", skin);
        scrollPaneWindow.setVisible(false);
        scrollPaneWindow.setMovable(true);
        scrollPaneWindow.setSize(600, 600);
        scrollPaneWindow.setPosition(
            (Gdx.graphics.getWidth() - scrollPaneWindow.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - scrollPaneWindow.getHeight()) / 2f
        );

        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scrollPaneWindow.setVisible(false);
            }
        });
        Table titleBar = scrollPaneWindow.getTitleTable();
        titleBar.clear();

        Label title = new Label("Info", skin, "title");
        title.setAlignment(Align.left);

        titleBar.add(title).expandX().left().padLeft(10);
        titleBar.add(closeButton).right().padRight(10);


        scrollPaneWindow.add(scrollPane).expand().fill().pad(10);

        stage.addActor(pauseMenuWindow);
        stage.addActor(scrollPaneWindow);

        resumeLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Game.getGameView().setPaused(false);
                Main.getMain().setScreen(Game.getGameView());
            }
        });

        showCheatCodesLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setShowCheatCodesScrollPane();
                scrollPaneWindow.setVisible(true);
                scrollPaneWindow.toFront();
            }
        });

        abilitiesLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setAbilitiesScrollPane();
                scrollPaneWindow.setVisible(true);
                scrollPaneWindow.toFront();
            }
        });

        saveAndQuitLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                scrollContent.clear();
                scrollContent.setFillParent(true);
                scrollPaneWindow.setVisible(true);
                scrollPaneWindow.toFront();

                Label nameGame = new Label("Pick a name for this game:", skin);
                nameGame.setAlignment(Align.center);

                TextField gameName = new TextField("", skin);
                gameName.setMessageText("Enter Game Name");
                gameName.setAlignment(Align.center);

                TextButton saveButton = new TextButton("Save and Quit", skin);
                scrollContent.add(nameGame).padBottom(10).center().expandX().row();
                scrollContent.add(gameName).width(300).padBottom(20).center().expandX().row();
                scrollContent.add(saveButton).center().expandX().row();

                saveButton.addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        String name = gameName.getText().trim();
                        if (!name.isEmpty()) {
                            SaveGameManager.saveGame(Game.getCurrentPlayer(), Game.getMap(), name);
                            Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                                GameAssetManager.getGameAssetManager().getSkin()));
                        } else {
                            gameName.setMessageText("Please enter a valid name!");
                        }
                    }
                });
            }
        });


        giveUpLabel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Game.getGameView().getController().endGame(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });

        for (Label label : new Label[]{resumeLabel, showCheatCodesLabel, abilitiesLabel, saveAndQuitLabel, giveUpLabel}) {
            label.addListener(new ClickListener() {
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    label.setColor(1f, 0.2f, 0.6f, 1f);
                }

                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    label.setColor(Color.WHITE);
                }
            });
        }
    }


    @Override
    public void render(float delta) {
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
