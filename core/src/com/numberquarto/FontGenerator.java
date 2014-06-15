package com.numberquarto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FontGenerator {

	private static BitmapFont numberScrollerFont;
	private static BitmapFont boardNumberFont;

	private static Skin skin = Assets.gameSkin;

	private FontGenerator() {}
	
	public static void load() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/Tahoma.ttf"));
		numberScrollerFont = generator
				.generateFont(calculateNumberScrollerFontSize());
		boardNumberFont = generator
				.generateFont(calculateBoardNumberFontSize());
		numberScrollerFont.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		boardNumberFont.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		generator.dispose();	
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

}
