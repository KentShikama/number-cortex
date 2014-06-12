package com.numberquarto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TitleScreen implements Screen {

	private static final String TAG = TitleScreen.class.getName();
	private static final String TITLE_BACKGROUND = "title_background";
	private static final String PLAY_BUTTON = "play";
	private static final String PASS_AND_PLAY_BUTTON = "pass_and_play";
	private static final String PLAY_ONLINE = "play_online";
	private static final String TUTORIAL = "tutorial";

	private Skin skin = Assets.homeSkin;

	private Stage stage;

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
		buildStage();
		buildBackground();
		buildButtons();
	}

	private void buildStage() {
		stage = new Stage(new FitViewport(640, 1136));
		Gdx.input.setInputProcessor(stage);
	}

	private void buildBackground() {
		TitleBackground background = new TitleBackground();
		stage.addActor(background);
	}

	private void buildButtons() {
		TitleScreenButton playButton = new PlayButton();
		TitleScreenButton passAndPlayButton = new PassAndPlayButton();
		TitleScreenButton playOnlineButton = new PlayOnlineButton();
		TitleScreenButton tutorialButton = new TutorialButton();
		stage.addActor(playButton);
		stage.addActor(passAndPlayButton);
		stage.addActor(playOnlineButton);
		stage.addActor(tutorialButton);
	}

	class TitleBackground extends Actor {
		private TextureRegion backgroundTexture;

		TitleBackground() {
			backgroundTexture = skin.getRegion(TITLE_BACKGROUND);
		}

		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(backgroundTexture, 0, 0);
		}
	}

	abstract class TitleScreenButton extends Actor {
		private TextureRegion buttonTexture;

		TitleScreenButton(String buttonName, int index, ClickListener listener) {
			buttonTexture = skin.getRegion(buttonName);
			this.setBounds(175, 1136 - (739 + index * 63),
					buttonTexture.getRegionWidth(),
					buttonTexture.getRegionHeight());
			this.addListener(listener);
		}

		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(buttonTexture, this.getX(), this.getY());
		}
	}

	class PlayButton extends TitleScreenButton {
		PlayButton() {
			super(PLAY_BUTTON, 0, new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, PLAY_BUTTON + " Touch Down");
					return true;
				}
			});
		}
	}

	class PassAndPlayButton extends TitleScreenButton {
		PassAndPlayButton() {
			super(PASS_AND_PLAY_BUTTON, 1, new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, PASS_AND_PLAY_BUTTON + " Touch Down");
					return true;
				}
			});
		}
	}

	class PlayOnlineButton extends TitleScreenButton {
		PlayOnlineButton() {
			super(PLAY_ONLINE, 2, new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, PLAY_ONLINE + " Touch Down");
					return true;
				}
			});
		}
	}

	class TutorialButton extends TitleScreenButton {
		TutorialButton() {
			super(TUTORIAL, 3, new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, TUTORIAL + " Touch Down");
					return true;
				}
			});
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
