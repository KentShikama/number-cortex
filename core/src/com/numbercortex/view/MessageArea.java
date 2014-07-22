package com.numbercortex.view;

import libgdx.NumberTextButton;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;

class MessageArea {

	static final String TAG = "Message Area";
	static final String NEXT_NUMBER_SQUARE_NAME = "16";

	private static final String CONTINUE = "continue";
	private static final String REPLAY = "replay";

	private static final int MAXIMUM_POSSIBLE_LEVEL = 18;

	private static TextButtonStyle textButtonStyle = buildLabelStyle();
	private static TextButtonStyle buildLabelStyle() {
		BitmapFont font = FontGenerator.getGillSans57();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		return textButtonStyle;
	}

	private static final String TEXT_BUTTON_BORDER_TEXTURE_NAME = "white_button";
	private static TextButton.TextButtonStyle borderedTextButtonStyle = buildTextButtonStyle();
	private static TextButton.TextButtonStyle buildTextButtonStyle() {
		BitmapFont font = FontGenerator.getGillSans57();
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.up = Assets.dialogSkin.getDrawable(TEXT_BUTTON_BORDER_TEXTURE_NAME);
		return textButtonStyle;
	}

	private static final String NEXT_NUMBER_TEXTURE_NAME = "next_number";
	private static NumberTextButton.NumberTextButtonStyle nextNumberStyle = buildButtonStyle(NEXT_NUMBER_TEXTURE_NAME);
	private static NumberTextButton.NumberTextButtonStyle buildButtonStyle(String textureName) {
		BitmapFont font = FontGenerator.getBoardNumberFont();
		Drawable numberRectangle = Assets.gameSkin.getDrawable(textureName);
		NumberTextButton.NumberTextButtonStyle buttonStyle = new NumberTextButton.NumberTextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
		buttonStyle.up = numberRectangle;
		return buttonStyle;
	}

	private Stage stage;
	private TextButton messageLabelShort;
	private TextButton messageLabelLong;
	private NumberTextButton nextNumberSquare;

	private Table buttonTable = new Table();
	private TextButton menuButton;
	private TextButton playButton;
	private TextButton continueButton;

	private static class Singleton {
		private static final MessageArea INSTANCE = new MessageArea();
	}

	private MessageArea() {}

