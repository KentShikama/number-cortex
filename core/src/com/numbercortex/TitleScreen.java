package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TitleScreen implements Screen {
	abstract class TitleScreenButton {

		private Button button;
		public Button getButton() {
			return button;
		}

		TitleScreenButton(String buttonName, int index, ClickListener listener) {
			Drawable buttonDrawable = skin.getDrawable(buttonName);
			TextureRegion buttonTexture = skin.getRegion(buttonName);
			Button.ButtonStyle buttonStyle = new Button.ButtonStyle(buttonDrawable, buttonDrawable, buttonDrawable);
			button = new Button(buttonStyle);
			button.setBounds(175, Launch.SCREEN_HEIGHT - (756 + index * 80), buttonTexture.getRegionWidth(),
					buttonTexture.getRegionHeight());
			button.addListener(listener);
		}
	}
	class PlayButton extends TitleScreenButton {
		PlayButton() {
			super(PLAY_BUTTON, 0, new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenTracker.mode = ScreenTracker.Mode.SINGLE_PLAYER;
					game.setScreen(ScreenTracker.settingsScreen);
				}
			});
		}
	}
	class PassAndPlayButton extends TitleScreenButton {
		PassAndPlayButton() {
			super(PASS_AND_PLAY_BUTTON, 1, new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenTracker.mode = ScreenTracker.Mode.TWO_PLAYER;
					game.setScreen(ScreenTracker.settingsScreen);
				}
			});
		}
	}
	class PlayOnlineButton extends TitleScreenButton {
		PlayOnlineButton() {
			super(PLAY_ONLINE, 2, new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ScreenTracker.mode = ScreenTracker.Mode.ONLINE;
					game.setScreen(ScreenTracker.settingsScreen);
				}
			});
		}
	}
	class TutorialButton extends TitleScreenButton {
		TutorialButton() {
			super(TUTORIAL, 3, new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Gdx.app.log(TAG, TUTORIAL + " has been clicked");
				}
			});
		}
	}

	public static final String TAG = TitleScreen.class.getName();

	private static final String TITLE_BACKGROUND = "title_background";
	private static final String PLAY_BUTTON = "play";
	private static final String PASS_AND_PLAY_BUTTON = "pass_and_play";
	private static final String PLAY_ONLINE = "play_online";
	private static final String TUTORIAL = "tutorial";

	private static Skin skin = Assets.homeSkin;
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
		buildButtons();
	}
	private void buildBackground() {
		ScreenBackground background = new ScreenBackground(skin, TITLE_BACKGROUND);
		stage.addActor(background);
	}
	private void buildButtons() {
		TitleScreenButton playButton = new PlayButton();
		TitleScreenButton passAndPlayButton = new PassAndPlayButton();
		TitleScreenButton playOnlineButton = new PlayOnlineButton();
		TitleScreenButton tutorialButton = new TutorialButton();
		stage.addActor(playButton.getButton());
		stage.addActor(passAndPlayButton.getButton());
		stage.addActor(playOnlineButton.getButton());
		stage.addActor(tutorialButton.getButton());
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
	public void resume() {
		Assets.loadHome();
	}
	@Override
	public void dispose() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
}
