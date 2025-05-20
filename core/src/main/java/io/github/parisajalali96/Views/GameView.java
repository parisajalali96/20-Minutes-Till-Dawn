package io.github.parisajalali96.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameMap;

public class GameView implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private GameMap map;
    private BitmapFont font;

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        map = new GameMap();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.update(delta);
        Game.update(delta);
        Game.getCurrentPlayer().update(delta, this.camera);

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

    }

    private String formatTime(float seconds) {
        int minutes = (int)(seconds / 60);
        int secs = (int)(seconds % 60);
        return String.format("%02d:%02d", minutes, secs);
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
}

