package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NumberCortexBoard {

	private static final int SQUARE_LENGTH = Launch.SCREEN_WIDTH / 4;
	private static final int NUMBER_OF_ROWS = 4;

	private static Skin skin = Assets.gameSkin;
	private static NumberTextButton.NumberTextButtonStyle greenRectangleStyle = buildButtonStyle("green_rectangle");
	private static NumberTextButton.NumberTextButtonStyle blueRectangleStyle = buildButtonStyle("blue_rectangle");
	private static NumberTextButton.NumberTextButtonStyle redRectangleStyle = buildButtonStyle("red_rectangle");
	private static NumberTextButton.NumberTextButtonStyle buildButtonStyle(String textureName) {
		BitmapFont font = FontGenerator.getBoardNumberFont();
		Drawable numberRectangle = skin.getDrawable(textureName);
		Drawable numberRectangleChecked = skin.getDrawable(textureName + "_checked");
		NumberTextButton.NumberTextButtonStyle buttonStyle = new NumberTextButton.NumberTextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
		buttonStyle.up = numberRectangle;
		buttonStyle.highlighted = numberRectangleChecked;
		return buttonStyle;
	}

	private ArrayList<NumberTextButton> cells;
	private boolean isBlue;

	private static class Singleton {
		private static NumberCortexBoard INSTANCE = new NumberCortexBoard();
	}
	private NumberCortexBoard() {
		this.isBlue = true;
		this.cells = buildCells(this.isBlue);
	}

	public static NumberCortexBoard createNumberCortexBoard(Stage stage, CortexPreferences preferences) {
		NumberCortexBoard instance = Singleton.INSTANCE;
		if (instance.isBlue != preferences.isBlue()) {
			instance.isBlue = preferences.isBlue();
			instance.cells = buildCells(instance.isBlue);
		}
		addCellsToStageIfAbsent(stage, instance);
		return instance;
	}
	private static ArrayList<NumberTextButton> buildCells(boolean isBlue) {
		ArrayList<NumberTextButton> cells = new ArrayList<NumberTextButton>();
		for (int i = 0; i < 16; i++) {
			int left = (i % NUMBER_OF_ROWS) * SQUARE_LENGTH;
			int bottom = ((NUMBER_OF_ROWS - 1) - i / NUMBER_OF_ROWS) * SQUARE_LENGTH;
			NumberTextButton rectangle;
			if (isGreen(i)) {
				rectangle = new NumberTextButton("", greenRectangleStyle);
			} else {
				if (isBlue) {
					rectangle = new NumberTextButton("", blueRectangleStyle);
				} else {
					rectangle = new NumberTextButton("", redRectangleStyle);
				}
			}
			rectangle.setName(String.valueOf(i));
			rectangle.setBounds(left, bottom + (Launch.SCREEN_HEIGHT - 850), SQUARE_LENGTH, SQUARE_LENGTH);
			cells.add(rectangle);
		}
		return cells;
	}
	private static boolean isGreen(int i) {
		int[] greenList = { 0, 2, 5, 7, 8, 10, 13, 15 };
		for (int green : greenList) {
			if (green == i) {
				return true;
			}
		}
		return false;
	}
	private static void addCellsToStageIfAbsent(Stage stage, NumberCortexBoard instance) {
		for (NumberTextButton cell : instance.cells) {
			for (Actor actor : stage.getActors()) {
				if (actor == cell) {
					continue;
				}
				cell.setText("");
				stage.addActor(cell);
			}
		}
	}

	public void updateCell(int coordinate, int number) {
		NumberTextButton cell = cells.get(coordinate);
		cell.setText(String.valueOf(number));
	}
	public void clearCell(int coordinate) {
		NumberTextButton cell = cells.get(coordinate);
		cell.setText("");
	}

	public ArrayList<NumberTextButton> getBoardCells() {
		return cells;
	}

	public void showWinningCoordinates(int[] winningCoordinates) {
		for (Integer winningCoordinate : winningCoordinates) {
			NumberTextButton cell = cells.get(winningCoordinate);
			cell.setHighlighted(true);
		}
	}
}