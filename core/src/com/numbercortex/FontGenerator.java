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
	private static BitmapFont levelNumberFont;
	
	private static BitmapFont gillSans57;
	private static BitmapFont gillSans50;
	private static BitmapFont gillSans50Compact;

	private FontGenerator() {}

	public static boolean isNull() {
		if (numberScrollerFont == null || boardNumberFont == null || levelNumberFont == null || gillSans57 == null
				|| gillSans50 == null || gillSans50Compact == null) {
			return true;
		} else {
			return false;
		}
	}

	public static BitmapFont getBoardNumberFont() {
		return boardNumberFont;
	}
	public static BitmapFont getNumberScrollerFont() {
		return numberScrollerFont;
	}
	public static BitmapFont getLevelNumberFont() {
		return levelNumberFont;
	}
	
	public static BitmapFont getGillSans57() {
		return gillSans57;
	}
	public static BitmapFont getGillSans50() {
		return gillSans50;
	}
	public static BitmapFont getGillSans50Compact() {
		return gillSans50Compact;
	}

	public static void load() {
		int numberScrollerFontSize = 86;
		int boardNumberFontSize = 112;
		int levelNumberFontSize = 60;
		
		createNumberFonts(numberScrollerFontSize, boardNumberFontSize, levelNumberFontSize);
		createGillSansFonts();
	}
	private static void createNumberFonts(int numberScrollerFontSize, int boardNumberFontSize, int levelFontSize) {
		FreeTypeFontGenerator tahomaGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Tahoma.ttf"));
		numberScrollerFont = tahomaGenerator.generateFont(numberScrollerFontSize);
		boardNumberFont = tahomaGenerator.generateFont(boardNumberFontSize);
		levelNumberFont = tahomaGenerator.generateFont(levelFontSize);
		numberScrollerFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		boardNumberFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		levelNumberFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tahomaGenerator.dispose();
	}
	private static void createGillSansFonts() {
		FreeTypeFontGenerator timesGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GillSans.ttc"));
		gillSans57 = timesGenerator.generateFont(57);
		gillSans57.getData().down = (float) (-57 * 1.1);
		gillSans57.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans50 = timesGenerator.generateFont(50);
		gillSans50.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans50Compact = timesGenerator.generateFont(50);
		gillSans50Compact.getData().down = -48f;
		gillSans50Compact.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		timesGenerator.dispose();
	}

	public static void dispose() {
		numberScrollerFont = null;
		boardNumberFont = null;
		levelNumberFont = null;
		
		gillSans57 = null;
		gillSans50 = null;
		gillSans50Compact = null;
	}
}
