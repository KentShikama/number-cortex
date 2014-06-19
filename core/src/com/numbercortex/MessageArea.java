package com.numbercortex;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MessageArea {

	private static final String NEXT_NUMBER = "next_number";
	
	private TextButton messageLabelShort;
	private TextButton messageLabelLong;
	private NumberTextButton nextNumberSquare;
	private static Skin skin = Assets.gameSkin;
	private Stage stage;

	public MessageArea(Stage stage) {
		this.stage = stage;
		TextButtonStyle textButtonStyle = buildLabelStyle();
		buildMessageLabelLong(textButtonStyle);
		buildMessageLabelShort(textButtonStyle);
		buildNextNumberSquare(stage);
	}

	private void buildMessageLabelLong(TextButtonStyle textButtonStyle) {
		messageLabelLong = new TextButton("", textButtonStyle);
		messageLabelLong.setBounds(30, Launch.SCREEN_HEIGHT - 175, Launch.SCREEN_WIDTH - 30 * 2, 145);
		messageLabelLong.getLabel().setWrap(true);
	}
	
	private void buildMessageLabelShort(TextButtonStyle textButtonStyle) {
		messageLabelShort = new TextButton("", textButtonStyle);
		messageLabelShort.setBounds(30, Launch.SCREEN_HEIGHT - 175, Launch.SCREEN_WIDTH - 30 * 2 - 165, 145);
		messageLabelShort.getLabel().setWrap(true);
	}
	
	private static TextButtonStyle buildLabelStyle() {
		BitmapFont font = FontGenerator.getMessageFont();
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		return textButtonStyle;
	}
	
	private void buildNextNumberSquare(Stage stage) {
		NumberTextButton.NumberTextButtonStyle nextNumberStyle = buildButtonStyle(NEXT_NUMBER);
		nextNumberSquare = new NumberTextButton("", nextNumberStyle);
		nextNumberSquare.setBounds(475, Launch.SCREEN_HEIGHT - 175, 141, 141);
		nextNumberSquare.setName("16");
	}
	
	private static NumberTextButton.NumberTextButtonStyle buildButtonStyle(String textureName) {
		BitmapFont font = FontGenerator.getBoardNumberFont();
		Drawable numberRectangle = skin.getDrawable(textureName);
		NumberTextButton.NumberTextButtonStyle buttonStyle = new NumberTextButton.NumberTextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = new Color(250f/255f, 235f/255f, 102f/255f, 1);
		buttonStyle.up = numberRectangle;
		return buttonStyle;
	}
	
	public NumberTextButton getNextNumberSquare() {
		return nextNumberSquare;
	}
	
	public void updateMessage(String message) {
		messageLabelLong.setText(message);
		messageLabelShort.remove();
		nextNumberSquare.remove();
		BitmapFont font = FontGenerator.getBoardNumberFont();
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = new Color(250f/255f, 235f/255f, 102f/255f, 1);
		Label label = new Label("", labelStyle);
		nextNumberSquare.setLabel(label);
		stage.addActor(messageLabelLong);
	}
	
	public void updateMessageWithNextNumber(String message, int nextNumber) {
		messageLabelShort.setText(message);
		nextNumberSquare.setText(String.valueOf(nextNumber));
		messageLabelLong.remove();
		stage.addActor(messageLabelShort);
		stage.addActor(nextNumberSquare);
	}

}
