package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TitleScreen implements Screen {
	class TitleScreenListener extends ClickListener {
		private ScreenTracker.Mode mode;
		private int index;
		private Screen screen;
		TitleScreenListener(int index, Screen screen, ScreenTracker.Mode mode) {
			this.index = index;
			this.screen = screen;
			this.mode = mode;
		}
		@Override
		public void clicked(InputEvent event, float x, float y) {
			float moveTime = 0.35f;
			Button button = (Button) event.getTarget();
			for (Actor actor : stage.getActors()) {
				DelayAction delayAction = Actions.delay(moveTime - moveTime/2);
				AlphaAction fadeOutAction = Actions.fadeOut(0.35f);
				SequenceAction sequence = Actions.sequence(delayAction, fadeOutAction);		
				if (actor == button) {
					MoveToAction moveToAction = Actions.moveTo(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT - (756 + index * 80), moveTime);
					Action screenSwitchAction = buildScreenSwitchAction();
					SequenceAction clickedButtonSequence = Actions.sequence(delayAction, fadeOutAction, screenSwitchAction);
					ParallelAction parallel = Actions.parallel(moveToAction, clickedButtonSequence);
					button.addAction(parallel);
				} else {
					actor.addAction(sequence);
				}
			}
		}
		private Action buildScreenSwitchAction() {
			Action completeAction = new Action() {
			    public boolean act( float delta ) {
					if (screen != null) {
				    	ScreenTracker.mode = mode;
						game.setScreen(screen);	
					}
					return true;
			    }
			};
			return completeAction;
		}
	}
	class TitleScreenButton {

		private Button button;
		public Button getButton() {
			return button;
		}

		TitleScreenButton(String buttonName, int index, Screen screen, ScreenTracker.Mode mode) {
			Drawable buttonDrawable = skin.getDrawable(buttonName);
			TextureRegion buttonTexture = skin.getRegion(buttonName);
			Button.ButtonStyle buttonStyle = new Button.ButtonStyle(buttonDrawable, buttonDrawable, buttonDrawable);
			button = new Button(buttonStyle);
			button.setBounds(175, Launch.SCREEN_HEIGHT - (756 + index * 80), buttonTexture.getRegionWidth(),
					buttonTexture.getRegionHeight());
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
		TitleScreenButton playButton = new TitleScreenButton(PLAY_BUTTON, 0, ScreenTracker.settingsScreen, ScreenTracker.Mode.SINGLE_PLAYER);
		TitleScreenButton passAndPlayButton = new TitleScreenButton(PASS_AND_PLAY_BUTTON, 1, ScreenTracker.settingsScreen, ScreenTracker.Mode.TWO_PLAYER);
		TitleScreenButton playOnlineButton = new TitleScreenButton(PLAY_ONLINE, 2, ScreenTracker.settingsScreen, ScreenTracker.Mode.ONLINE);
		TitleScreenButton tutorialButton =new TitleScreenButton(TUTORIAL, 3, null, null);
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
