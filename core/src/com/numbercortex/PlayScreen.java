package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PlayScreen implements Screen, CortexScreen {
	
	private Game game;
	private Stage stage;
	
	private static final Skin skin = Assets.gameSkin;
	private static final String RED_BACKGROUND = "red_background";
	private static final String BLUE_BACKGROUND = "blue_background";
	
	private static final int BOTTOM_RECTANGLE_WIDTH = skin.getRegion("settings").getRegionWidth();
	private static final int BOTTOM_RECTANGLE_HEIGHT = skin.getRegion("settings").getRegionHeight();
	
	private boolean isBlue = false;
	private CortexModel model;
	private NumberCortexBoard board;
	private NumberScroller numberScroller;
		
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
	
	public void updateState(CortexState state) {
		/**
		 * Update board map
		 */
		Map<Integer, Integer> boardMap = state.getCoordinateNumberMap();
		for (Map.Entry<Integer, Integer> entry : boardMap.entrySet()) {
			int coordinate = entry.getKey();
			int number = entry.getValue();
			if (number != -1) {
				board.updateCell(coordinate, number);
			}
		}
		/**
		 * Update number scroller
		 */
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		numberScroller.update(availableNumbers);
		/**
		 * Update message area
		 */
		
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		buildBackground();
		buildBoard();
		buildNumberScroller();
		buildBottomButtons();
		
		model = new DefaultCortexModel();
		model.register(this);
		model.startGame();
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
	
	private void buildBoard() {
		board = new NumberCortexBoard(stage, model, isBlue);
		DragAndDropHandler.getInstance().notifyBoardConstruction(board);
	}
	
	private void buildNumberScroller() {
		numberScroller = new NumberScroller(stage);
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
			@Override
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
