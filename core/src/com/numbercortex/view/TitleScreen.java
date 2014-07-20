package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.numbercortex.Launch;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;

class TitleScreen implements Screen {
	class TitleScreenButton extends Group {

		private Screen screen;
		private ModeTracker.Mode mode;

		TitleScreenButton(String buttonName, int index, Screen screen, ModeTracker.Mode mode) {
			this.screen = screen;
			this.mode = mode;

			Drawable buttonDrawable = Assets.homeSkin.getDrawable(buttonName);
			TextureRegion buttonTexture = Assets.homeSkin.getRegion(buttonName);
			Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
			buttonStyle.up = buttonDrawable;
			Button button = new Button(buttonStyle);
			button.setBounds(175, Launch.SCREEN_HEIGHT - (836 + index * 80) + 30, buttonTexture.getRegionWidth(),
					buttonTexture.getRegionHeight());
			if (screen != null) {
				ClickListenerWithSound listener = new TitleScreenListener();
				button.addListener(listener);
			}
			this.addActor(button);
		}

		class TitleScreenListener extends ClickListenerWithSound {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Action screenSwitchAction = buildScreenSwitchAction();
				TitleScreenButton.this.addAction(screenSwitchAction);
			}
			private Action buildScreenSwitchAction() {
				Action completeAction = new Action() {
					@Override
					public boolean act(float delta) {
						if (screen != null) {
							if (mode == ModeTracker.Mode.SINGLE_PLAYER && Persistence.getInstance().getMaxLevel() == 0) {
								ModeTracker.mode = mode;
								GameManager manager = SinglePlayerGameManager.createNewGameManager();
								game.setScreen(ScreenTracker.playScreen);
								manager.startNewGame();
							} else {
								ModeTracker.mode = mode;
								game.setScreen(screen);
							}
						}
						return true;
					}
				};
				return completeAction;
			}
		}
	}

	public static final String TAG = "Title Screen";

	private static final String TITLE = "number_cortex_title";
	private static final String PLAY_BUTTON = "play";
	private static final String PASS_AND_PLAY_BUTTON = "pass_and_play";
	private static final String OPTIONS = "options";
	private static final String LINE_EXTENSION = "line_extension";

	private Stage stage;
	private Game game;

	TitleScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}

	@Override
	public void show() {
		stage.clear();
		buildBackground();
		buildTitle();
		buildButtons();
		buildLineExtension();
		Gdx.input.setCatchBackKey(false);
	}
	private void buildBackground() {
		Background background = new Background(Launch.SEA_BLUE, Assets.backgroundTexture);
		stage.addActor(background);
	}
	private void buildTitle() {
		TextureRegion titleTexture = Assets.homeSkin.getRegion(TITLE);
		Image title = new Image(titleTexture);
		title.setBounds(63, Launch.SCREEN_HEIGHT - 762 + 30, titleTexture.getRegionWidth(),
				titleTexture.getRegionHeight());
		stage.addActor(title);
	}
	private void buildButtons() {
		TitleScreenButton playButton = new TitleScreenButton(PLAY_BUTTON, 0, ScreenTracker.levelsScreen,
				ModeTracker.Mode.SINGLE_PLAYER);
		TitleScreenButton passAndPlayButton = new TitleScreenButton(PASS_AND_PLAY_BUTTON, 1,
				ScreenTracker.twoPlayerSettingsScreen, ModeTracker.Mode.TWO_PLAYER);
		TitleScreenButton optionsButton = new TitleScreenButton(OPTIONS, 2, null, null);
		stage.addActor(playButton);
		stage.addActor(passAndPlayButton);
		stage.addActor(optionsButton);
	}
	private void buildLineExtension() {
		TextureRegion lineExtensionTexture = Assets.homeSkin.getRegion(LINE_EXTENSION);
		Image lineExtension = new Image(lineExtensionTexture);
		lineExtension.setBounds(175, Launch.SCREEN_HEIGHT - 1036 + 30, lineExtensionTexture.getRegionWidth(),
				lineExtensionTexture.getRegionHeight());
		stage.addActor(lineExtension);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		persistence.setCurrentScreen(TAG);
		persistence.save();
	}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}
	@Override
	public void hide() {}
}
