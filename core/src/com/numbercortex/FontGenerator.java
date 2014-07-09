package com.numbercortex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FontGenerator {

	private static BitmapFont numberScrollerFont;
	private static BitmapFont boardNumberFont;
	private static BitmapFont messageFont;
	private static BitmapFont levelFont;
	private static BitmapFont dialogFont;

	private static Skin skin = Assets.gameSkin;

	private static final String SCROLLER_RECTANGLE = "scroller_rectangle";

	private FontGenerator() {}

	public static boolean isNull() {
		if (numberScrollerFont == null || boardNumberFont == null || messageFont == null || levelFont == null || dialogFont == null) {
			return true;
		} else {
			return false;
		}
	}

	public static BitmapFont getBoardNumberFont() {
		return boardNumberFont;
	}
	public static BitmapFont getMessageFont() {
		return messageFont;
	}
	public static BitmapFont getNumberScrollFont() {
		return numberScrollerFont;
	}
	public static BitmapFont getLevelFont() {
		return levelFont;
	}
	public static BitmapFont getDialogFont() {
		return dialogFont;
	}

	public static void load() {
		int numberScrollerFontSize = calculateNumberScrollerFontSize();
		int boardNumberFontSize = calculateBoardNumberFontSize();
		int levelFontSize = (int) (numberScrollerFontSize * 0.7);
		int messageFontSize = (int) (calculateBoardNumberFontSize() * 0.5);
		int dialogFontSize = (int) (calculateBoardNumberFontSize() * 0.4);
		createTahomaFonts(numberScrollerFontSize, boardNumberFontSize, levelFontSize);
		createTimesFonts(messageFontSize, dialogFontSize);
	}
	private static int calculateBoardNumberFontSize() {
		TextureRegion scrollerRectangle = skin.getRegion("red_rectangle");
		int scrollerRectangleHeight = scrollerRectangle.getRegionHeight();
		int fontSize = (int) (scrollerRectangleHeight * 0.7);
		return fontSize;
	}
	private static int calculateNumberScrollerFontSize() {
		TextureRegion scrollerRectangle = skin.getRegion(SCROLLER_RECTANGLE);
		int scrollerRectangleHeight = scrollerRectangle.getRegionHeight();
		int fontHeight = (int) (scrollerRectangleHeight * 0.85);
		return fontHeight;
	}
	private static void createTahomaFonts(int numberScrollerFontSize, int boardNumberFontSize, int levelFontSize) {
		FreeTypeFontGenerator tahomaGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Tahoma.ttf"));
		numberScrollerFont = tahomaGenerator.generateFont(numberScrollerFontSize);
		boardNumberFont = tahomaGenerator.generateFont(boardNumberFontSize);
		levelFont = tahomaGenerator.generateFont(levelFontSize);
		numberScrollerFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		boardNumberFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		levelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tahomaGenerator.dispose();
	}
	private static void createTimesFonts(int messageFontSize, int dialogFontSize) {
		FreeTypeFontGenerator timesGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Times.dfont"));
		messageFont = timesGenerator.generateFont(messageFontSize);
		messageFont.getData().down = (float) (-messageFontSize * 1.1);
		messageFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		dialogFont = timesGenerator.generateFont(dialogFontSize);
		dialogFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		timesGenerator.dispose();
	}

	public static void dispose() {
		numberScrollerFont = null;
		boardNumberFont = null;
		messageFont = null;
		levelFont = null;
		dialogFont = null;
	}
}
