package com.numbercortex;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayScreenBackground extends Actor {

	private Color backgroundProperty;
	private ShapeRenderer shapeRenderer;

	public PlayScreenBackground(Color backgroundProperty) {
		this.backgroundProperty = backgroundProperty;
		this.shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(backgroundProperty);
		shapeRenderer.rect(0, 0, Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		shapeRenderer.end();
		batch.begin();
	}

}
