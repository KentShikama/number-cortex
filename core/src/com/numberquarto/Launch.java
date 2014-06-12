package com.numberquarto;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;


public class Launch extends Game {
	boolean firstTimeCreate = true;
	FPSLogger fps;
	
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 1136;

	@Override
	public void create() {
		Assets.load();
		setScreen(new SettingsScreen());
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
//		fps.log();
	}

	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
	}
	
}

