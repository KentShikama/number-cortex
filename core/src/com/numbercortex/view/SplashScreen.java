package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

class SplashScreen extends GameScreen {
	
	SplashScreen(Game game) {
		super(game);
	}
	
	private static final String TITLE = "number_cortex_title";
	private static final int FIRST_BUTTON_CENTER_OFFSET_Y = 734;
	
	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		buildTitle(stage);
	}
	private void buildTitle(Stage stage) {
		TextureRegion titleTexture = Assets.homeSkin.getRegion(TITLE);
		Image title = new Image(titleTexture);
		title.setBounds(63, Launch.SCREEN_HEIGHT - (FIRST_BUTTON_CENTER_OFFSET_Y - 74) - 140, titleTexture.getRegionWidth(),
				titleTexture.getRegionHeight());
		stage.addActor(title);
	}
	
	@Override
	public void render(float delta) {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.act(delta);
		stage.draw();
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
}
