package faust.lhipgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import faust.lhipgame.LHIPGame;
import faust.lhipgame.game.music.MusicManager;
import faust.lhipgame.game.music.enums.TuneEnum;
import faust.lhipgame.menu.enums.MenuItem;
import faust.lhipgame.saves.SaveFileManager;

import java.util.Objects;

/**
 * Menu class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class Menu implements InputProcessor {

    private static final float FONT_SIZE = 0.5f;
    private static final float MENU_X_OFFSET = 50;
    private static final float MENU_Y_OFFSET = (float) (LHIPGame.GAME_HEIGHT * 0.5);
    private static final float SPAN = 15;
    private static final String ARROW_CHARACTER = "~";

    private BitmapFont mainFont;

    private MenuItem currentMenu = MenuItem.MAIN;
    private int selectedMenuVoice = 0;
    private boolean changeToGameScreen = false;
    private boolean changeToMainScreen = false;
    private MusicManager musicManager;

    private final SaveFileManager saveFileManager;

    public Menu(SaveFileManager saveFileManager, MusicManager musicManager) {
        this.saveFileManager = saveFileManager;
        this.musicManager = musicManager;
    }

    public Menu(SaveFileManager saveFileManager, MenuItem currentMenu) {
        this.saveFileManager = saveFileManager;
        this.currentMenu = currentMenu;
    }

    public void loadFonts(AssetManager assetManager) {

        // Prepare font
        mainFont = assetManager.get("fonts/main_font.fnt");
        mainFont.getData().setScale(FONT_SIZE);
    }

    public void drawCurrentMenu(SpriteBatch batch) {
        Objects.requireNonNull(batch);
        Objects.requireNonNull(currentMenu);
        Objects.requireNonNull(currentMenu.getSubItems()); // Must have subitems!

        //Draw arrow on selected option
        if (Objects.nonNull(currentMenu.getTitle()))
            mainFont.draw(batch, currentMenu.getTitle(), MENU_X_OFFSET, MENU_Y_OFFSET);

        MenuItem[] subItemsArray = currentMenu.getSubItems();
        for (int i = 0; i < subItemsArray.length; i++) {
            //TODO internazionalizzare
            mainFont.draw(batch, subItemsArray[i].name().replace('_', ' '),
                    MENU_X_OFFSET, MENU_Y_OFFSET - (SPAN * (1 + i)));
        }
        //Draw arrow on selected option
        mainFont.draw(batch, ARROW_CHARACTER, MENU_X_OFFSET - 10, MENU_Y_OFFSET - (SPAN * (1 + this.selectedMenuVoice)));
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.UP: {
                if (selectedMenuVoice > 0) {
                    selectedMenuVoice--;
                }
                break;
            }
            case Input.Keys.S:
            case Input.Keys.DOWN: {
                if (selectedMenuVoice < currentMenu.getSubItems().length - 1) {
                    selectedMenuVoice++;
                }
                break;
            }
            case Input.Keys.X:
            case Input.Keys.K:
            case Input.Keys.ENTER: {
                handleSelection();
                break;
            }
        }
        return true;
    }

    /**
     * Handles selection event
     */
    private void handleSelection() {
        switch (currentMenu) {
            case NEW_GAME:
                handleNewGame();
                break;
            case MAIN:
                handleMain();
                break;
            case GAME_OVER:
                handleGameOver();
                break;
            case PLAY_GAME:
                handlePlayGame();
                break;
        }
    }

    private void handlePlayGame() {
        switch (selectedMenuVoice) {
            case 0: {
                //New game
                selectedMenuVoice = 0;
                currentMenu = MenuItem.NEW_GAME;
                break;
            }
            case 1: {
                //Load game
                changeToGameScreen = true;
                break;
            }
            case 2: {
                //Back
                selectedMenuVoice = 0;
                currentMenu = MenuItem.MAIN;
                break;
            }
        }
    }

    private void handleMain() {
        switch (selectedMenuVoice) {
            case 0: {
                //New game
                selectedMenuVoice = 0;
                currentMenu = MenuItem.PLAY_GAME;
                break;
            }
            case 1: {
                //TODO credit
                break;
            }
            case 3: {
                //Exit game
                Gdx.app.exit();
                break;
            }
        }
    }

    private void handleNewGame() {
        switch (selectedMenuVoice) {
            case 0: {
                //Yes i'm sure
                saveFileManager.cleanSaveFile();
                changeToGameScreen = true;
                break;
            }
            case 1: {
                //No, back to main
                currentMenu = MenuItem.MAIN;
                selectedMenuVoice = 0;
                break;
            }
        }
    }

    private void handleGameOver() {
        switch (selectedMenuVoice) {
            case 0: {
                //Yes, continue last save
                changeToGameScreen = true;
                break;
            }
            case 1: {
                //No, back to main
                changeToMainScreen = true;
                break;
            }
        }
    }

    public boolean isChangeToGameScreen() {
        return changeToGameScreen;
    }

    public boolean isChangeToMainScreen() {
        return changeToMainScreen;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
