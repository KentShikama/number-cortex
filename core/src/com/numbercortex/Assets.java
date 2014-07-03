package com.numbercortex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	public static Skin homeSkin;
	public static Skin levelsSkin;
	public static Skin gameSkin;
	
	public static Skin settingsSkin;


	public static void loadHome() {
		if (homeSkin == null) {
			TextureAtlas home = new TextureAtlas(Gdx.files.internal("home/home.txt"));
			homeSkin = new Skin(home);
		}
	}
	
	public static void loadLevels() {
		if (levelsSkin == null) {
			TextureAtlas levels = new TextureAtlas(Gdx.files.internal("levels/levels.atlas"));
			levelsSkin = new Skin(levels);
		}
		if (FontGenerator.isNull()) {
			FontGenerator.load();
		}
	}

	public static void loadGame() {
		if (gameSkin == null) {
			TextureAtlas game = new TextureAtlas(Gdx.files.internal("game/game.atlas"));
			gameSkin = new Skin(game);
		}
		if (FontGenerator.isNull()) {
			FontGenerator.load();
		}
	}
	
	/**
	 * TODO: Delete once no longer needed
	 */
	public static void loadSettings() {
		if (settingsSkin == null) {
			TextureAtlas settings = new TextureAtlas(Gdx.files.internal("settings/settings.txt"));
			settingsSkin = new Skin(settings);
		}
	}

}
