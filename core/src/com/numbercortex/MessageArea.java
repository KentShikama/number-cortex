package com.numbercortex;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MessageArea {

	private static final String NEXT_NUMBER = "next_number";

	private static TextButtonStyle textButtonStyle = buildLabelStyle();
	private static TextButtonStyle buildLabelStyle() {
		BitmapFont font = FontGenerator.getMessageFont();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
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

	private static class Singleton {
		private static final MessageArea INSTANCE = new MessageArea();
	}

	private MessageArea() {}

	public static MessageArea createMessageArea(Stage stage) {
		MessageArea instance = Singleton.INSTANCE;
		instance.stage = stage;
		instance.messageLabelLong = buildMessageLabelLong();
		instance.messageLabelShort = buildMessageLabelShort();
		instance.nextNumberSquare = buildNextNumberSquare();
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
	private static NumberTextButton buildNextNumberSquare() {
		NumberTextButton nextNumberSquare = new NumberTextButton("", nextNumberStyle);
		nextNumberSquare.setBounds(475, Launch.SCREEN_HEIGHT - 175, 141, 141);
		nextNumberSquare.setName("16");
		return nextNumberSquare;
	}

	public NumberTextButton getNextNumberSquare() {
		return nextNumberSquare;
	}

	public void updateMessage(String message) {
		messageLabelLong.setText(message);
		messageLabelShort.remove();
		nextNumberSquare.remove();
		stage.addActor(messageLabelLong);
	}

	public void updateMessageWithNextNumber(String message, int nextNumber) {
		Label newLabel = buildNumberLabel();
		nextNumberSquare.setLabel(newLabel);
		nextNumberSquare.setText(String.valueOf(nextNumber));
		messageLabelShort.setText(message);
		messageLabelLong.remove();
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
}
