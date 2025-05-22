package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.parisajalali96.Controllers.MainMenuController;
import io.github.parisajalali96.Controllers.SettingsMenuController;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.KeyControl;
import io.github.parisajalali96.Models.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class SettingsMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private final Player player = Game.getCurrentPlayer();
    private final Label menuTitle;
    private final Label volumeLabel;
    private final Slider volumeSlider;
    private final Label volumeValueLabel;
    private final CheckBox sfxCheckBox;
    private final CheckBox autoReloadCheckBox;
    private final Label musicLabel;
    private final SelectBox<String> musicSelector;
    private Table table;
    private final TextButton exitButton;
    private final TextButton changeGameControlls;
    private final SettingsMenuController controller;

    //change game control buttons
    private Window gameControlChangeWindow;
    private ScrollPane gameControlChangeScrollPane;
    private Map<String, TextField> keyFields = new LinkedHashMap<>();



    public SettingsMenu(SettingsMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        menuTitle = new Label("Settings", skin);
        volumeLabel = new Label("Volume:", skin);
        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeValueLabel = new Label("", skin);
        musicLabel = new Label("Background Music:", skin);
        sfxCheckBox = new CheckBox("Enable SFX", skin);
        autoReloadCheckBox = new CheckBox("Enable Auto Reload", skin);
        musicSelector = new SelectBox<>(skin);
        exitButton = new TextButton("Exit", skin);
        changeGameControlls = new TextButton("Game Controls", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        volumeSlider.setValue(GameAssetManager.getGameAssetManager().getMusicVolume());
        volumeValueLabel.setText(String.format("%.0f%%", volumeSlider.getValue() * 100));
        sfxCheckBox.setChecked(GameAssetManager.getGameAssetManager().isSfxEnabled());
        autoReloadCheckBox.setChecked(Game.isAutoReloadActive());
        musicSelector.setItems("Music 1", "Music 2", "Music 3");
        musicSelector.setSelected(GameAssetManager.getGameAssetManager().getMusic());

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                volumeValueLabel.setText(String.format("%.0f%%", volume * 100));
                GameAssetManager.getGameAssetManager().setMusicVolume(volume);
            }
        });

        sfxCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAssetManager.getGameAssetManager().setSfxEnabled(sfxCheckBox.isChecked());
            }
        });

        autoReloadCheckBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Game.setAutoReloadActive(autoReloadCheckBox.isChecked());
            }
        });

        musicSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAssetManager.getGameAssetManager().setMusic(musicSelector.getSelected());
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenu(new MainMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()));
            }
        });
        changeGameControlls.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createGameControlChangeWindow();
            }
        });

        table = new Table();
        table.defaults().pad(10);
        table.align(com.badlogic.gdx.utils.Align.center);

        table.add(menuTitle).colspan(3).center().padBottom(20);
        table.row();
        table.add(volumeLabel).left();
        table.add(volumeSlider).width(250);
        table.add(volumeValueLabel).width(50);
        table.row();
        table.add(sfxCheckBox).colspan(3).left();
        table.row();
        table.add(autoReloadCheckBox).colspan(3).left();
        table.row();
        table.add(musicLabel).left();
        table.add(musicSelector).colspan(2).left();
        table.row();
        table.add(exitButton).colspan(3).center().padTop(20);
        table.row();
        table.add(changeGameControlls).colspan(3).center().padTop(20);


        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(table);

        stage.addActor(root);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.125f, 0.102f, 0.141f, 1f);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void createGameControlChangeWindow() {
        gameControlChangeWindow = new Window("Game Control Window", skin);
        gameControlChangeWindow.setMovable(true);
        gameControlChangeWindow.setResizable(true);
        gameControlChangeWindow.setSize(500, 400);
        gameControlChangeWindow.center();

        Table keyTable = new Table();
        keyTable.top().left();

        keyFields.clear();

        for (Map.Entry<String, Integer> entry : KeyControl.getKeyControl().entrySet()) {
            Label keyName = new Label(entry.getKey(), skin);
            String keyCharacter = Input.Keys.toString(entry.getValue());
            TextField keyValue = new TextField(keyCharacter, skin);
            keyFields.put(entry.getKey(), keyValue);
            keyTable.add(keyName).left().pad(5);
            keyTable.add(keyValue).width(100).pad(5).row();
        }

        gameControlChangeScrollPane = new ScrollPane(keyTable, skin);
        gameControlChangeScrollPane.setFadeScrollBars(false);
        gameControlChangeScrollPane.setScrollingDisabled(true, false);

        gameControlChangeWindow.add(gameControlChangeScrollPane).expand().fill().row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Map.Entry<String, TextField> entry : keyFields.entrySet()) {
                    String action = entry.getKey();
                    String newKeyString = entry.getValue().getText().trim();

                    try {
                        changeKeyControl(action, newKeyString);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid key for " + action + ": " + newKeyString);
                    }
                }
                gameControlChangeWindow.remove();
            }
        });

        gameControlChangeWindow.add(closeButton).padTop(10).center().row();
        gameControlChangeWindow.setSize(600, 600);
        float x = (Gdx.graphics.getWidth() - gameControlChangeWindow.getWidth()) / 2f;
        float y = (Gdx.graphics.getHeight() - gameControlChangeWindow.getHeight()) / 2f;
        gameControlChangeWindow.setPosition(x, y);
        stage.addActor(gameControlChangeWindow);
    }


    private void changeKeyControl(String selectedKey, String newKey) {
        KeyControl.changeKey(selectedKey, newKey);
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

