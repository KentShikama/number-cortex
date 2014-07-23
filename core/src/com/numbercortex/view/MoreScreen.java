package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.numbercortex.Persistence;

public class MoreScreen extends HomeScreen {

	public static final String TAG = "More Screen";

	private static final String MORE_GAMES_BUTTON = "More Games";
	private static final String RATE_GAME_BUTTON = "Rate Game";
	private static final String WEBSITE_BUTTON = "Website";
	private static final String CREDITS = "Credits";

	MoreScreen(Game game) {
		super(game);
	}

	@Override
	void setUpBackKeyCatch() {
		Gdx.input.setCatchBackKey(true);
		backKey = false;
	}
	@Override
	void buildButtons(Stage stage) {
		HomeScreenButton playButton = new HomeScreenButton(MORE_GAMES_BUTTON, 0, null, null);
		HomeScreenButton passAndPlayButton = new HomeScreenButton(RATE_GAME_BUTTON, 1, null, null);
		HomeScreenButton optionsButton = new HomeScreenButton(WEBSITE_BUTTON, 2, null, null);
		HomeScreenButton moreButton = new HomeScreenButton(CREDITS, 3, ScreenTracker.creditsScreen, null);
		stage.addActor(playButton);
		stage.addActor(passAndPlayButton);
		stage.addActor(optionsButton);
		stage.addActor(moreButton);
	}
	@Override
	void buildBottomNavigation(Stage stage) {
		BackBottomNavigation backBottomNavigation = new BackBottomNavigation("Home", ScreenTracker.titleScreen);
		stage.addActor(backBottomNavigation);
	}
	@Override
	void handleBackKey() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			backKey = true;
		} else if (backKey) {
			backKey = false;
			game.setScreen(ScreenTracker.titleScreen);
		}
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
	}

}
