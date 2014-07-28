package com.numbercortex.view;

import java.util.ArrayList;

import libgdx.NumberTextButton;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.numbercortex.logic.InteractableSendable;

class NumberScroller {

	private static final String NUMBER_RECTANGLE_BACKGROUND = "number_rectangle_background";
	private static final String SCROLLER_RECTANGLE = "scroller_rectangle";

	private static ScrollPane.ScrollPaneStyle style = buildScrollPaneStyle();
	private static ScrollPane.ScrollPaneStyle buildScrollPaneStyle() {
		ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
		style.background = Assets.gameSkin.getDrawable(SCROLLER_RECTANGLE);
		return style;
	}

	private static NumberTextButton.NumberTextButtonStyle numberTextButtonStyle = buildButtonStyle();
	private static NumberTextButton.NumberTextButtonStyle buildButtonStyle() {
		BitmapFont font = FontGenerator.getNumberScrollerFont();
		Drawable numberRectangle = Assets.gameSkin.getDrawable(NUMBER_RECTANGLE_BACKGROUND);
		NumberTextButton.NumberTextButtonStyle numberTextButtonStyle = new NumberTextButton.NumberTextButtonStyle();
		numberTextButtonStyle.font = font;
		numberTextButtonStyle.fontColor = new Color(Launch.BRIGHT_YELLOW);
		numberTextButtonStyle.up = numberRectangle;
		return numberTextButtonStyle;
	}

	private ScrollPane numberScroller;
	private NumberScrollerArrows arrows;

	private InteractableSendable messenger;
	private Table numberTable = new Table();
	private NumberButtonListener listener = new NumberButtonListener();
	private class NumberButtonListener extends ClickListener {

		@Override
		public void clicked(final InputEvent event, float x, float y) {
			if (messenger == null) {
				return;
			}
			int tapCount = this.getTapCount();
			final int number = getClickedNumber(event);
			if (tapCount >= 2) {
				Timer.instance().clear();
				messenger.chooseNumber(null, number);
			} else {
				Timer.schedule(new Task() {
					@Override
					public void run() {
						messenger.handleConfirmedSingleTap(number);
					}
				}, 0.4f);
			}
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

	static NumberScroller createNumberScroller(Stage stage) {
		float worldWidth = stage.getViewport().getWorldWidth();
		NumberScroller numberScrollerInstance = Singleton.INSTANCE;
		if (style == null) {
			style = buildScrollPaneStyle();
		}
		numberScrollerInstance.numberScroller = buildNumberScroller(numberScrollerInstance.numberTable, style,
				worldWidth);
		numberScrollerInstance.arrows = new NumberScrollerArrows(numberScrollerInstance.numberScroller, worldWidth);
		stage.addActor(numberScrollerInstance.numberScroller);
		numberScrollerInstance.arrows.addArrows(stage);
		return numberScrollerInstance;
	}
	/**
	 * The width of the numberScroller is enlarged by 1 pixel to prevent the background color from showing through
	 */
	private static ScrollPane buildNumberScroller(Table numberTable, ScrollPane.ScrollPaneStyle style, float worldWidth) {
		ScrollPane numberScroller = new ScrollPane(numberTable, style);
		float scrollerRectangleWidth = worldWidth - 200;
		float scrollerRectangleHeight = Assets.gameSkin.getRegion(SCROLLER_RECTANGLE).getRegionHeight();
		numberScroller.setBounds(101 - 1, Launch.SCREEN_HEIGHT - 1013, scrollerRectangleWidth + 2,
				scrollerRectangleHeight);
		numberScroller.setOverscroll(true, false);
		return numberScroller;
	}

	void setSendable(InteractableSendable messenger) {
		this.messenger = messenger;
		if (messenger == null) {
			numberScroller.setTouchable(Touchable.disabled);
			arrows.setDisabled(true);
		} else {
			numberScroller.setTouchable(Touchable.enabled);
			arrows.setDisabled(false);
		}
	}

	void removeScroller() {
		numberScroller.remove();
		arrows.remove();
	}
	void removeScrollerWithAnimation(float delay) {
		for (Actor numberButton : numberTable.getChildren()) {
			numberButton.clearListeners();
		}
		AnimationUtilities.delayFadeAndRemoveActor(numberScroller, delay);
		arrows.removeWithAnimation(delay);
	}

	void update(ArrayList<Integer> numberList) {
		numberTable.clearChildren();
		if (numberTextButtonStyle == null) {
			numberTextButtonStyle = buildButtonStyle();
		}
		for (Integer number : numberList) {
			NumberTextButton button = new NumberTextButton(number.toString(), buildButtonStyle());
			button.setName(String.valueOf(number));
			button.addListener(listener);
			numberTable.add(button);
		}
	}

	void chooseNumberWithAnimation(int nextNumber, Action completeChooseNumberAction) {
		NumberTextButton nextNumberCell = (NumberTextButton) numberScroller.findActor(String.valueOf(nextNumber));
		int nextNumberCellPosition = numberTable.getCell(nextNumberCell).getColumn();
		int nextNumberCellCoordinate = nextNumberCellPosition * 125;
		int numberScrollerCenterCoordinate = (int) (numberScroller.getWidth() / 2);
		numberScroller.setScrollX(nextNumberCellCoordinate - numberScrollerCenterCoordinate + 63);

		DelayAction delayAction = new DelayAction(1.2f);
		SequenceAction sequence = Actions.sequence(delayAction, completeChooseNumberAction);
		numberScroller.addAction(sequence);
	}

	static void dispose() {
		style = null;
		numberTextButtonStyle = null;
	}
}
