package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

class TransitionScreen extends GameScreen {

	private GameScreen currentScreen;
	private GameScreen nextScreen;

	enum Direction {
		LEFT, RIGHT;
	}

	TransitionScreen(Game game) {
		super(game);
	}
	
	void splashTransition(GameScreen nextScreen) {
		transition(null, nextScreen, 1f);
	}

	void transition(Direction direction, GameScreen nextScreen) {
		transition(direction, nextScreen, 0.4f);
	}

	private void transition(Direction direction, GameScreen nextScreen, float duration) {
		GameScreen currentScreen = getCurrentScreen();
		transition(direction, currentScreen, nextScreen, duration);
	}
	private GameScreen getCurrentScreen() {
		GameScreen currentScreen = (GameScreen) game.getScreen();
		if (isInMiddleOfTransition(currentScreen)) {
			currentScreen = this.nextScreen;
		}
		return currentScreen;
	}
	private boolean isInMiddleOfTransition(GameScreen currentScreen) {
		return currentScreen instanceof TransitionScreen;
	}

	private void transition(Direction direction, final GameScreen currentScreen, final GameScreen nextScreen,
			final float duration) {
		this.currentScreen = currentScreen;
		this.nextScreen = nextScreen;

		currentScreen.hide();
		nextScreen.show();

		game.setScreen(this);
		animateNextScreen(direction, nextScreen, duration);
		animateCurrentScreenAndSwitchScreens(direction, currentScreen, nextScreen, duration);
	}
	private void animateNextScreen(Direction direction, final GameScreen nextScreen, final float duration) {
		if (direction == null) {
			animateNextScreenWithoutSliding(nextScreen, duration);
		} else {
			animateNextScreenWithSliding(direction, nextScreen, duration);
		}
	}
	private void animateNextScreenWithoutSliding(final GameScreen nextScreen, final float duration) {
		SequenceAction fadeInAction = Actions.sequence(Actions.fadeOut(0.0001f), Actions.fadeIn(duration));
		nextScreen.stage.addAction(fadeInAction);
	}
	private void animateNextScreenWithSliding(Direction direction, final GameScreen nextScreen, final float duration) {
		if (direction == Direction.LEFT) {
			nextScreen.stage.getRoot().setPosition(-nextScreen.stage.getWidth(), 0);
		} else {
			nextScreen.stage.getRoot().setPosition(nextScreen.stage.getWidth(), 0);
		}
		SequenceAction fadeInAction = Actions.sequence(Actions.fadeOut(0.0001f), Actions.fadeIn(duration));
		MoveToAction moveNextScreenIn = Actions.moveTo(0, 0, duration, Interpolation.exp10Out);
		nextScreen.stage.addAction(Actions.parallel(fadeInAction, moveNextScreenIn));
	}
	private void animateCurrentScreenAndSwitchScreens(Direction direction, final GameScreen currentScreen,
			final GameScreen nextScreen, final float duration) {
		Action currentScreenAnimation;
		if (direction == null) {
			currentScreenAnimation = Actions.fadeOut(duration);
		} else {
			currentScreenAnimation = buildCurrentScreenAnimationWithSliding(direction, currentScreen, nextScreen, duration);
		}
		Action switchScreensAction = buildSwitchScreensAction(nextScreen);
		currentScreen.stage.addAction(Actions.sequence(currentScreenAnimation, switchScreensAction));
	}
	private ParallelAction buildCurrentScreenAnimationWithSliding(Direction direction, final GameScreen currentScreen,
			final GameScreen nextScreen, final float duration) {
		MoveToAction moveCurrentScreenOut;
		if (direction == Direction.LEFT) {
			moveCurrentScreenOut = Actions.moveTo(currentScreen.stage.getWidth(), 0, duration, Interpolation.exp10Out);
		} else {
			moveCurrentScreenOut = Actions.moveTo(-currentScreen.stage.getWidth(), 0, duration, Interpolation.exp10Out);
		}
		AlphaAction fadeOutAction = Actions.fadeOut((float) (duration / 1.5), Interpolation.exp10Out);
		ParallelAction fadeAndMoveOutCurrentScreen = Actions.parallel(moveCurrentScreenOut, fadeOutAction);
		return fadeAndMoveOutCurrentScreen;
	}
	private Action buildSwitchScreensAction(final GameScreen nextScreen) {
		Action switchScreensAction = Actions.run(new Runnable() {
			@Override
			public void run() {
				game.setScreen(nextScreen);
				Assets.unloadSplash();
			}
		});
		return switchScreensAction;
	}
	
	@Override
	public void render(float delta) {
		nextScreen.render(delta);
		currentScreen.render(delta);
	}
}
