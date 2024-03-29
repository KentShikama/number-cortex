package com.numbercortex.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.numbercortex.CortexState;
import com.numbercortex.ModeTracker;
import com.numbercortex.ModeTracker.Mode;
import com.numbercortex.Persistence;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;

import facebook.CrossPlatformFacebook;
import libgdx.Game;

public class Launch extends Game {

    static final int SCREEN_WIDTH = 640;
    static final int SCREEN_HEIGHT = 1136;

    static final Color SEA_BLUE = new Color(79f / 255, 120f / 255, 141f / 255, 1);
    static final Color RETRO_RED = new Color(200f / 255, 99f / 255, 91f / 255, 1);
    static final Color BRIGHT_YELLOW = new Color(250f / 255, 235f / 255, 102f / 255, 1);

    private Stage numberBackgroundStage;
    private boolean assigned;
    private FPSLogger fps;

    private SplashScreen splashScreen;
    
    private String appLink;
    private CrossPlatformFacebook facebook;

    public Launch(String appLink, CrossPlatformFacebook facebook) {
        this.appLink = appLink;
        this.facebook = facebook;
    }

    @Override
    public void create() {
        loadAndAssignStartingAssets();
        loadAssets();
        fps = new FPSLogger();
    }
    private void loadAndAssignStartingAssets() {
        Assets.manager = new AssetManager();
        Assets.loadBackground();
        Assets.loadSplash();
        Assets.manager.finishLoading();
        Assets.assignBackgroundScreen();
        Assets.assignSplashScreen();
        numberBackgroundStage = buildBackgroundStage();
        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);
        splashScreen.show();
    }
    private Stage buildBackgroundStage() {
        ExtendViewport extendViewport = new ExtendViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT, (float) (Launch.SCREEN_HEIGHT / 1.2), Launch.SCREEN_HEIGHT);
        Stage backgroundStage = new Stage(extendViewport);
        return backgroundStage;
    }
    private void loadAssets() {
        Assets.loadHome();
        Assets.loadSettings();
        Assets.loadGame();
        Assets.loadLevels();
        Assets.loadDialog();
        Assets.loadAudio();
        Assets.loadFonts();
        assigned = false;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        addNumberBackground();
    }
    private void addNumberBackground() {
        numberBackgroundStage.clear();
        float worldWidth = numberBackgroundStage.getViewport().getWorldWidth();
        Background numberBackground = new Background(Launch.SEA_BLUE, Assets.backgroundTexture, worldWidth);
        numberBackgroundStage.addActor(numberBackground);
    }

    @Override
    public void render() {
        // fps.log();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Assets.manager.update()) {
            assignAssetsAndShowGameIfApplicable();
        }
        if (!(this.getScreen() instanceof PlayScreen)) {
            renderNumberBackground();       
        }
        super.render();
    }
    private void renderNumberBackground() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        numberBackgroundStage.getViewport().update(screenWidth, screenHeight, true);
        numberBackgroundStage.draw();
    }

    private void assignAssetsAndShowGameIfApplicable() {
        if (!assigned) {
            Assets.assignHomeScreen();
            Assets.assignSettingsScreen();
            Assets.assignPlayScreen();
            Assets.assignLevelsScreen();
            Assets.assignDialogScreen();
            Assets.assignAudio();
            Assets.assignFonts();
            showGame();
            splashScreen = null;
            assigned = true;
        }
    }
    private void showGame() {
        Persistence persistence = Persistence.getInstance().load();
        ModeTracker.mode = buildMode(persistence);
        ScreenTracker.initializeScreens(this, appLink, facebook);
        GameScreen screen = buildCurrentScreen(persistence);
        recreateScreenState(persistence, screen);
        if (screen instanceof HomeScreen) {
            HomeScreen homeScreen = (HomeScreen) screen;
            splashScreen.animatedTitleTransition(homeScreen);
        } else {
            ScreenTracker.transitionScreen.splashTransition(screen);
        }
    }
    private Mode buildMode(Persistence persistence) {
        String currentModeString = persistence.getMode();
        return ModeTracker.getMode(currentModeString);
    }
    private GameScreen buildCurrentScreen(Persistence persistence) {
        String currentScreenString = persistence.getCurrentScreen();
        GameScreen screen = ScreenTracker.getScreen(currentScreenString);
        return screen;
    }
    private void recreateScreenState(Persistence persistence, Screen screen) {
        recreateGameIfApplicable(persistence, screen);
        recreateOpeningMusicIfApplicable(persistence, screen);
    }
    private void recreateGameIfApplicable(Persistence persistence, Screen screen) {
        if (persistence.isInPlay() || screen instanceof PlayScreen) {
            CortexState currentCortexState = persistence.getCurrentCortexState();
            if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
                SinglePlayerGameManager.createNewGameManager(ScreenTracker.playScreen, currentCortexState);
            } else {
                TwoPlayerGameManager.createNewGameManager(ScreenTracker.playScreen, currentCortexState);
            }
        }
    }
    private void recreateOpeningMusicIfApplicable(Persistence persistence, Screen screen) {
        if (!persistence.isInPlay() || !(screen instanceof PlayScreen)) {
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    Sound.loopOpeningBGM();
                }
            }, 1f);
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (assigned) {
            Persistence.getInstance().save();
        }
    }
    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}
