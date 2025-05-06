package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Player;

public class SettingsMenu implements PopupMenu {
    private final Player player;

    public SettingsMenu(Player player) {
        this.player = player;
    }

    @Override
    public Window build(Stage stage, Skin skin) {
        Window window = new Window("Settings", skin);
        window.setSize(700, 600);
        window.setModal(true);
        window.setMovable(false);
        window.setPosition(
            (Gdx.graphics.getWidth() - window.getWidth()) / 2,
            (Gdx.graphics.getHeight() - window.getHeight()) / 2
        );

        //volume slider
        Label volumeLabel = new Label("Volume:", skin);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        Label volumeValueLabel = new Label("", skin);
        volumeSlider.setValue(GameAssetManager.getGameAssetManager().getMusicVolume());
        volumeValueLabel.setText(String.format("%.0f%%", volumeSlider.getValue() * 100));

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                volumeValueLabel.setText(String.format("%.0f%%", volume * 100));
                GameAssetManager.getGameAssetManager().setMusicVolume(volume);
            }
        });

        // sfx checkbox
        CheckBox sfxCheckBox = new CheckBox("Enable SFX", skin);
        sfxCheckBox.setChecked(GameAssetManager.getGameAssetManager().isSfxEnabled());
        sfxCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAssetManager.getGameAssetManager().setSfxEnabled(sfxCheckBox.isChecked());
            }
        });

        // music selector
        Label musicLabel = new Label("Background Music:", skin);
        SelectBox<String> musicSelector = new SelectBox<>(skin);
        musicSelector.setItems("Music 1", "Music 2", "Music 3"); // Replace with actual names
        musicSelector.setSelected(GameAssetManager.getGameAssetManager().getMusic());

        musicSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAssetManager.getGameAssetManager().setMusic(musicSelector.getSelected());
            }
        });


        Table table = new Table();
        table.pad(20).defaults().pad(10).left();
        table.add(volumeLabel);
        table.add(volumeSlider).width(250);
        table.add(volumeValueLabel).width(50);
        table.row();
        table.add(sfxCheckBox).left();
        table.row();
        table.add(musicLabel);
        table.add(musicSelector).colspan(2).left();
        table.row();

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });

        table.add(exitButton).colspan(3).center().padTop(20);

        window.add(table);
        return window;
    }
}

