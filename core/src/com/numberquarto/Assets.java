package com.numberquarto;

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
	
	public static void load() {
		home = new TextureAtlas(Gdx.files.internal("home/home.txt"));		
		homeSkin = new Skin(home);
		
		settings = new TextureAtlas(Gdx.files.internal("settings/settings.txt"));
		settingsSkin = new Skin(settings);
		
		game = new TextureAtlas(Gdx.files.internal("game/game.txt"));
		gameSkin = new Skin(game);
	}

}
