package com.numberquarto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	private static TextureAtlas home;	
	public static Skin homeSkin;
	
	public static void load() {
		home = new TextureAtlas(Gdx.files.internal("home/home.txt"));		
		homeSkin = new Skin();
		homeSkin.addRegions(home);
	}

}
