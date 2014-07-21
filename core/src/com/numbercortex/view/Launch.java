package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.numbercortex.CortexState;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;

public class Launch extends Game {
	private Stage backgroundStage;

	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 1136;
	public static final Color SEA_BLUE = new Color(79f / 255, 120f / 255, 141f / 255, 1);
	public static final Color SEA_GREEN = new Color(141f / 255, 185f / 255, 170f / 255, 1);
	public static final Color RETRO_RED = new Color(200f / 255, 99f / 255, 91f / 255, 1);
	public static final Color BRIGHT_YELLOW = new Color(250f / 255, 235f / 255, 102f / 255, 1);

	@Override
	public void create() {
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
		
		StretchViewport stretchViewport = new StretchViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		backgroundStage = new Stage(stretchViewport);
		Background background = new Background(Launch.SEA_BLUE, Assets.backgroundTexture);
		backgroundStage.addActor(background);

		Assets.loadFonts();
		Persistence persistence = Persistence.getInstance().load();

		String currentModeString = persistence.getMode();
		ModeTracker.mode = ModeTracker.getMode(currentModeString);

		ScreenTracker.initializeScreens(this);

		GameManager gameManager = buildGameManager(persistence);

		String currentScreenString = persistence.getCurrentScreen();
		Screen screen = ScreenTracker.getScreen(currentScreenString);
		setScreen(screen);

		if (screen instanceof PlayScreen) {
			gameManager.resumeGame();
		}
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

	@Override
	public void dispose() {
		super.dispose();
		ScreenTracker.dispose();
		Assets.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!(this.getScreen() instanceof PlayScreen)) {
			int screenWidth = Gdx.graphics.getWidth();
			int screenHeight = Gdx.graphics.getHeight();
			backgroundStage.getViewport().update(screenWidth, screenHeight, true);
			backgroundStage.draw();
			super.resize(screenWidth, screenHeight);
		}
		super.render();
	}
	@Override
	public void resume() {
		super.resume();
		Assets.manager.finishLoading();
	}
	public void pause() {
		super.pause();
		Persistence.getInstance().save();
	}

}
