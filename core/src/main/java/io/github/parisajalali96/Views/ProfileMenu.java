package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Player;

public class ProfileMenu implements PopupMenu{
    private final Player player;
    public ProfileMenu(Player player) {
        this.player = player;
    }
    @Override
    public Window build(Stage stage, Skin skin) {
        Window window = new Window("Profile", skin);
        window.setSize(400, 350);
        window.setModal(true);
        window.setMovable(false);
        window.setPosition(
            (Gdx.graphics.getWidth() - window.getWidth()) / 2,
            (Gdx.graphics.getHeight() - window.getHeight()) / 2
        );
        TextButton exitButton= new TextButton("Exit", skin);
        Label volumeLabel = new Label("Volume:", skin);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(5); // get current volume
        Label volumeValueLabel = new Label(String.format("%.0f%%", volumeSlider.getValue() * 100), skin);
        window.addActor(volumeLabel);
        window.addActor(volumeSlider);
        window.addActor(volumeValueLabel);
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                volumeValueLabel.setText(String.format("%.0f%%", volume * 100));
                //GameAssetManager.getGameAssetManager().setMusicVolume(volume);
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });
        window.add(exitButton);
        return window;
    }
}
