package com.numberquarto;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PlayScreen implements Screen {
	
	private Game game;
	private Stage stage;
	
	private static final Skin skin = Assets.gameSkin;
	private static final String RED_BACKGROUND = "red_background";
	private static final String BLUE_BACKGROUND = "blue_background";
	
	private static final int BOTTOM_RECTANGLE_WIDTH = skin.getRegion("settings").getRegionWidth();
	private static final int BOTTOM_RECTANGLE_HEIGHT = skin.getRegion("settings").getRegionHeight();
	
	private boolean isBlue = false;
	
	PlayScreen(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		buildBackground();
		buildBoard();
		buildNumberScroller();
		buildBottomButtons();
	}

	private void buildBackground() {
		String backgroundProperty = getBackgroundProperty();
		ScreenBackground background = new ScreenBackground(skin, backgroundProperty);
		stage.addActor(background);
	}
	
	private String getBackgroundProperty() {
		if (isBlue) {
			return BLUE_BACKGROUND;
		} else {
			return RED_BACKGROUND;
		}
	}

	class NumberQuartoBoard {

		private static final int SQUARE_LENGTH = Launch.SCREEN_WIDTH/4;
		private static final int NUMBER_OF_ROWS = 4;
		
		public NumberQuartoBoard(Stage stage) {
			for (int i = 0; i < 16; i++) {
				int left = (i % NUMBER_OF_ROWS) * SQUARE_LENGTH;
				int bottom = ((NUMBER_OF_ROWS - 1) - (int)(i/NUMBER_OF_ROWS)) * SQUARE_LENGTH;
				Image rectangle;
				if (isGreen(i)) {
					Drawable greenRectangleSkin = skin.getDrawable("green_rectangle");
					rectangle = new Image(greenRectangleSkin);
				} else {
					Drawable backgroundColorRectangleSkin;
					if (isBlue) {
						backgroundColorRectangleSkin = skin.getDrawable("blue_rectangle");
					} else {
						backgroundColorRectangleSkin = skin.getDrawable("red_rectangle");
					}
					rectangle = new Image(backgroundColorRectangleSkin);				
				}
				rectangle.setName(String.valueOf(i));
				rectangle.setBounds(left, bottom + (Launch.SCREEN_HEIGHT - 850), SQUARE_LENGTH, SQUARE_LENGTH);
				stage.addActor(rectangle);
			}
		}

		private boolean isGreen(int i) {
			int[] greenList = {0, 2, 5, 7, 8, 10, 13, 15};
			for (int green : greenList) {
				if (green == i) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	private void buildBoard() {
		NumberQuartoBoard board = new NumberQuartoBoard(stage);
	}
	
	private void buildNumberScroller() {
		NumberScroller numberScroller = new NumberScroller(stage, skin);
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}
		numberScroller.update(list);
	}
	
	private void buildBottomButtons() {
		Drawable settingsRectangleSkin = skin.getDrawable("settings");
		Drawable helpRectangleSkin = skin.getDrawable("help");
		buildSettingsButton(settingsRectangleSkin);	
		buildHelpButton(helpRectangleSkin);
	}

	private void buildSettingsButton(Drawable settingsRectangleSkin) {
		final ImageButton settingsButton = new ImageButton(settingsRectangleSkin, settingsRectangleSkin,
				settingsRectangleSkin);
		settingsButton.setBounds(434, Launch.SCREEN_HEIGHT - 1136,
				BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		settingsButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SettingsScreen(game));
			}
		});
		stage.addActor(settingsButton);
	}
	
	private void buildHelpButton(Drawable helpRectangleSkin) {
		ImageButton helpButton = new ImageButton(helpRectangleSkin, helpRectangleSkin,
				helpRectangleSkin);
		helpButton.setBounds(543, Launch.SCREEN_HEIGHT - 1136,
				BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		stage.addActor(helpButton);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
