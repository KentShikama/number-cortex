package com.numberquarto;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NumberQuartoBoard {

	private static final int SQUARE_LENGTH = Launch.SCREEN_WIDTH/4;
	private static final int NUMBER_OF_ROWS = 4;
	
	private TextButton.TextButtonStyle greenRectangleStyle = buildButtonStyle("green_rectangle");
	private TextButton.TextButtonStyle blueRectangleStyle = buildButtonStyle("blue_rectangle");
	private TextButton.TextButtonStyle redRectangleStyle = buildButtonStyle("red_rectangle");
	
	private static Skin skin = Assets.gameSkin;

	public NumberQuartoBoard(Stage stage, boolean isBlue) {
		for (int i = 0; i < 16; i++) {
			int left = (i % NUMBER_OF_ROWS) * SQUARE_LENGTH;
			int bottom = ((NUMBER_OF_ROWS - 1) - (int)(i/NUMBER_OF_ROWS)) * SQUARE_LENGTH;
			TextButton rectangle;
			if (isGreen(i)) {
				rectangle = new TextButton("", greenRectangleStyle);
			} else {
				if (isBlue) {
					rectangle = new TextButton("", blueRectangleStyle);				
				} else {
					rectangle = new TextButton("", redRectangleStyle);				
				}
			}
			rectangle.setName(String.valueOf(i));
			rectangle.setBounds(left, bottom + (Launch.SCREEN_HEIGHT - 850), SQUARE_LENGTH, SQUARE_LENGTH);
			stage.addActor(rectangle);
		}
	}
	
	private TextButton.TextButtonStyle buildButtonStyle(String textureName) {
		BitmapFont font = FontGenerator.getBoardNumberFont();
		Drawable numberRectangle = skin.getDrawable(textureName);
		TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = new Color(250f/255f, 235f/255f, 102f/255f, 1);
		buttonStyle.up = numberRectangle;
		return buttonStyle;
	}

	private boolean isGreen(int i) {
		int[] greenList = {0, 2, 5, 7, 8, 10, 13, 15};
		for (int green : greenList) {
			if (green == i) {
				return true;
			}
		}
		return false;
	}
	
}