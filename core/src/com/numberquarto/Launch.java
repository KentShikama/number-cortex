package com.numberquarto;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;


public class Launch extends Game {
	boolean firstTimeCreate = true;
	FPSLogger fps;

	@Override
	public void create() {
		Assets.load();
		setScreen(new TitleScreen());
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

