package com.numbercortex.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.numbercortex.Launch;

public class BackgroundScreen extends Actor {

	private Color backgroundProperty;
	private ShapeRenderer shapeRenderer;

	private Texture backgroundTexture;

	BackgroundScreen(Color backgroundProperty) {
		this.backgroundProperty = backgroundProperty;
		this.shapeRenderer = new ShapeRenderer();
	}

	BackgroundScreen(Color backgroundProperty, Texture backgroundTexture) {
		this.backgroundProperty = backgroundProperty;
		this.shapeRenderer = new ShapeRenderer();
		this.backgroundTexture = backgroundTexture;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(backgroundProperty);
		shapeRenderer.rect(0, 0, Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		shapeRenderer.end();
		batch.begin();
		if (backgroundTexture != null) {
			batch.draw(backgroundTexture, 0, 0);
		}
	}

}
