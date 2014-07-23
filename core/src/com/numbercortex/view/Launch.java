package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
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

	private Stage backgroundStage;

	@Override
	public void create() {
		loadAndAssignAssets();
		showGame();
	}
	private void loadAndAssignAssets() {
		Assets.manager = new AssetManager();
		Assets.loadBackground();
		Assets.loadHome();
		Assets.loadSettings();
		Assets.loadGame();
		Assets.loadLevels();
		Assets.loadDialog();
		Assets.loadAudio();
		Assets.manager.finishLoading();
		Assets.assignBackgroundScreen();
		Assets.assignHomeScreen();
		Assets.assignSettingsScreen();
		Assets.assignPlayScreen();
		Assets.assignLevelsScreen();
		Assets.assignDialogScreen();
		Assets.assignAudio();
		Assets.loadAndAssignFonts();
	}
	private void showGame() {
		Persistence persistence = Persistence.getInstance().load();
		backgroundStage = buildBackground();
		ModeTracker.mode = buildMode(persistence);
		ScreenTracker.initializeScreens(this);
		GameManager gameManager = buildGameManager(persistence);
		Screen screen = buildCurrentScreen(persistence);
		resumeGameIfApplicable(gameManager, screen);
	}
	private void resumeGameIfApplicable(GameManager gameManager, Screen screen) {
		if (screen instanceof PlayScreen) {
			gameManager.resumeGame();
		}
	}
	private Stage buildBackground() {
		FillViewport stretchViewport = new FillViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		Stage backgroundStage = new Stage(stretchViewport);
		Background background = new Background(Launch.SEA_BLUE, Assets.backgroundTexture);
		backgroundStage.addActor(background);
		return backgroundStage;
	}
	private Mode buildMode(Persistence persistence) {
		String currentModeString = persistence.getMode();
		return ModeTracker.getMode(currentModeString);
	}
	private GameManager buildGameManager(Persistence persistence) {
		GameManager gameManager = null;
		if (persistence.isInPlay()) {
			CortexState currentCortexState = persistence.getCurrentCortexState();
			if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
				gameManager = SinglePlayerGameManager.createNewGameManager(currentCortexState);
			} else {
				gameManager = TwoPlayerGameManager.createNewGameManager(currentCortexState);
			}
		}
		return gameManager;
	}
	private Screen buildCurrentScreen(Persistence persistence) {
		String currentScreenString = persistence.getCurrentScreen();
		Screen screen = ScreenTracker.getScreen(currentScreenString);
		setScreen(screen);
		return screen;
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		Screen currentScreen = this.getScreen(); 
		if (currentScreen instanceof PlayScreen) {
			GameManager gameManager;
			if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
				gameManager = SinglePlayerGameManager.getInstance();
			} else {
				gameManager = TwoPlayerGameManager.getInstance();
			}
			gameManager.resumeGame();
		}
	}
	
	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderBackgroundIfApplicable();
		super.render();
	}
	private void renderBackgroundIfApplicable() {
		if (!(this.getScreen() instanceof PlayScreen)) {
			renderBackground();
		}
	}
	private void renderBackground() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		backgroundStage.getViewport().update(screenWidth, screenHeight, true);
		backgroundStage.draw();
		super.resize(screenWidth, screenHeight);
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
