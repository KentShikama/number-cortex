package com.numbercortex.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

class Assets {

	static AssetManager manager;

	static Texture backgroundTexture;
	static Skin splashSkin;
	static Skin homeSkin;
	static Skin levelsSkin;
	static Skin gameSkin;
	static Skin dialogSkin;
	static Skin settingsSkin;

	static Music openingBGM;
	static Music gameBGM;
	static Sound clickSound;
	static Sound missClickSound;
	static Sound winSound;
	static Sound loseSound;
	
	static BitmapFont numberScrollerFont;
	static BitmapFont boardNumberFont;
	static BitmapFont levelNumberFont;

	static BitmapFont gillSans57;
	static BitmapFont gillSans50;
	static BitmapFont gillSans50Compact;
	static BitmapFont gillSans41;

	static BitmapFont gillSansLight140;
	static BitmapFont gillSansLight200;

	private static final String BACKGROUND_TEXTURE = "background/number_background.png";
	
	private static final String SPLASH_ATLAS = "splash/splash.atlas";
	private static final String SPLASH_SKIN = "splash/splash.json";

	private static final String HOME_ATLAS = "home/home.atlas";
	private static final String HOME_SKIN = "home/home.json";

	private static final String LEVEL_ATLAS = "levels/levels.atlas";
	private static final String LEVEL_SKIN = "levels/levels.json";

	private static final String GAME_ATLAS = "game/game.atlas";
	private static final String GAME_SKIN = "game/game.json";

	private static final String DIALOG_ATLAS = "dialog/dialog.atlas";
	private static final String DIALOG_SKIN = "dialog/dialog.json";

	private static final String SETTINGS_ATLAS = "settings/settings.atlas";
	private static final String SETTINGS_SKIN = "settings/settings.json";

	private static final String OPENING_BACKGROUND_MUSIC = "audio/opening.mp3";
	private static final String GAME_BACKGROUND_MUSIC = "audio/operation_clandestine.mp3";
	private static final String CLICK_SOUND = "audio/click.wav";
	private static final String MISS_CLICK_SOUND = "audio/miss.wav";
	private static final String WIN_SOUND = "audio/win.mp3";
	private static final String LOSE_SOUND = "audio/lose.mp3";
	
	private static final String TAHOMA_TTF = "fonts/Tahoma.ttf";
	private static final String GILL_SANS_TTF = "fonts/GillSans.ttf";
	private static final String GILL_SANS_LIGHT_TTF = "fonts/GillSansLight.ttf";
	
	private static final String NUMBER_SCROLLER_FONT = "numberScrollerFont.ttf";
	private static final String BOARD_NUMBER_FONT = "boardNumberFont.ttf";
	private static final String LEVEL_NUMBER_FONT = "levelNumberFont.ttf";
	private static final String GILL_SANS_57_FONT = "gillSans57.ttf";
	private static final String GILL_SANS_50_FONT = "gillSans50.ttf";
	private static final String GILL_SANS_COMPACT_50_FONT = "gillSansCompact50.ttf";
	private static final String GILL_SANS_41_FONT = "gillSans41.ttf";
	private static final String GILL_SANS_LIGHT_140_FONT = "gillSansLight140.ttf";
	private static final String GILL_SANS_LIGHT_200_FONT = "gillSansLight200.ttf";

	static void loadBackground() {
		manager.load(BACKGROUND_TEXTURE, Texture.class);
	}

	static void assignBackgroundScreen() {
		backgroundTexture = manager.get(BACKGROUND_TEXTURE, Texture.class);
	}
	
	static void loadSplash() {
		manager.load(SPLASH_ATLAS, TextureAtlas.class);
		manager.load(SPLASH_SKIN, Skin.class, new SkinLoader.SkinParameter(SPLASH_ATLAS));
	}
	static void assignSplashScreen() {
		splashSkin = manager.get(SPLASH_SKIN, Skin.class);
	}
	static void unloadSplash() {
		if (splashSkin != null) {
			manager.unload(SPLASH_ATLAS);
			manager.unload(SPLASH_SKIN);
			splashSkin = null;	
		}
	}

	static void loadHome() {
		manager.load(HOME_ATLAS, TextureAtlas.class);
		manager.load(HOME_SKIN, Skin.class, new SkinLoader.SkinParameter(HOME_ATLAS));
	}
	static void assignHomeScreen() {
		homeSkin = manager.get(HOME_SKIN, Skin.class);
	}

	static void loadLevels() {
		manager.load(LEVEL_ATLAS, TextureAtlas.class);
		manager.load(LEVEL_SKIN, Skin.class, new SkinLoader.SkinParameter(LEVEL_ATLAS));
	}
	static void assignLevelsScreen() {
		levelsSkin = manager.get(LEVEL_SKIN, Skin.class);
	}

	static void loadGame() {
		manager.load(GAME_ATLAS, TextureAtlas.class);
		manager.load(GAME_SKIN, Skin.class, new SkinLoader.SkinParameter(GAME_ATLAS));
	}
	static void assignPlayScreen() {
		gameSkin = manager.get(GAME_SKIN, Skin.class);
	}

	static void loadDialog() {
		manager.load(DIALOG_ATLAS, TextureAtlas.class);
		manager.load(DIALOG_SKIN, Skin.class, new SkinLoader.SkinParameter(DIALOG_ATLAS));
	}

	static void assignDialogScreen() {
		dialogSkin = manager.get(DIALOG_SKIN, Skin.class);
	}

	static void loadSettings() {
		manager.load(SETTINGS_ATLAS, TextureAtlas.class);
		manager.load(SETTINGS_SKIN, Skin.class, new SkinLoader.SkinParameter(SETTINGS_ATLAS));
	}
	static void assignSettingsScreen() {
		settingsSkin = manager.get(SETTINGS_SKIN, Skin.class);
	}

