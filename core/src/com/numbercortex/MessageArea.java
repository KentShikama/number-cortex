package com.numbercortex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MessageArea {

	private static final String NEXT_NUMBER = "next_number";

	private static TextButtonStyle textButtonStyle = buildLabelStyle();
	private static TextButtonStyle buildLabelStyle() {
		BitmapFont font = FontGenerator.getGillSans60();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		return textButtonStyle;
	}

	private static TextButton.TextButtonStyle borderedTextButtonStyle = buildTextButtonStyle();
	private static TextButton.TextButtonStyle buildTextButtonStyle() {
		BitmapFont font = FontGenerator.getGillSans60();
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.up = Assets.dialogSkin.getDrawable("white_button");
		return textButtonStyle;
	}

	private static NumberTextButton.NumberTextButtonStyle nextNumberStyle = buildButtonStyle(NEXT_NUMBER);
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

	public static MessageArea createMessageArea(Stage stage, Game game) {
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
			nextNumberStyle = buildButtonStyle(NEXT_NUMBER);
		}
		NumberTextButton nextNumberSquare = new NumberTextButton("", nextNumberStyle);
		nextNumberSquare.setBounds(475, Launch.SCREEN_HEIGHT - 175, 141, 141);
		nextNumberSquare.setName("16");
		return nextNumberSquare;
	}
	private static TextButton buildMenuButton(final Game game) {
		if (borderedTextButtonStyle == null) {
			borderedTextButtonStyle = buildTextButtonStyle();
		}
		TextButton menuButton = new TextButton("Menu", borderedTextButtonStyle);
		menuButton.addListener(new ClickListener() {
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
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
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
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenTracker.level = ScreenTracker.level + 1;
				game.setScreen(ScreenTracker.singlePlayerSettingsScreen);
			}
		});
		return continueButton;
	}

	public NumberTextButton getNextNumberSquare() {
		return nextNumberSquare;
	}

	public void updateMessage(String message) {
		messageLabelLong.setText(message);
		messageLabelShort.remove();
		nextNumberSquare.remove();
		buttonTable.remove();
		stage.addActor(messageLabelLong);
	}

	public void updateMessageWithNextNumber(String message, int nextNumber) {
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

	public void showEndingMessageSequence(final String winner, final String winningAttribute, float delay) {
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
					updateMessage("Tie game!");
				} else {
					updateMessage(winner + " wins!\n" + "(" + winningAttribute + ")");
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
				if (winner.equals("Player") && ScreenTracker.level != 18) {
					updateMessageWithButtons("continue");
				} else {
					updateMessageWithButtons("replay");
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
		if (message.equals("replay")) {
			messageLabelLong.setText("Do you wish to play again?");
			buttonTable.add(playButton).pad(20).padTop(50);
		} else {
			messageLabelLong.setText("Do you wish to continue to the next level?");
			buttonTable.add(continueButton).pad(20).padTop(50);
		}
	}

	public static void dispose() {
		borderedTextButtonStyle = null;
		nextNumberStyle = null;
	}
}
