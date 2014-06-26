package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
 		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
		 		setScreen(new TitleScreen(Launch.this));
			}
		});
		Assets.loadSettings();
		Assets.loadGame();
		FontGenerator.load();
		CortexPreferences.getInstance().load();
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
	}
	
}

