package faust.lhipgame.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import faust.lhipgame.LHIPGame;
import faust.lhipgame.gameentities.enums.DecorationsEnum;
import faust.lhipgame.gameentities.enums.POIEnum;
import faust.lhipgame.instances.DecorationInstance;
import faust.lhipgame.instances.POIInstance;
import faust.lhipgame.instances.PlayerInstance;
import faust.lhipgame.rooms.enums.MapLayersEnum;
import faust.lhipgame.rooms.enums.MapObjNameEnum;
import faust.lhipgame.rooms.enums.RoomType;
import faust.lhipgame.text.TextManager;
import faust.lhipgame.world.WorldManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract room common logic
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public abstract class AbstractRoom {

    /**
     * Boundaries for room changing
     */
    public static final float LEFT_BOUNDARY = 0;
    public static final float BOTTOM_BOUNDARY = 0;
    public static final float RIGHT_BOUNDARY = LHIPGame.GAME_WIDTH - 16;
    public static final float TOP_BOUNDARY = LHIPGame.GAME_HEIGHT - 16;

    protected TiledMap tiledMap;
    protected TiledMapRenderer tiledMapRenderer;
    protected MapObjects mapObjects;

    protected List<POIInstance> poiList;
    protected List<DecorationInstance> decorationList;
    protected PlayerInstance player;
    protected RoomType roomType;
    protected String roomFileName;

    /**
     * Constructor without additional loader argouments
     * @param roomType
     * @param worldManager
     * @param textManager
     * @param player
     * @param camera
     */
    public AbstractRoom(final RoomType roomType, final WorldManager worldManager, final TextManager textManager, final PlayerInstance player, final OrthographicCamera camera){
        this(roomType, worldManager, textManager, player, camera,null);
    }

    /**
     * Constructor
     *
     * @param roomType
     * @param worldManager
     * @param textManager
     * @param player
     * @param camera
     * @param additionalLoadArgs
     */
    public AbstractRoom(final RoomType roomType, final WorldManager worldManager, final TextManager textManager, final PlayerInstance player, final OrthographicCamera camera, Object...additionalLoadArgs ) {
        Objects.requireNonNull(worldManager);
        Objects.requireNonNull(textManager);
        Objects.requireNonNull(player);
        Objects.requireNonNull(roomType);

        // Load tiled map by name
        this.roomType = roomType;
        this.roomFileName = "terrains/"+roomType.getMapFileName();
        loadTiledMap(additionalLoadArgs);

        // Extract mapObjects
        mapObjects = tiledMap.getLayers().get(MapLayersEnum.OBJECT_LAYER.ordinal()).getObjects();

        // Set camera for rendering
        tiledMapRenderer.setView(camera);

        // Add content to room
        this.player = player;
        poiList = new ArrayList<>();
        decorationList = new ArrayList<>();

        // Place objects in room
        for (MapObject obj : mapObjects) {

            // Prepare POI
            if (MapObjNameEnum.POI.name().equals(obj.getName())) {
                addObjAsPOI(obj,textManager);
            }

            // Prepare decoration
            if (MapObjNameEnum.DECO.name().equals(obj.getName())) {
                addObjAsDecoration(obj);
            }

        }

        worldManager.clearBodies();
        worldManager.insertPlayerIntoWorld(player,player.getStartX(),player.getStartY());
        worldManager.insertPOIIntoWorld(poiList);
        worldManager.insertDecorationsIntoWorld(decorationList);
        player.changePOIList(poiList);

        // Do other stuff
        this.initRoom(roomType, worldManager, textManager, player, camera);
    }

    /**
     * Implements tiled map load
     * @param additionalLoadArguments if needed
     */
    protected abstract void loadTiledMap(Object[] additionalLoadArguments);

    /**
     * Add a object as POI
     *
     * @param obj
     * @param textManager
     */
    protected void addObjAsPOI(MapObject obj,TextManager textManager){

        POIEnum poiType = POIEnum.getFromString((String) obj.getProperties().get("type"));
        Objects.requireNonNull(poiType);

        poiList.add(new POIInstance(textManager,
                (float) obj.getProperties().get("x"),
                (float) obj.getProperties().get("y"),
                poiType));
    };

    /**
     * Add a object as Decoration
     *
     * @param obj MapObject to add
     */
    protected void addObjAsDecoration(MapObject obj){

        DecorationsEnum decoType = DecorationsEnum.getFromString((String) obj.getProperties().get("type"));
        Objects.requireNonNull(decoType);

        decorationList.add(new DecorationInstance(
                (float) obj.getProperties().get("x"),
                (float) obj.getProperties().get("y"),
                decoType));
    }

    /**
     * Method for additional room initialization
     *
     * @param roomType
     * @param worldManager
     * @param textManager
     * @param player
     * @param camera
     */
    protected abstract void initRoom(RoomType roomType, final WorldManager worldManager, TextManager textManager, PlayerInstance player, OrthographicCamera camera);

    /**
     * Draws room background
     */
    public void drawRoomBackground() {
        tiledMapRenderer.render();
    }

    /**
     * Draws room contents
     *
     * @param batch
     * @param stateTime
     */
    public void drawRoomContents(final SpriteBatch batch, float stateTime) {

        //
        poiList.forEach((poi) -> poi.draw(batch, stateTime));

        // All decorations behind player
        decorationList.forEach((deco) -> {
            if (deco.getBody().getPosition().y >= player.getBody().getPosition().y -2 || deco.getInteracted())
                deco.draw(batch, stateTime);
        });

        player.draw(batch, stateTime);

        // All decorations in front of player
        decorationList.forEach((deco) -> {
            if (deco.getBody().getPosition().y < player.getBody().getPosition().y -2 && !deco.getInteracted())
                deco.draw(batch, stateTime);
        });

    }

    /**
     * Disposes the terrain and the contents of the room
     */
    public void dispose() {
        tiledMap.dispose();
        decorationList.forEach((deco) -> deco.dispose());
        poiList.forEach((poi) -> poi.dispose());
    }

    public RoomType getRoomType() {
        return roomType;
    }
}
