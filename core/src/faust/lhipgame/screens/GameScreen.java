package faust.lhipgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.*;
import faust.lhipgame.LHIPGame;
import faust.lhipgame.instances.PlayerInstance;
import faust.lhipgame.rooms.RoomsManager;
import faust.lhipgame.text.TextManager;
import faust.lhipgame.world.WorldManager;

public class GameScreen implements Screen {

    private WorldManager worldManager;
    private PlayerInstance player;
    private TextManager textManager;
    private RoomsManager roomsManager;

    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private float stateTime = 0f;
    private Viewport viewport;
    private ShapeRenderer background;
    private static final Color back = new Color(0x595959ff);

    private final LHIPGame game;

    public GameScreen(LHIPGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Box2D.init();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LHIPGame.GAME_WIDTH, LHIPGame.GAME_HEIGHT);
        viewport = new FillViewport(LHIPGame.GAME_WIDTH, LHIPGame.GAME_HEIGHT, camera);

        // Creating player and making it available to input processor
        player = new PlayerInstance();

        worldManager = new WorldManager();
        textManager = new TextManager();
        roomsManager = new RoomsManager(worldManager,textManager,player,camera);

        box2DDebugRenderer = new Box2DDebugRenderer();

        worldManager.insertPlayerIntoWorld(player, LHIPGame.GAME_WIDTH / 2, LHIPGame.GAME_HEIGHT / 2);

        background = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {

        doLogic();
        worldManager.doStep();
        stateTime += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        //Draw gray background
        drawBackGround();

        //Draw all instances
        drawGameInstances(stateTime);

        //Draw all overlays
        drawOverlays();

     //   box2DDebugRenderer.render(worldManager.getWorld(), camera.combined);

    }

    private void drawOverlays() {
        game.getBatch().begin();
        textManager.renderTextBoxes(game.getBatch(), player);
        game.getBatch().end();
    }

    private void drawGameInstances(float stateTime) {
        game.getBatch().begin();
        roomsManager.drawCurrentRoomContents(game.getBatch(), stateTime);
        game.getBatch().end();
    }

    /**
     * Draws the background
     */
    private void drawBackGround() {
        game.getBatch().begin();
        background.setColor(back);
        background.setProjectionMatrix(camera.combined);
        background.begin(ShapeRenderer.ShapeType.Filled);
        background.rect(0, 0, LHIPGame.GAME_WIDTH, LHIPGame.GAME_HEIGHT);
        background.end();
        roomsManager.drawCurrentRoomBackground();
        game.getBatch().end();
    }

    /**
     * Executes the logic of each game Instance
     */
    private void doLogic() {

        roomsManager.doLogic();
        // for each enemies -> logic()
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        player.dispose();
        roomsManager.dispose();
        worldManager.dispose();
        textManager.dispose();
        box2DDebugRenderer.dispose();
    }
}
