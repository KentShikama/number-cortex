package com.numbercortex.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

class NumberScrollerArrows {

	private static final String RIGHT_ARROW = "right_arrow";
	private static final String LEFT_ARROW = "left_arrow";

	private ImageButton leftArrowButton;
	private ImageButton rightArrowButton;

	NumberScrollerArrows(final ScrollPane scroller, float worldWidth) {
		Drawable leftArrowRectangleSkin = Assets.gameSkin.getDrawable(LEFT_ARROW);
		Drawable rightArrowRectangleSkin = Assets.gameSkin.getDrawable(RIGHT_ARROW);

		int arrowRectangleTextureWidth = Assets.gameSkin.getRegion(LEFT_ARROW).getRegionWidth();
		int arrowRectangleTextureHeight = Assets.gameSkin.getRegion(LEFT_ARROW).getRegionHeight();

		leftArrowButton = new ImageButton(leftArrowRectangleSkin);
		leftArrowButton.setBounds(0, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth,
				arrowRectangleTextureHeight);
		leftArrowButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (leftArrowButton.isDisabled()) {
					return true;
				}
				super.touchDown(event, x, y, pointer, button);
				scroller.fling(1, 750, 0);
				return true;
			}
		});
		rightArrowButton = new ImageButton(rightArrowRectangleSkin);
		rightArrowButton.setBounds(worldWidth - arrowRectangleTextureWidth, Launch.SCREEN_HEIGHT - 1013,
				arrowRectangleTextureWidth, arrowRectangleTextureHeight);
		rightArrowButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (rightArrowButton.isDisabled()) {
					return true;
				}
				super.touchDown(event, x, y, pointer, button);
				scroller.fling(1, -750, 0);
				return true;
			}
		});
	}

	void addArrows(Stage stage) {
		stage.addActor(leftArrowButton);
		stage.addActor(rightArrowButton);
	}

	void setDisabled(boolean disabled) {
		leftArrowButton.setDisabled(disabled);
		rightArrowButton.setDisabled(disabled);
	}

	void remove() {
		leftArrowButton.remove();
		rightArrowButton.remove();
	}
	void remove(float delay) {
		AnimationUtilities.delayFadeAndRemoveActor(leftArrowButton, delay);
		AnimationUtilities.delayFadeAndRemoveActor(rightArrowButton, delay);
	}
}
