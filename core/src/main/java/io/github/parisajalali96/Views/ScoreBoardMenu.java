package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.parisajalali96.Models.Player;

public class ScoreBoardMenu implements PopupMenu{
    private final Player player;
    public ScoreBoardMenu(Player player) {
        this.player = player;
    }

    @Override
    public Window build(Stage stage, Skin skin) {
        Window window = new Window("ScoreBoard", skin);
        window.setSize(400, 350);
        window.setModal(true);
        window.setMovable(false);
        window.setPosition(
            (Gdx.graphics.getWidth() - window.getWidth()) / 2,
            (Gdx.graphics.getHeight() - window.getHeight()) / 2
        );
        TextButton exitButton= new TextButton("Exit", skin);
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
