package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.numbercortex.TwoPlayerSettingsScreen.SettingsTitle;

public class SinglePlayerSettingsScreen implements Screen {

	class MenuBody extends Actor {
		private TextureRegion menuBody;

		MenuBody() {
			menuBody = skin.getRegion(MENU_BODY);
			this.setBounds(0, Launch.SCREEN_HEIGHT - 875, menuBody.getRegionWidth(), menuBody.getRegionHeight());
		}

		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(menuBody, this.getX(), this.getY());
		}
	}

	class SettingsTitle extends Actor {
		private TextureRegion settingsTitle;

		SettingsTitle() {
			settingsTitle = skin.getRegion(SETTINGS_TITLE);
			this.setBounds(0, Launch.SCREEN_HEIGHT - 245, settingsTitle.getRegionWidth(),
					settingsTitle.getRegionHeight());
		}

		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(settingsTitle, this.getX(), this.getY());
		}
	}

	private static final String TAG = TwoPlayerSettingsScreen.class.getCanonicalName();
	private static Skin skin = Assets.settingsSkin;

	private Stage stage;
	private Game game;

	private static final int CHECKBOX_LENGTH = 84;

	private static final TextureRegion PLAY_BUTTON_TEXTURE = Assets.settingsSkin.getRegion("play_button");
	private static final int RIGHT_BUTTON_WIDTH = PLAY_BUTTON_TEXTURE.getRegionWidth();
	private static final int RIGHT_BUTTON_HEIGHT = PLAY_BUTTON_TEXTURE.getRegionHeight();

	private static final TextureRegion QUIT_BUTTON_TEXTURE = Assets.settingsSkin.getRegion("quit_button");
	private static final int LEFT_BUTTON_WIDTH = QUIT_BUTTON_TEXTURE.getRegionWidth();
	private static final int LEFT_BUTTON_HEIGHT = QUIT_BUTTON_TEXTURE.getRegionHeight();

	private static final String SETTINGS_BACKGROUND = "settings_background";
	private static final String SETTINGS_TITLE = "settings_title";
	private static final String MENU_BODY = "menu_body";

	public SinglePlayerSettingsScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
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
	public void show() {
		stage.clear();
		
		ScreenBackground background = new ScreenBackground(skin, SETTINGS_BACKGROUND);
		stage.addActor(background);

		SettingsTitle title = new SettingsTitle();
		stage.addActor(title);

		if (ScreenTracker.isInPlay) {
			buildResumeButton();
		} else {
			buildPlayButton();
		}
		buildQuitButton();
	}

	private void buildPlayButton() {
		Drawable playButtonSkin = skin.getDrawable("play_button");
		final ImageButton playButton = new ImageButton(playButtonSkin);
		playButton.setBounds(277, Launch.SCREEN_HEIGHT - 1045, RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.playScreen);
			}
		});
		stage.addActor(playButton);
	}

	private void buildQuitButton() {
		Drawable quitButtonSkin = skin.getDrawable("quit_button");
		final ImageButton exitButton = new ImageButton(quitButtonSkin);
		exitButton.setBounds(72, Launch.SCREEN_HEIGHT - 1045, LEFT_BUTTON_WIDTH, LEFT_BUTTON_HEIGHT);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenTracker.isInPlay = false;
				game.setScreen(ScreenTracker.titleScreen);
			}
		});
		stage.addActor(exitButton);

	}

	private void buildResumeButton() {
		Drawable resumeButtonSkin = skin.getDrawable("resume_button");
		ImageButton resumeButton = new ImageButton(resumeButtonSkin);
		resumeButton.setBounds(277, Launch.SCREEN_HEIGHT - 1045, RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameManager manager = GameManagerImpl.getInstance();
				game.setScreen(ScreenTracker.playScreen);
				manager.resumeGame();
			}
		});
		stage.addActor(resumeButton);
	}

	@Override
	public void hide() {
		CortexPreferences.getInstance().save();
	}

	@Override
	public void resume() {
		Assets.loadSettings();
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {}

}
