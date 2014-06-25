package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Launch extends Game {
	boolean firstTimeCreate = true;
	FPSLogger fps;
	
	private Stage stage;
	
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 1136;

	@Override
	public void create() {
		Assets.loadHome();
		Assets.loadSettings();
		Assets.loadGame();
		FontGenerator.load();
		CortexPreferences.getInstance().load();
		
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);

		setScreen(new TitleScreen(this));
		fps = new FPSLogger();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
	}

	@Override
	public void render() {
		super.render();
//		fps.log();
	}
	
}

