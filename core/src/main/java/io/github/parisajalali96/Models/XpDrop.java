package io.github.parisajalali96.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.parisajalali96.Models.Enums.WeaponType;

public class XpDrop {
    private Vector2 dropLocation;
    private final int xp = 3;
    private final TextureRegion textureRegion = new TextureRegion(WeaponType.getBulletTexture());

    private static final float PICKUP_RADIUS = 20f;
    private static final float ATTRACTION_RADIUS = 150f;
    private static final float ATTRACTION_SPEED = 300;

    public XpDrop(Vector2 dropLocation) {
        this.dropLocation = dropLocation;
    }
    public void addXp(){
        Game.getCurrentPlayer().addXp(xp);
    }

    public void update(float deltaTime) {
        Player player = Game.getCurrentPlayer();
        Vector2 playerPos = player.getPosition();

        float distance = playerPos.dst(dropLocation);
        if (distance < ATTRACTION_RADIUS && distance > PICKUP_RADIUS) {
            Vector2 direction = new Vector2(playerPos).sub(dropLocation).nor();
            dropLocation.mulAdd(direction, ATTRACTION_SPEED * deltaTime);
        }
    }
    public void draw(SpriteBatch batch) {
        batch.draw(textureRegion, dropLocation.x, dropLocation.y);
    }

    public Vector2 getPosition() {
        return dropLocation;
    }

    public boolean isPickedUpBy(Player player) {
        return player.getPosition().dst(dropLocation) < PICKUP_RADIUS;
    }

}
