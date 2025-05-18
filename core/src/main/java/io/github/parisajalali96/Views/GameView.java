package io.github.parisajalali96.Views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import io.github.parisajalali96.Models.Enums.EnemyType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Models.GameMap;
import io.github.parisajalali96.Models.Weapon;

public class GameView implements Screen {
    private SpriteBatch batch;
    OrthographicCamera camera;
    private GameMap map;


    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        map = new GameMap();

        // Example: Spawn one Tree enemy at (200, 200)
        map.spawnEnemy(EnemyType.Tree, new Vector2(200, 200));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(Game.getCurrentPlayer().getPosition().x + 32, Game.getCurrentPlayer().getPosition().y + 32, 0);
        camera.update();

        Game.getCurrentPlayer().update(delta, this.camera);
        map.update(delta);

        batch.begin();
        Game.getCurrentPlayer().draw(batch);
        map.draw(batch);
        batch.end();
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
        batch.dispose();
        Game.getCurrentPlayer().dispose();

    }
}
