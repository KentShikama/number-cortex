package com.numbercortex.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

class FontGenerator {

	private static BitmapFont numberScrollerFont;
	private static BitmapFont boardNumberFont;
	private static BitmapFont levelNumberFont;

	private static BitmapFont gillSans57;
	private static BitmapFont gillSans50;
	private static BitmapFont gillSans50Compact;
	private static BitmapFont gillSans40;

	private static BitmapFont gillSansLight140;
	private static BitmapFont gillSansLight200;

	private FontGenerator() {}

	static BitmapFont getBoardNumberFont() {
		return boardNumberFont;
	}
	static BitmapFont getNumberScrollerFont() {
		return numberScrollerFont;
	}
	static BitmapFont getLevelNumberFont() {
		return levelNumberFont;
	}

	static BitmapFont getGillSans57() {
		return gillSans57;
	}
	static BitmapFont getGillSans50() {
		return gillSans50;
	}
	static BitmapFont getGillSans50Compact() {
		return gillSans50Compact;
	}
	static BitmapFont getGillSans40() {
		return gillSans40;
	}

	static BitmapFont getGillSansLight140() {
		return gillSansLight140;
	}
	static BitmapFont getGillSansLight200() {
		return gillSansLight200;
	}

	static void load() {
		int numberScrollerFontSize = 86;
		int boardNumberFontSize = 112;
		int levelNumberFontSize = 60;

		createNumberFonts(numberScrollerFontSize, boardNumberFontSize, levelNumberFontSize);
		createGillSansFonts();
		createGillSansLightFonts();
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
		FreeTypeFontGenerator gillSansGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GillSans.ttc"));
		gillSans57 = gillSansGenerator.generateFont(57);
		gillSans57.getData().down = (float) (-57 * 1.1);
		gillSans57.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans50 = gillSansGenerator.generateFont(50);
		gillSans50.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans50Compact = gillSansGenerator.generateFont(50);
		gillSans50Compact.getData().down = -48f;
		gillSans50Compact.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans40 = gillSansGenerator.generateFont(41);
		gillSans40.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSansGenerator.dispose();
	}
	private static void createGillSansLightFonts() {
		FreeTypeFontGenerator gillSansLightGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/GillSansLight.ttf"));
		gillSansLight140 = gillSansLightGenerator.generateFont(140);
		gillSansLight140.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSansLight200 = gillSansLightGenerator.generateFont(200);
		gillSansLight200.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSansLightGenerator.dispose();
	}

	static void dispose() {
		numberScrollerFont = null;
		boardNumberFont = null;
		levelNumberFont = null;

		gillSans57 = null;
		gillSans50 = null;
		gillSans50Compact = null;
		gillSans40 = null;

		gillSansLight140 = null;
		gillSansLight200 = null;
	}
}
