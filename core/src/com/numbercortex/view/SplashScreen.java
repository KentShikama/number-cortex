package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

class SplashScreen extends GameScreen {
	
	private Image title;

	SplashScreen(Game game) {
		super(game);
	}
	
	private static final String TITLE = "number_cortex_title";
	
	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		buildTitle(stage);
	}
	private void buildTitle(Stage stage) {
		TextureRegion titleTexture = Assets.homeSkin.getRegion(TITLE);
		title = new Image(titleTexture);
		title.setBounds(63, Launch.SCREEN_HEIGHT - 776, titleTexture.getRegionWidth(),
				titleTexture.getRegionHeight());
		stage.addActor(title);
	}
	
	void animatedTitleTransition(final HomeScreen screen) {
		MoveToAction moveUp = Actions.moveTo(63, Launch.SCREEN_HEIGHT - 660, 1f);
		Action switchToTitle = Actions.run(new Runnable() {
			@Override
			public void run() {
				game.setScreen(screen);
			}
		});
		title.addAction(Actions.sequence(moveUp, switchToTitle));
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
