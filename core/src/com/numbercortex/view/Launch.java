package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.numbercortex.CortexState;
import com.numbercortex.ModeTracker;
import com.numbercortex.ModeTracker.Mode;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;

public class Launch extends Game {

	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 1136;

	public static final Color SEA_BLUE = new Color(79f / 255, 120f / 255, 141f / 255, 1);
	public static final Color SEA_GREEN = new Color(141f / 255, 185f / 255, 170f / 255, 1);
	public static final Color RETRO_RED = new Color(200f / 255, 99f / 255, 91f / 255, 1);
	public static final Color BRIGHT_YELLOW = new Color(250f / 255, 235f / 255, 102f / 255, 1);

	private Stage numberBackgroundStage;
	private Stage plainBackgroundStage;
	private boolean assigned;
	private FPSLogger fps;

	@Override
	public void create() {
		loadAndAssignStartingAssets();
		loadAssets();
		fps = new FPSLogger();
	}
	private void loadAndAssignStartingAssets() {
		Assets.manager = new AssetManager();
		Assets.loadBackground();
		Assets.manager.finishLoading();
		Assets.assignBackgroundScreen();
		numberBackgroundStage = buildBackgroundStage();
		plainBackgroundStage = buildBackgroundStage();
	}
	private Stage buildBackgroundStage() {
		ExtendViewport extendViewport = new ExtendViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT,
				(float) (Launch.SCREEN_HEIGHT / 1.2), Launch.SCREEN_HEIGHT);
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
		Assets.loadAndAssignFonts();
		assigned = false;
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		addBackground();
		if (this.getScreen() instanceof PlayScreen) {
			resumeGame();
		}
	}
	private void addBackground() {
		numberBackgroundStage.clear();
		plainBackgroundStage.clear();
		float worldWidth = numberBackgroundStage.getViewport().getWorldWidth();
		Background numberBackground = new Background(Launch.SEA_BLUE, Assets.backgroundTexture, worldWidth);
		Background plainBackground = new Background(Launch.SEA_BLUE, worldWidth);
		numberBackgroundStage.addActor(numberBackground);
		plainBackgroundStage.addActor(plainBackground);
	}
	private void resumeGame() {
		GameManager gameManager;
		if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
			gameManager = SinglePlayerGameManager.getInstance();
		} else {
			gameManager = TwoPlayerGameManager.getInstance();
		}
		gameManager.resumeGame();
	}

	
	@Override
	public void render() {
		fps.log();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Assets.manager.update()) {
			assignAssetsAndShowGameIfApplicable();
			renderBackground();
			super.render();
		} else {
			renderBackground();
		}
	}
	private void renderBackground() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		if (this.getScreen() instanceof PlayScreen || this.getScreen() == null) {
			plainBackgroundStage.getViewport().update(screenWidth, screenHeight, true);
			plainBackgroundStage.draw();
		} else {
			numberBackgroundStage.getViewport().update(screenWidth, screenHeight, true);
			numberBackgroundStage.draw();
		}
	}
	
	private void assignAssetsAndShowGameIfApplicable() {
		if (!assigned) {
			Assets.assignHomeScreen();
			Assets.assignSettingsScreen();
			Assets.assignPlayScreen();
			Assets.assignLevelsScreen();
			Assets.assignDialogScreen();
			Assets.assignAudio();
			showGame();
			assigned = true;
		}
	}
	private void showGame() {
		Persistence persistence = Persistence.getInstance().load();
		ModeTracker.mode = buildMode(persistence);
		ScreenTracker.initializeScreens(this);
		Screen screen = buildCurrentScreen(persistence);
		GameManager gameManager = buildGameManager(persistence, screen);
		setScreen(screen);
		resumeGameIfApplicable(gameManager, screen);
	}
	private Mode buildMode(Persistence persistence) {
		String currentModeString = persistence.getMode();
		return ModeTracker.getMode(currentModeString);
	}
	private Screen buildCurrentScreen(Persistence persistence) {
		String currentScreenString = persistence.getCurrentScreen();
		Screen screen = ScreenTracker.getScreen(currentScreenString);
		return screen;
	}
	private GameManager buildGameManager(Persistence persistence, Screen screen) {
		GameManager gameManager = null;
		if (persistence.isInPlay() || screen instanceof PlayScreen) {
			CortexState currentCortexState = persistence.getCurrentCortexState();
			if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
				gameManager = SinglePlayerGameManager.createNewGameManager(currentCortexState);
			} else {
				gameManager = TwoPlayerGameManager.createNewGameManager(currentCortexState);
			}
		}
		return gameManager;
	}
	private void resumeGameIfApplicable(GameManager gameManager, Screen screen) {
		if (screen instanceof PlayScreen) {
			gameManager.resumeGame();
		}
	}


	@Override
	public void pause() {
		super.pause();
		Persistence.getInstance().save();
	}
	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}
}