	static void loadFonts() {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter numberScrollerFontParam = new FreeTypeFontLoaderParameter();
		numberScrollerFontParam.fontFileName = TAHOMA_TTF;
		numberScrollerFontParam.fontParameters.size = 86;
		manager.load(NUMBER_SCROLLER_FONT, BitmapFont.class, numberScrollerFontParam);

		FreeTypeFontLoaderParameter boardNumberFontParam = new FreeTypeFontLoaderParameter();
		boardNumberFontParam.fontFileName = TAHOMA_TTF;
		boardNumberFontParam.fontParameters.size = 112;
		manager.load(BOARD_NUMBER_FONT, BitmapFont.class, boardNumberFontParam);
		
		FreeTypeFontLoaderParameter levelNumberFontParam = new FreeTypeFontLoaderParameter();
		levelNumberFontParam.fontFileName = TAHOMA_TTF;
		levelNumberFontParam.fontParameters.size = 60;
		manager.load(LEVEL_NUMBER_FONT, BitmapFont.class, levelNumberFontParam);
		
		FreeTypeFontLoaderParameter gillSans57Param = new FreeTypeFontLoaderParameter();
		gillSans57Param.fontFileName = GILL_SANS_TTF;
		gillSans57Param.fontParameters.size = 57;
		manager.load(GILL_SANS_57_FONT, BitmapFont.class, gillSans57Param);
		
		FreeTypeFontLoaderParameter gillSans50Param = new FreeTypeFontLoaderParameter();
		gillSans50Param.fontFileName = GILL_SANS_TTF;
		gillSans50Param.fontParameters.size = 50;
		manager.load(GILL_SANS_50_FONT, BitmapFont.class, gillSans50Param);
		
		FreeTypeFontLoaderParameter gillSans50CompactParam = new FreeTypeFontLoaderParameter();
		gillSans50CompactParam.fontFileName = GILL_SANS_TTF;
		gillSans50CompactParam.fontParameters.size = 50;
		manager.load(GILL_SANS_COMPACT_50_FONT, BitmapFont.class, gillSans50CompactParam);
		
		FreeTypeFontLoaderParameter gillSans41Param = new FreeTypeFontLoaderParameter();
		gillSans41Param.fontFileName = GILL_SANS_TTF;
		gillSans41Param.fontParameters.size = 41;
		manager.load(GILL_SANS_41_FONT, BitmapFont.class, gillSans41Param);
	
		FreeTypeFontLoaderParameter gillSansLight140Param = new FreeTypeFontLoaderParameter();
		gillSansLight140Param.fontFileName = GILL_SANS_LIGHT_TTF;
		gillSansLight140Param.fontParameters.size = 140;
		manager.load(GILL_SANS_LIGHT_140_FONT, BitmapFont.class, gillSansLight140Param);
		
		FreeTypeFontLoaderParameter gillSansLight200Param = new FreeTypeFontLoaderParameter();
		gillSansLight200Param.fontFileName = GILL_SANS_LIGHT_TTF;
		gillSansLight200Param.fontParameters.size = 200;
		manager.load(GILL_SANS_LIGHT_200_FONT, BitmapFont.class, gillSansLight200Param);
	}
	static void assignFonts() {
		numberScrollerFont = manager.get(NUMBER_SCROLLER_FONT, BitmapFont.class);
		boardNumberFont = manager.get(BOARD_NUMBER_FONT, BitmapFont.class);
		levelNumberFont = manager.get(LEVEL_NUMBER_FONT, BitmapFont.class);

		gillSans57 = manager.get(GILL_SANS_57_FONT, BitmapFont.class);
		gillSans57.getData().down = (float) (-57 * 1.1);
		gillSans50 = manager.get(GILL_SANS_50_FONT, BitmapFont.class);
		gillSans50Compact = manager.get(GILL_SANS_COMPACT_50_FONT, BitmapFont.class);
		gillSans50Compact.getData().down = -48f;
		gillSans41 = manager.get(GILL_SANS_41_FONT, BitmapFont.class);
		
		gillSansLight140 = manager.get(GILL_SANS_LIGHT_140_FONT, BitmapFont.class);
		gillSansLight200 = manager.get(GILL_SANS_LIGHT_200_FONT, BitmapFont.class);

		applyLinearFilterToAllFonts();
	}

	private static void applyLinearFilterToAllFonts() {
		numberScrollerFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		boardNumberFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		levelNumberFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans57.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans50.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans50Compact.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gillSans41.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	static void loadAudio() {
		manager.load(OPENING_BACKGROUND_MUSIC, Music.class);
		manager.load(GAME_BACKGROUND_MUSIC, Music.class);
		manager.load(CLICK_SOUND, Sound.class);
		manager.load(MISS_CLICK_SOUND, Sound.class);
		manager.load(WIN_SOUND, Sound.class);
		manager.load(LOSE_SOUND, Sound.class);
	}
	static void assignAudio() {
		openingBGM = manager.get(OPENING_BACKGROUND_MUSIC, Music.class);
		gameBGM = manager.get(GAME_BACKGROUND_MUSIC, Music.class);
		clickSound = manager.get(CLICK_SOUND, Sound.class);
		missClickSound = manager.get(MISS_CLICK_SOUND, Sound.class);
		winSound = manager.get(WIN_SOUND, Sound.class);
		loseSound = manager.get(LOSE_SOUND, Sound.class);
	}

	static void dispose() {
		ScreenTracker.dispose();
		CortexDialog.dispose();
		Assets.manager.dispose();
	}
}
