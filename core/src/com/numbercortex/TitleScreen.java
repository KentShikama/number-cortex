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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TitleScreen implements Screen {
	class TitleScreenButton {

		private Button button;
		private int index;
		private Screen screen;
		private ScreenTracker.Mode mode;

		TitleScreenButton(String buttonName, int index, Screen screen, ScreenTracker.Mode mode) {
			this.index = index;
			this.screen = screen;
			this.mode = mode;

			Drawable buttonDrawable = Assets.homeSkin.getDrawable(buttonName);
			TextureRegion buttonTexture = Assets.homeSkin.getRegion(buttonName);
			Button.ButtonStyle buttonStyle = new Button.ButtonStyle(buttonDrawable, buttonDrawable, buttonDrawable);
			button = new Button(buttonStyle);
			button.setBounds(175, Launch.SCREEN_HEIGHT - (756 + index * 80), buttonTexture.getRegionWidth(),
					buttonTexture.getRegionHeight());
			ClickListener listener = new TitleScreenListener();
			button.addListener(listener);
		}

		class TitleScreenListener extends ClickListener {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				float moveTime = 0.35f;
				Button button = (Button) event.getTarget();
				for (Actor actor : stage.getActors()) {
					DelayAction delayAction = Actions.delay(moveTime - moveTime / 2);
					AlphaAction fadeOutAction = Actions.fadeOut(0.35f);
					SequenceAction sequence = Actions.sequence(delayAction, fadeOutAction);
					if (actor == button) {
						MoveToAction moveToAction = Actions.moveTo(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT
								- (756 + index * 80), moveTime);
						Action screenSwitchAction = buildScreenSwitchAction();
						SequenceAction clickedButtonSequence = Actions.sequence(delayAction, fadeOutAction,
								screenSwitchAction);
						ParallelAction parallel = Actions.parallel(moveToAction, clickedButtonSequence);
						button.addAction(parallel);
					} else {
						actor.addAction(sequence);
					}
				}
			}
			private Action buildScreenSwitchAction() {
				Action completeAction = new Action() {
					@Override
					public boolean act(float delta) {
						if (screen != null) {
							if (mode == ScreenTracker.Mode.SINGLE_PLAYER && CortexPreferences.getInstance().getCurrentLevel() == 0) {
								ScreenTracker.mode = mode;
								ScreenTracker.level = 0;
								GameManager manager = GameManagerImpl.createNewGameManager();
								game.setScreen(ScreenTracker.playScreen);
								manager.startNewGame();
							} else {
								ScreenTracker.mode = mode;
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

	public static final String TAG = TitleScreen.class.getName();

	private static final String TITLE_BACKGROUND = "title_background";
	private static final String PLAY_BUTTON = "play";
	private static final String PASS_AND_PLAY_BUTTON = "pass_and_play";
	private static final String PLAY_ONLINE = "play_online";
	private static final String TUTORIAL = "tutorial";

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
		BackgroundScreen background = new BackgroundScreen(Launch.SEA_BLUE, Assets.backgroundTexture);
		stage.addActor(background);
	}
	private void buildButtons() {
		TitleScreenButton playButton = new TitleScreenButton(PLAY_BUTTON, 0, ScreenTracker.levelsScreen,
				ScreenTracker.Mode.SINGLE_PLAYER);
		TitleScreenButton passAndPlayButton = new TitleScreenButton(PASS_AND_PLAY_BUTTON, 1,
				ScreenTracker.twoPlayerSettingsScreen, ScreenTracker.Mode.TWO_PLAYER);
		TitleScreenButton playOnlineButton = new TitleScreenButton(PLAY_ONLINE, 2,
				ScreenTracker.twoPlayerSettingsScreen, ScreenTracker.Mode.ONLINE);
		TitleScreenButton tutorialButton = new TitleScreenButton(TUTORIAL, 3, null, null);
		stage.addActor(playButton.button);
		stage.addActor(passAndPlayButton.button);
		stage.addActor(playOnlineButton.button);
		stage.addActor(tutorialButton.button);
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
	public void resume() {}
	@Override
	public void dispose() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
}
