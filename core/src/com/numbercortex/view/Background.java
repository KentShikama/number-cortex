package com.numbercortex.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Background extends Actor {

	private Color backgroundProperty;
	private ShapeRenderer shapeRenderer;
	private float worldWidth;

	private Texture backgroundTexture;

	Background(Color backgroundProperty, float worldWidth) {
		this(backgroundProperty, null, worldWidth);
	}
	Background(Color backgroundProperty, Texture backgroundTexture, float worldWidth) {
		this.backgroundProperty = backgroundProperty;
		this.backgroundTexture = backgroundTexture;
		this.worldWidth = worldWidth;
		this.shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(backgroundProperty);
		shapeRenderer.rect(0, 0, worldWidth, Launch.SCREEN_HEIGHT);
		shapeRenderer.end();
		batch.begin();
		if (backgroundTexture != null) {
			batch.draw(backgroundTexture, 0, 0, worldWidth, (float) (worldWidth * 1.775));
		}
	}

}
