package com.numbercortex.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

	static void loadAndAssignFonts() {
		FontGenerator.load();
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
		FontGenerator.dispose();
		CortexDialog.dispose();
		Assets.manager.dispose();
	}
}
