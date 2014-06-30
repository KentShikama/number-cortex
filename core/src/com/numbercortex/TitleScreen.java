package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.numbercortex.ScreenTracker.Mode;

public class TitleScreen implements Screen {
	class TitleScreenListener extends ClickListener {
		private ScreenTracker.Mode mode;
		private int index;
		private Screen screen;
		TitleScreenListener(int index, Screen screen, ScreenTracker.Mode mode) {
			this.index = index;
			this.screen = screen;
		}
		@Override
		public void clicked(InputEvent event, float x, float y) {
			MoveToAction action = Actions.moveTo(Launch.SCREEN_WIDTH - 50, Launch.SCREEN_HEIGHT - (756 + index * 80), 0.3f);
			Button button = (Button) event.getTarget();
			Action completeAction = new Action() {
			    public boolean act( float delta ) {
					ScreenTracker.mode = mode;
					if (screen != null) {
						game.setScreen(screen);	
					}
					return true;
			    }
			};
			button.addAction(Actions.sequence(action, completeAction));
		}
	}
	class TitleScreenButton {

		private Button button;
		public Button getButton() {
			return button;
		}

		TitleScreenButton(String buttonName, int index, Screen screen) {
			Drawable buttonDrawable = skin.getDrawable(buttonName);
			TextureRegion buttonTexture = skin.getRegion(buttonName);
			Button.ButtonStyle buttonStyle = new Button.ButtonStyle(buttonDrawable, buttonDrawable, buttonDrawable);
			button = new Button(buttonStyle);
			button.setBounds(175, Launch.SCREEN_HEIGHT - (756 + index * 80), buttonTexture.getRegionWidth(),
					buttonTexture.getRegionHeight());
			Mode mode;
			if (buttonName.equals(PLAY_BUTTON)) {
				mode = ScreenTracker.Mode.SINGLE_PLAYER;
			} else if (buttonName.equals(PASS_AND_PLAY_BUTTON)) {
				mode = ScreenTracker.Mode.TWO_PLAYER;
			} else if (buttonName.equals(PLAY_ONLINE)) {
				mode = ScreenTracker.Mode.ONLINE;
			} else {
				mode = null;
			}
			ClickListener listener = new TitleScreenListener(index, screen, mode);
			button.addListener(listener);
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
		TitleScreenButton playButton = new TitleScreenButton(PLAY_BUTTON, 0, ScreenTracker.settingsScreen);
		TitleScreenButton passAndPlayButton = new TitleScreenButton(PASS_AND_PLAY_BUTTON, 1, ScreenTracker.settingsScreen);
		TitleScreenButton playOnlineButton = new TitleScreenButton(PLAY_ONLINE, 2, ScreenTracker.settingsScreen);
		TitleScreenButton tutorialButton =new TitleScreenButton(TUTORIAL, 3, null);
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
