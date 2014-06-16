package com.numberquarto;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NumberScroller {
	
	private static final String RIGHT_ARROW = "right_arrow";
	private static final String LEFT_ARROW = "left_arrow";
	private static final String NUMBER_RECTANGLE_BACKGROUND = "number_rectangle_background";
	static final String SCROLLER_RECTANGLE = "scroller_rectangle";
	
	private static TextButton.TextButtonStyle buttonStyle;
	private static Skin skin = Assets.gameSkin;
	
	ScrollPane numberScroller;
	
	NumberScroller (Stage stage) {				
		Table numberTable = new Table();	
		ScrollPane.ScrollPaneStyle style = buildScrollPaneStyle();
		numberScroller = buildNumberScroller(numberTable, style);
		stage.addActor(numberScroller);
		
		buildButtonStyle();
		buildArrows(stage);
	}

	private ScrollPane.ScrollPaneStyle buildScrollPaneStyle() {
		ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
		style.background = skin.getDrawable(SCROLLER_RECTANGLE);
		return style;
	}

	/**
	 * The width of the numberScroller is enlarged by 1 pixel
	 * to prevent the background color from showing through
	 */
	private ScrollPane buildNumberScroller(Table numberTable, ScrollPane.ScrollPaneStyle style) {
		ScrollPane numberScroller = new ScrollPane(numberTable, style);
		int scrollerRectangleWdith = skin.getRegion(SCROLLER_RECTANGLE).getRegionWidth();
		int scrollerRectangleHeight = skin.getRegion(SCROLLER_RECTANGLE).getRegionHeight();
		numberScroller.setBounds(101 - 1, Launch.SCREEN_HEIGHT - 1013, scrollerRectangleWdith + 2, scrollerRectangleHeight);
		numberScroller.setOverscroll(true, false);
		return numberScroller;
	}

	private static TextButton.TextButtonStyle buildButtonStyle() {
		BitmapFont font = FontGenerator.getNumberScrollFont();
		Drawable numberRectangle = skin.getDrawable(NUMBER_RECTANGLE_BACKGROUND);
		buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = new Color(250f/255f, 235f/255f, 102f/255f, 1);
		buttonStyle.up = numberRectangle;
		return buttonStyle;
	}
	
	private void buildArrows(Stage stage) {
		Drawable leftArrowRectangleSkin = skin.getDrawable(LEFT_ARROW);
		Drawable rightArrowRectangleSkin = skin.getDrawable(RIGHT_ARROW);
		
		int arrowRectangleTextureWidth = skin.getRegion(LEFT_ARROW).getRegionWidth();
		int arrowRectangleTextureHeight = skin.getRegion(LEFT_ARROW).getRegionHeight();

		ImageButton leftArrowButton = new ImageButton(leftArrowRectangleSkin);
		leftArrowButton.setBounds(0, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth, arrowRectangleTextureHeight);
		leftArrowButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				numberScroller.fling(1, 750, 0);
				return true;
			}
		});
		ImageButton rightArrowButton = new ImageButton(rightArrowRectangleSkin);
		rightArrowButton.setBounds(539, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth, arrowRectangleTextureHeight);
		rightArrowButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				numberScroller.fling(1, -750, 0);
				return true;
			}
		});
		stage.addActor(leftArrowButton);
		stage.addActor(rightArrowButton);
	}
	
	public void update (ArrayList<Integer> numberList) {
		Table numberTable = (Table) numberScroller.getChildren().get(0);
		numberTable.clearChildren();
		NumberButtonListener listener = new NumberButtonListener();
		for (Integer number : numberList) {
			TextButton button = new TextButton(number.toString(), buttonStyle);
			button.addListener(listener);
			numberTable.add(button);
		}
	}
	
	class NumberButtonListener extends ClickListener {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			int number = getClickedNumber(event);
			System.out.println("The number " + number + " was clicked.");
		}

		private int getClickedNumber(InputEvent event) {
			Label label = (Label) event.getTarget();
			String numberString = label.getText().toString();
			int number = Integer.valueOf(numberString);
			return number;
		}
	}
}
