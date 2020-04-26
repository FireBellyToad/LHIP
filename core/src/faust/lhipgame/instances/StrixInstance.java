package faust.lhipgame.instances;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import faust.lhipgame.gameentities.LivingEntity;
import faust.lhipgame.gameentities.StrixEntity;
import faust.lhipgame.gameentities.enums.Direction;
import faust.lhipgame.gameentities.enums.GameBehavior;

import java.util.Objects;

public class StrixInstance extends LivingInstance {

    private static final float STRIX_SPEED = 25;

    private PlayerInstance target;

    public StrixInstance(float x, float y, PlayerInstance target) {
        super(new StrixEntity());
        currentDirection = Direction.DOWN;
        this.startX = x;
        this.startY = y;
        this.target = target;
    }

    @Override
    public void doLogic() {

        if(target.getBody().getPosition().dst(getBody().getPosition()) > 2)
            if (getBody().getPosition().x >= target.getBody().getPosition().x)
                currentDirection = Direction.LEFT;
            else
                currentDirection = Direction.RIGHT;

        if (target.getBody().getPosition().dst(getBody().getPosition()) <= LINE_OF_SIGHT) {
            currentBehavior = GameBehavior.WALK;
            // Normal from strix position to target
            Vector2 direction = new Vector2(target.getBody().getPosition().x - body.getPosition().x,
                    target.getBody().getPosition().y - body.getPosition().y).nor();

            // Move towards target
            body.setLinearVelocity(STRIX_SPEED * direction.x, STRIX_SPEED * direction.y);
        } else {
            currentBehavior = GameBehavior.IDLE;
            body.setLinearVelocity(0, 0);
        }
    }

    @Override
    public void createBody(World world, float x, float y) {

        Objects.requireNonNull(world);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);

        // Define shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4, 2);

        // Define Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        // Associate body to world
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    /**
     * Draw the Entity frames using Body position
     *
     * @param batch
     * @param stateTime
     */
    public void draw(final SpriteBatch batch, float stateTime) {
        Objects.requireNonNull(batch);

        TextureRegion frame = ((LivingEntity) entity).getFrame(currentBehavior, currentDirection, stateTime);

        //Draw Strix
        batch.draw(frame, body.getPosition().x - POSITION_OFFSET, body.getPosition().y - POSITION_Y_OFFSET);

    }
}