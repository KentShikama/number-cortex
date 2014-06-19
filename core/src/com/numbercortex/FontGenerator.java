package com.numbercortex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FontGenerator {

	private static BitmapFont numberScrollerFont;
	private static BitmapFont boardNumberFont;
	private static BitmapFont messageFont;

	private static Skin skin = Assets.gameSkin;

	private FontGenerator() {}
	
	public static void load() {
		int numberScrollerFontSize = calculateNumberScrollerFontSize();
		int boardNumberFontSize = calculateBoardNumberFontSize();
		int messageFontSize = (int) (calculateBoardNumberFontSize() * 0.5);
		FreeTypeFontGenerator tahomaGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/Tahoma.ttf"));
		numberScrollerFont = tahomaGenerator
				.generateFont(numberScrollerFontSize);
		boardNumberFont = tahomaGenerator
				.generateFont(boardNumberFontSize);
		numberScrollerFont.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		boardNumberFont.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tahomaGenerator.dispose();
		FreeTypeFontGenerator timesGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/Times.dfont"));
		messageFont = timesGenerator.generateFont(messageFontSize);
		messageFont.getRegion().getTexture()
		.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		messageFont.getLineHeight();
		timesGenerator.dispose();
	}

	private static int calculateNumberScrollerFontSize() {
		TextureRegion scrollerRectangle = skin
				.getRegion(NumberScroller.SCROLLER_RECTANGLE);
		int scrollerRectangleHeight = scrollerRectangle.getRegionHeight();
		int fontHeight = (int) (scrollerRectangleHeight * 0.85);
		return fontHeight;
	}

	private static int calculateBoardNumberFontSize() {
		TextureRegion scrollerRectangle = skin.getRegion("red_rectangle");
		int scrollerRectangleHeight = scrollerRectangle.getRegionHeight();
		int fontSize = (int) (scrollerRectangleHeight * 0.7);
		return fontSize;
	}

	public static BitmapFont getNumberScrollFont() {
		return numberScrollerFont;
	}

	public static BitmapFont getBoardNumberFont() {
		return boardNumberFont;
	}
	
	public static BitmapFont getMessageFont() {
		return messageFont;
	}

}
