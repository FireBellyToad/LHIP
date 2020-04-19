package faust.lhipgame.instances;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import faust.lhipgame.gameentities.POIEntity;
import faust.lhipgame.gameentities.enums.POIEnum;
import faust.lhipgame.text.TextManager;

import java.util.Objects;

public class POIInstance extends GameInstance {

    private static final long FLICKER_DURATION_IN_NANO = 125000000; // 1/8 second in nanoseconds

    private boolean enableFlicker = false; // flag for enable flickering
    private boolean mustFlicker = false;// flag that is true when the POI must be hidden
    private long startTime = 0;
    private TextManager textManager;


    public POIInstance(final TextManager textManager, float x, float y, POIEnum poiType) {
        super(new POIEntity(poiType));
        this.textManager = textManager;
        this.startX = x;
        this.startY = y;
    }

    /**
     * Handles the examination from a Player Instance
     */
    public void examine() {
        //TODO inserire logica
        String messageKey = POIEntity.FOUND_ITEM_MESSAGE_KEY;

        if(MathUtils.randomBoolean()){
            messageKey = ((POIEntity) this.entity).getMessageKey();
        }

        textManager.addNewTextBox(messageKey);
    }

    @Override
    public void draw(SpriteBatch batch, float stateTime) {
        Objects.requireNonNull(batch);

        // If flickering is not enabled or the flickering POI must be shown, draw the texture
        if(!this.enableFlicker || !mustFlicker){
            batch.draw(entity.getTexture(), body.getPosition().x- POSITION_OFFSET, body.getPosition().y- POSITION_OFFSET);
        }

        // Every 1/8 seconds alternate between showing and hiding the texture to achieve flickering effect
        if(this.enableFlicker && TimeUtils.timeSinceNanos(startTime) > FLICKER_DURATION_IN_NANO){
            mustFlicker = !mustFlicker;

            // restart flickering timer
            startTime = TimeUtils.nanoTime();
        }

    }

    public void setEnableFlicker(boolean enableFlicker) {
        this.enableFlicker = enableFlicker;
    }
}
