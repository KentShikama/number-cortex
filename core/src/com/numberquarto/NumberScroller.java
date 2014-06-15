package com.numberquarto;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NumberScroller {
	Table numberTable;
	TextButton.TextButtonStyle buttonStyle;
	private Skin skin;
	
	NumberScroller (Stage stage, Skin skin) {		
		this.skin = skin;
		
		numberTable = new Table();	
		ScrollPane.ScrollPaneStyle style = buildScrollPaneStyle();
		ScrollPane numberScroller = buildNumberScroller(style);
		stage.addActor(numberScroller);

		BitmapFont font = createFont();
		buildButtonStyle(font);
		
		buildArrows(stage);
	}

	private ScrollPane.ScrollPaneStyle buildScrollPaneStyle() {
		ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
		style.background = skin.getDrawable("scroller_rectangle");
		return style;
	}

	private ScrollPane buildNumberScroller(ScrollPane.ScrollPaneStyle style) {
		ScrollPane numberScroller = new ScrollPane(numberTable, style);
		int SCROLLER_RECTANGLE_WIDTH = skin.getRegion("scroller_rectangle").getRegionWidth();
		int SCROLLER_RECTANGLE_HEIGHT = skin.getRegion("scroller_rectangle").getRegionHeight();
		numberScroller.setBounds(101 - 1, Launch.SCREEN_HEIGHT - 1013, SCROLLER_RECTANGLE_WIDTH + 2, SCROLLER_RECTANGLE_HEIGHT);
		numberScroller.setOverscroll(true, false);
		return numberScroller;
	}

	private void buildButtonStyle(BitmapFont font) {
		Drawable numberRectangle = skin.getDrawable("number_rectangle_background");
		buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.fontColor = new Color(250f/255f, 235f/255f, 102f/255f, 1);
		buttonStyle.up = numberRectangle;
	}

	private BitmapFont createFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Tahoma.ttf"));
		int fontSize = calculateFontSize();
		BitmapFont font = generator.generateFont(fontSize);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		generator.dispose();
		return font;
	}

	private int calculateFontSize() {
		TextureRegion scrollerRectangle = skin.getRegion("scroller_rectangle");
		int scrollerRectangleHeight = scrollerRectangle.getRegionHeight();
		int fontHeight = (int) (scrollerRectangleHeight * 0.85);
		return fontHeight;
	}
	
	private void buildArrows(Stage stage) {
		Drawable leftArrowRectangleSkin = skin.getDrawable("left_arrow");
		Drawable rightArrowRectangleSkin = skin.getDrawable("right_arrow");
		
		int arrowRectangleTextureWidth = skin.getRegion("left_arrow").getRegionWidth();
		int arrowRectangleTextureHeight = skin.getRegion("left_arrow").getRegionHeight();

		ImageButton leftArrowRectangle = new ImageButton(leftArrowRectangleSkin);
		leftArrowRectangle.setBounds(0, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth, arrowRectangleTextureHeight);
		ImageButton rightArrowRectangle = new ImageButton(rightArrowRectangleSkin);
		rightArrowRectangle.setBounds(539, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth, arrowRectangleTextureHeight);
		stage.addActor(leftArrowRectangle);
		stage.addActor(rightArrowRectangle);
	}
	
	public void update (ArrayList<Integer> numberList) {
		numberTable.clearChildren();
		for (Integer number : numberList) {
			TextButton button = new TextButton(number.toString(), buttonStyle);
			numberTable.add(button);
		}
	}
}
