package com.numbercortex;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NumberScroller {

	private static final String NUMBER_RECTANGLE_BACKGROUND = "number_rectangle_background";
	private static final String SCROLLER_RECTANGLE = "scroller_rectangle";

	private static ScrollPane.ScrollPaneStyle style = buildScrollPaneStyle();
	private static ScrollPane.ScrollPaneStyle buildScrollPaneStyle() {
		ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
		style.background = Assets.gameSkin.getDrawable(SCROLLER_RECTANGLE);
		return style;
	}

	private static NumberTextButton.NumberTextButtonStyle buttonStyle = buildButtonStyle();
	private static NumberTextButton.NumberTextButtonStyle buildButtonStyle() {
		BitmapFont font = FontGenerator.getNumberScrollFont();
		Drawable numberRectangle = Assets.gameSkin.getDrawable(NUMBER_RECTANGLE_BACKGROUND);
		buttonStyle = new NumberTextButton.NumberTextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = Launch.BRIGHT_YELLOW;
		buttonStyle.up = numberRectangle;
		return buttonStyle;
	}

	private ScrollPane numberScroller;
	private NumberScrollerArrows arrows;

	private Sendable messenger;
	private Table numberTable = new Table();
	private NumberButtonListener listener = new NumberButtonListener();
	private class NumberButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			int number = getClickedNumber(event);
			messenger.chooseNumber(null, number);
		}

		private int getClickedNumber(InputEvent event) {
			Label label = (Label) event.getTarget();
			String numberString = label.getText().toString();
			int number = Integer.valueOf(numberString);
			return number;
		}
	}

	private static class Singleton {
		private static final NumberScroller INSTANCE = new NumberScroller();
	}
	private NumberScroller() {}

	public static NumberScroller createNumberScroller(Stage stage) {
		NumberScroller numberScrollerInstance = Singleton.INSTANCE;
		if (style == null) {
			style = buildScrollPaneStyle();
		}
		numberScrollerInstance.numberScroller = buildNumberScroller(numberScrollerInstance.numberTable, style);
		numberScrollerInstance.arrows = new NumberScrollerArrows(numberScrollerInstance.numberScroller);
		stage.addActor(numberScrollerInstance.numberScroller);
		numberScrollerInstance.arrows.addArrows(stage);
		return numberScrollerInstance;
	}
	/**
	 * The width of the numberScroller is enlarged by 1 pixel to prevent the background color from showing through
	 */
	private static ScrollPane buildNumberScroller(Table numberTable, ScrollPane.ScrollPaneStyle style) {
		ScrollPane numberScroller = new ScrollPane(numberTable, style);
		int scrollerRectangleWidth = Assets.gameSkin.getRegion(SCROLLER_RECTANGLE).getRegionWidth();
		int scrollerRectangleHeight = Assets.gameSkin.getRegion(SCROLLER_RECTANGLE).getRegionHeight();
		numberScroller.setBounds(101 - 1, Launch.SCREEN_HEIGHT - 1013, scrollerRectangleWidth + 2,
				scrollerRectangleHeight);
		numberScroller.setOverscroll(true, false);
		return numberScroller;
	}

	public void setSendable(Sendable messenger) {
		this.messenger = messenger;
	}

	public void removeScroller() {
		for (Actor numberButton : numberTable.getChildren()) {
			numberButton.clearListeners();
		}
		AnimationUtilities.delayFadeAndRemoveActor(numberScroller);
		arrows.remove();
	}

	public void update(ArrayList<Integer> numberList) {
		numberTable.clearChildren();
		if (buttonStyle == null) {
			buttonStyle = buildButtonStyle();
		}
		for (Integer number : numberList) {
			NumberTextButton button = new NumberTextButton(number.toString(), buttonStyle);
			button.addListener(listener);
			numberTable.add(button);
		}
	}

	public static void dispose() {
		style = null;
		buttonStyle = null;
	}
}
