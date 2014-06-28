package com.numbercortex;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class NumberScrollerArrows {

	private static Skin skin = Assets.gameSkin;
	private static final String RIGHT_ARROW = "right_arrow";
	private static final String LEFT_ARROW = "left_arrow";

	public static void buildArrows(Stage stage, final ScrollPane scroller) {
		Drawable leftArrowRectangleSkin = skin.getDrawable(LEFT_ARROW);
		Drawable rightArrowRectangleSkin = skin.getDrawable(RIGHT_ARROW);

		int arrowRectangleTextureWidth = skin.getRegion(LEFT_ARROW).getRegionWidth();
		int arrowRectangleTextureHeight = skin.getRegion(LEFT_ARROW).getRegionHeight();

		ImageButton leftArrowButton = new ImageButton(leftArrowRectangleSkin);
		leftArrowButton.setBounds(0, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth,
				arrowRectangleTextureHeight);
		leftArrowButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				scroller.fling(1, 750, 0);
				return true;
			}
		});
		ImageButton rightArrowButton = new ImageButton(rightArrowRectangleSkin);
		rightArrowButton.setBounds(539, Launch.SCREEN_HEIGHT - 1013, arrowRectangleTextureWidth,
				arrowRectangleTextureHeight);
		rightArrowButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				scroller.fling(1, -750, 0);
				return true;
			}
		});
		stage.addActor(leftArrowButton);
		stage.addActor(rightArrowButton);
	}

}
