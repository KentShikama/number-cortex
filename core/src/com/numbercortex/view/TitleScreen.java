package com.numbercortex.view;

import libgdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;

class TitleScreen extends HomeScreen {

	static final String TAG = "Title Screen";

	private static final String PLAY_BUTTON = "Play";
	private static final String PASS_AND_PLAY_BUTTON = "Pass & Play";
	private static final String OPTIONS = "Options";
	private static final String MORE = "More";

	TitleScreen(Game game) {
		super(game);
	}

	@Override
	void setUpBackKeyCatch() {
		Gdx.input.setCatchBackKey(false);
	}
	@Override
	void buildButtons(Stage stage) {
		HomeScreenButton playButton = new HomeScreenButton(PLAY_BUTTON, 0, ScreenTracker.levelsScreen,
				ModeTracker.Mode.SINGLE_PLAYER);
		HomeScreenButton passAndPlayButton = new HomeScreenButton(PASS_AND_PLAY_BUTTON, 1,
				ScreenTracker.twoPlayerSettingsScreen, ModeTracker.Mode.TWO_PLAYER);
		HomeScreenButton optionsButton = new HomeScreenButton(OPTIONS, 2, ScreenTracker.optionsScreen, null);
		HomeScreenButton moreButton = new HomeScreenButton(MORE, 3, ScreenTracker.moreScreen, null);
		stage.addActor(playButton);
		stage.addActor(passAndPlayButton);
		stage.addActor(optionsButton);
		stage.addActor(moreButton);
	}
	@Override
	void buildBottomNavigation(Stage stage) {}

	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
	}

	@Override
	void handleBackKey() {}
}
