package com.numbercortex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	private static TextureAtlas home;
	private static TextureAtlas settings;
	private static TextureAtlas game;
	public static Skin homeSkin;
	public static Skin settingsSkin;
	public static Skin gameSkin;

	public static void loadHome() {
		if (homeSkin == null) {
			home = new TextureAtlas(Gdx.files.internal("home/home.txt"));
			homeSkin = new Skin(home);
		}
	}

	public static void loadSettings() {
		if (settingsSkin == null) {
			settings = new TextureAtlas(Gdx.files.internal("settings/settings.txt"));
			settingsSkin = new Skin(settings);
		}
	}

	public static void loadGame() {
		if (gameSkin == null) {
			game = new TextureAtlas(Gdx.files.internal("game/game.atlas"));
			gameSkin = new Skin(game);
		}
		if (FontGenerator.isNull()) {
			FontGenerator.load();
		}
	}

}
