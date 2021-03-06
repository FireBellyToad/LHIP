package faust.lhipgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import faust.lhipgame.game.music.MusicManager;
import faust.lhipgame.saves.SaveFileManager;
import faust.lhipgame.screens.CameraManager;
import faust.lhipgame.screens.FBTScreen;

public class LHIPGame extends Game {

    public static final int GAME_WIDTH = 160;
    public static final int GAME_HEIGHT = 144;

    private SpriteBatch batch;
    private AssetManager assetManager;
    private CameraManager cameraManager;
    private SaveFileManager saveFileManager;
    private MusicManager musicManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        cameraManager = new CameraManager();
        saveFileManager = new SaveFileManager();
        musicManager = new MusicManager();

        assetManager.load("splash/fbt_splash.png", Texture.class);
        assetManager.finishLoading();

        setScreen(new FBTScreen(this));
    }

    @Override
    public void dispose() {

        getScreen().dispose();
        assetManager.dispose();
        cameraManager.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public SaveFileManager getSaveFileManager() { return saveFileManager;}

    public MusicManager getMusicManager() {
        return musicManager;
    }
}
