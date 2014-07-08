package com.numbercortex;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	public static AssetManager manager;

	public static Skin homeSkin;
	public static Skin levelsSkin;
	public static Skin gameSkin;
	public static Skin dialogSkin;
	public static Skin settingsSkin;

	private static final String HOME_ATLAS = "home/home.txt";
	private static final String HOME_SKIN = "home/home.json";

	private static final String LEVEL_ATLAS = "levels/levels.atlas";
	private static final String LEVEL_SKIN = "levels/levels.json";

	private static final String GAME_ATLAS = "game/game.atlas";
	private static final String GAME_SKIN = "game/game.json";

	private static final String DIALOG_ATLAS = "dialog/dialog.atlas";
	private static final String DIALOG_SKIN = "dialog/dialog.json";

	private static final String SETTINGS_ATLAS = "settings/settings.txt";
	private static final String SETTINGS_SKIN = "settings/settings.json";

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

	/**
	 * TODO: Delete once no longer needed
	 */
	public static void loadSettings() {
		manager.load(SETTINGS_ATLAS, TextureAtlas.class);
		manager.load(SETTINGS_SKIN, Skin.class, new SkinLoader.SkinParameter(SETTINGS_ATLAS));
	}
	public static void assignSettingsScreen() {
		settingsSkin = manager.get(SETTINGS_SKIN, Skin.class);
	}

}
