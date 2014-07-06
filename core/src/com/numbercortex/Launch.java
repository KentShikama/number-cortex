package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
		buildHomeScreen();
		buildOtherScreens();
	}
	private void buildHomeScreen() {
		Assets.loadHome();
		Assets.manager.finishLoading();
		Assets.assignHomeScreen();
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);
		ScreenTracker.titleScreen = new TitleScreen(Launch.this); 
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				setScreen(ScreenTracker.titleScreen);
			}
		});
	}
	private void buildOtherScreens() {
		Assets.loadSettings();
		Assets.loadGame();
		Assets.loadLevels();
		Assets.manager.finishLoading();
		Assets.assignSettingsScreen();
		Assets.assignPlayScreen();
		Assets.assignLevelsScreen();
		
		FontGenerator.load();
		CortexPreferences.getInstance().load();
		ScreenTracker.singlePlayerSettingsScreen = new SinglePlayerSettingsScreen(this);
		ScreenTracker.twoPlayerSettingsScreen = new TwoPlayerSettingsScreen(this);
		ScreenTracker.levelsScreen = new LevelsScreen(this);
		ScreenTracker.playScreen = new PlayScreen(this);
	}

	public Stage getStage() {
		return stage;
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
		ScreenTracker.levelsScreen.dispose();
		Assets.manager.clear();
	}

	@Override
	public void render() {
		super.render();
	}

}
