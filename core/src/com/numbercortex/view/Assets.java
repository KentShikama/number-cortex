package com.numbercortex.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	public static AssetManager manager;

	static Texture backgroundTexture;
	static Skin homeSkin;
	static Skin levelsSkin;
	static Skin gameSkin;
	static Skin dialogSkin;
	static Skin settingsSkin;
	
	static Music backgroundMusic;
	static Sound clickSound;

	private static final String BACKGROUND_TEXTURE = "background/number_background.png";

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
	
	private static final String BACKGROUND_MUSIC = "audio/operation_clandestine.mp3";
	private static final String CLICK = "audio/click.wav";

	public static void loadBackground() {
		manager.load(BACKGROUND_TEXTURE, Texture.class);
	}

	public static void assignBackgroundScreen() {
		backgroundTexture = manager.get(BACKGROUND_TEXTURE, Texture.class);
	}

	public static void loadHome() {
		manager.load(HOME_ATLAS, TextureAtlas.class);
		manager.load(HOME_SKIN, Skin.class, new SkinLoader.SkinParameter(HOME_ATLAS));
	}
	public static void assignHomeScreen() {
		homeSkin = manager.get(HOME_SKIN, Skin.class);
	}

	public static void loadLevels() {
		manager.load(LEVEL_ATLAS, TextureAtlas.class);
		manager.load(LEVEL_SKIN, Skin.class, new SkinLoader.SkinParameter(LEVEL_ATLAS));
	}
	public static void assignLevelsScreen() {
		levelsSkin = manager.get(LEVEL_SKIN, Skin.class);
	}

	public static void loadGame() {
		manager.load(GAME_ATLAS, TextureAtlas.class);
		manager.load(GAME_SKIN, Skin.class, new SkinLoader.SkinParameter(GAME_ATLAS));
	}
	public static void assignPlayScreen() {
		gameSkin = manager.get(GAME_SKIN, Skin.class);
	}

	public static void loadDialog() {
		manager.load(DIALOG_ATLAS, TextureAtlas.class);
		manager.load(DIALOG_SKIN, Skin.class, new SkinLoader.SkinParameter(DIALOG_ATLAS));
	}

	public static void assignDialogScreen() {
		dialogSkin = manager.get(DIALOG_SKIN, Skin.class);
	}

	public static void loadSettings() {
		manager.load(SETTINGS_ATLAS, TextureAtlas.class);
		manager.load(SETTINGS_SKIN, Skin.class, new SkinLoader.SkinParameter(SETTINGS_ATLAS));
	}
	public static void assignSettingsScreen() {
		settingsSkin = manager.get(SETTINGS_SKIN, Skin.class);
	}

	public static void loadFonts() {
		FontGenerator.load();
	}
	
	public static void loadAudio() {
		manager.load(BACKGROUND_MUSIC, Music.class);
		manager.load(CLICK, Sound.class);
	}
	public static void assignAudio() {
		backgroundMusic = manager.get(BACKGROUND_MUSIC, Music.class);
		clickSound = manager.get(CLICK, Sound.class);
	}

	public static void dispose() {
		FontGenerator.dispose();
		CortexDialog.dispose();
		Assets.manager.dispose();
	}
}
