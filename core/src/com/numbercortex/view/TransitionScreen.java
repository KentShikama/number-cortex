package com.numbercortex.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.numbercortex.ModeTracker;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;

public class TransitionScreen extends GameScreen {

	private GameScreen currentScreen;
	private GameScreen nextScreen;

	enum Direction {
		LEFT, RIGHT;
	}

	public TransitionScreen(Game game) {
		super(game);
	}

	public void transition(Direction direction, GameScreen nextScreen) {
		transition(direction, nextScreen, 0.35f);
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

		SequenceAction action = Actions.sequence(Actions.fadeOut(0.0001f),
				Actions.fadeIn(duration));
		nextScreen.stage.addAction(action);
		currentScreen.stage.addAction(Actions.sequence(Actions.fadeOut(duration), Actions.run(new Runnable() {
			@Override
			public void run() {
				game.setScreen(nextScreen);
				if (nextScreen instanceof PlayScreen) {
					GameManager gameManager;
					if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
						gameManager = SinglePlayerGameManager.getInstance();
					} else {
						gameManager = TwoPlayerGameManager.getInstance();
					}
					gameManager.resumeGame();
				}
			}
		})));
	}

	@Override
	public void render(float delta) {
		nextScreen.render(delta);
		currentScreen.render(delta);
	}
}
