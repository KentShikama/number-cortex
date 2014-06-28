package com.numbercortex;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

class ScreenBackground extends Actor {
	private TextureRegion backgroundTexture;

	ScreenBackground(Skin skin, String background_texture) {
		backgroundTexture = skin.getRegion(background_texture);
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(backgroundTexture, 0, 0);
	}
}