	static MessageArea createMessageArea(Stage stage, Game game) {
		MessageArea instance = Singleton.INSTANCE;
		instance.stage = stage;
		instance.messageLabelLong = buildMessageLabelLong();
		instance.messageLabelShort = buildMessageLabelShort();
		instance.nextNumberSquare = buildNextNumberSquare();
		instance.buttonTable = buildButtonTable();
		instance.menuButton = buildMenuButton(game);
		instance.playButton = buildPlayButton(game);
		instance.continueButton = buildContinueButton(game);
		return instance;
	}
	private static TextButton buildMessageLabelLong() {
		TextButton messageLabelLong = new TextButton("", textButtonStyle);
		messageLabelLong.setBounds(30, Launch.SCREEN_HEIGHT - 175, Launch.SCREEN_WIDTH - 30 * 2, 145);
		messageLabelLong.getLabel().setWrap(true);
		return messageLabelLong;
	}
	private static TextButton buildMessageLabelShort() {
		TextButton messageLabelShort = new TextButton("", textButtonStyle);
		messageLabelShort.setBounds(30, Launch.SCREEN_HEIGHT - 175, Launch.SCREEN_WIDTH - 30 * 2 - 165, 145);
		messageLabelShort.getLabel().setWrap(true);
		return messageLabelShort;
	}
	private static Table buildButtonTable() {
		Table buttonTable = new Table();
		buttonTable.setBounds(30, Launch.SCREEN_HEIGHT - 370, Launch.SCREEN_WIDTH - 30 * 2, 145);
		return buttonTable;
	}
	private static NumberTextButton buildNextNumberSquare() {
		if (nextNumberStyle == null) {
			nextNumberStyle = buildButtonStyle(NEXT_NUMBER_TEXTURE_NAME);
		}
		NumberTextButton nextNumberSquare = new NumberTextButton("", nextNumberStyle);
		nextNumberSquare.setBounds(475, Launch.SCREEN_HEIGHT - 175, 141, 141);
		nextNumberSquare.setName(NEXT_NUMBER_SQUARE_NAME);
		return nextNumberSquare;
	}
	private static TextButton buildMenuButton(final Game game) {
		if (borderedTextButtonStyle == null) {
			borderedTextButtonStyle = buildTextButtonStyle();
		}
		TextButton menuButton = new TextButton("Menu", borderedTextButtonStyle);
		menuButton.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(ScreenTracker.titleScreen);
			}
		});
		return menuButton;
	}
	private static TextButton buildPlayButton(final Game game) {
		if (borderedTextButtonStyle == null) {
			borderedTextButtonStyle = buildTextButtonStyle();
		}
		TextButton playButton = new TextButton("Play", borderedTextButtonStyle);
		playButton.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
					game.setScreen(ScreenTracker.singlePlayerSettingsScreen);
				} else {
					game.setScreen(ScreenTracker.twoPlayerSettingsScreen);
				}
			}
		});
		return playButton;
	}
	private static TextButton buildContinueButton(final Game game) {
		if (borderedTextButtonStyle == null) {
			borderedTextButtonStyle = buildTextButtonStyle();
		}
		TextButton continueButton = new TextButton("Continue", borderedTextButtonStyle);
		continueButton.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Persistence persistence = Persistence.getInstance();
				int currentLevel = persistence.getCurrentLevel();
				int nextLevel = currentLevel + 1;
				persistence.setCurrentLevel(nextLevel);
				game.setScreen(ScreenTracker.singlePlayerSettingsScreen);
			}
		});
		return continueButton;
	}

	NumberTextButton getNextNumberSquare() {
		return nextNumberSquare;
	}

	void updateMessage(String message) {
		messageLabelLong.setText(message);
		messageLabelShort.remove();
		nextNumberSquare.remove();
		buttonTable.remove();
		stage.addActor(messageLabelLong);
	}

	void updateMessageWithNextNumber(String message, int nextNumber) {
		Label newLabel = buildNumberLabel();
		nextNumberSquare.setLabel(newLabel);
		nextNumberSquare.setText(String.valueOf(nextNumber));
		messageLabelShort.setText(message);
		messageLabelLong.remove();
		buttonTable.remove();
		stage.addActor(messageLabelShort);
		stage.addActor(nextNumberSquare);
	}
	private static Label buildNumberLabel() {
		BitmapFont font = FontGenerator.getBoardNumberFont();
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Launch.BRIGHT_YELLOW;
		Label label = new Label("", labelStyle);
		return label;
	}

	void flashChosenNumber(int chosenNumber) {
		RepeatAction repeatAction = buildRepeatableFlashingAction(chosenNumber);
		nextNumberSquare.addAction(repeatAction);
	}
	private RepeatAction buildRepeatableFlashingAction(int chosenNumber) {
		DelayAction delayAction = Actions.delay(0.5f);
		Action toggleAction = buildToggleAction(chosenNumber);
		SequenceAction sequence = Actions.sequence(toggleAction, delayAction);
		RepeatAction repeatAction = new RepeatAction();
		repeatAction.setAction(sequence);
		repeatAction.setCount(4);
		return repeatAction;
	}
	private Action buildToggleAction(final int chosenNumber) {
		Action toggleAction = new Action() {
			@Override
			public boolean act(float delta) {
				toggleCell();
				return true;
			}
			private void toggleCell() {
				if (nextNumberSquare.isHighlighted()) {
					nextNumberSquare.setText(String.valueOf(chosenNumber));
					nextNumberSquare.setHighlighted(false);
				} else {
					nextNumberSquare.setText("");
					nextNumberSquare.setHighlighted(true);
				}
			}
		};
		return toggleAction;
	}

	void showEndingMessageSequence(final String winner, final String winningAttribute, float delay) {
		Action showWinner = buildShowWinnerAction(winner, winningAttribute);
		DelayAction delayAction = Actions.delay(delay);
		Action showNextOptions = buildShowNextOptionsAction(winner);
		stage.addAction(Actions.sequence(showWinner, delayAction, showNextOptions));
	}
	private Action buildShowWinnerAction(final String winner, final String winningAttribute) {
		Action showWinner = new Action() {
			@Override
			public boolean act(float delta) {
				if (winner == null) {
					String tieMessage = "Tie game!";
					updateMessage(tieMessage);
				} else {
					String winningMessage = winner + " wins!\n" + "(" + winningAttribute + ")";
					updateMessage(winningMessage);
				}
				return true;
			}
		};
		return showWinner;
	}
	private Action buildShowNextOptionsAction(final String winner) {
		Action showNextOptions = new Action() {
			@Override
			public boolean act(float delta) {
				if (winner != null && winner.equals("Player")
						&& Persistence.getInstance().getCurrentLevel() != MAXIMUM_POSSIBLE_LEVEL) {
					updateMessageWithButtons(CONTINUE);
				} else {
					updateMessageWithButtons(REPLAY);
				}
				return true;
			}
		};
		return showNextOptions;
	}
	private void updateMessageWithButtons(String message) {
		messageLabelLong.setBounds(30, Launch.SCREEN_HEIGHT - 225, Launch.SCREEN_WIDTH - 30 * 2, 145);
		buttonTable.clear();
		buttonTable.add(menuButton).pad(20).padTop(50);
		stage.addActor(buttonTable);
		if (message.equals(REPLAY)) {
			messageLabelLong.setText("Do you wish to play again?");
			buttonTable.add(playButton).width(300).pad(15).padTop(50);
		} else if (message.equals(CONTINUE)) {
			messageLabelLong.setText("Do you wish to continue to the next level?");
			buttonTable.add(continueButton).width(300).pad(15).padTop(50);
		} else {
			Gdx.app.log(TAG, "Impossible update message.");
		}
	}

	static void dispose() {
		borderedTextButtonStyle = null;
		nextNumberStyle = null;
	}
}
