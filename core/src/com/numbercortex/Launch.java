package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Launch extends Game {
	private Stage stage;

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
		Assets.manager.finishLoading();
		Assets.assignBackgroundScreen();
		Assets.assignHomeScreen();
		Assets.assignSettingsScreen();
		Assets.assignPlayScreen();
		Assets.assignLevelsScreen();
		Assets.assignDialogScreen();
		
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);

		FontGenerator.load();
		Persistence persistence = Persistence.getInstance().load();
		
		String currentModeString = persistence.getMode();
		ScreenTracker.mode = ScreenTracker.getMode(currentModeString);
		
		ScreenTracker.titleScreen = new TitleScreen(this);
		ScreenTracker.singlePlayerSettingsScreen = new SinglePlayerSettingsScreen(this);
		ScreenTracker.twoPlayerSettingsScreen = new TwoPlayerSettingsScreen(this);
		ScreenTracker.levelsScreen = new LevelsScreen(this);
		ScreenTracker.playScreen = new PlayScreen(this);
		
		GameManager gameManager = null;
		if (persistence.isInPlay()) {
			CortexState currentCortexState = persistence.getCurrentCortexState();
			gameManager = GameManagerImpl.createNewGameManager(currentCortexState);
		}
		
		String currentScreenString = persistence.getCurrentScreen();
		Screen screen = ScreenTracker.getScreen(currentScreenString);
		setScreen(screen);
		
		if (screen instanceof PlayScreen) {
			gameManager.resumeGame();
		}
	}

	public Stage getStage() {
		return stage;
	}

	@Override
	public void dispose() {
		super.dispose();
		ScreenTracker.dispose();
		CortexDialog.dispose();
		MessageArea.dispose();
		NumberCortexBoard.dispose();
		NumberScroller.dispose();
		FontGenerator.dispose();
		Assets.manager.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resume() {
		super.resume();
		Assets.manager.finishLoading();
	}

}
