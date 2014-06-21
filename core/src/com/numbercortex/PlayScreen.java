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

public class PlayScreen implements Screen {
	
	private Game game;
	private Stage stage;
	
	private static final Skin skin = Assets.gameSkin;
	private static final String RED_BACKGROUND = "red_background";
	private static final String BLUE_BACKGROUND = "blue_background";
	
	private static final int BOTTOM_RECTANGLE_WIDTH = skin.getRegion("settings").getRegionWidth();
	private static final int BOTTOM_RECTANGLE_HEIGHT = skin.getRegion("settings").getRegionHeight();
	
	private NumberCortexBoard board;
	private NumberScroller numberScroller;
	private MessageArea messageArea;
	private DragAndDropHandler handler = DragAndDropHandler.getInstance();
	private Local local;
	
	PlayScreen(Game game) {
		this.game = game;
	}
	
	public void updateMessageArea(String message, int chosenNumber) {
		if (chosenNumber != -1) {
			messageArea.updateMessageWithNextNumber(message, chosenNumber);
		} else {
			messageArea.updateMessage(message);
		}	
	}
	
	public void updateBoardCell(int coordinate, int number) {
		board.updateCell(coordinate, number);
	}
	
	public void updateNumberScroller(ArrayList<Integer> availableNumbers) {
		numberScroller.update(availableNumbers);
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
		
		CortexPreferences preferences = CortexPreferences.getInstance();
		
		CortexModel model = new DefaultCortexModel();
		local = new Local(model);

		handler.setExchangeable(local);
		buildBackground(preferences);
		buildMessageArea();
		buildBoard(preferences);
		buildNumberScroller();
		buildBottomButtons();
		
		Player player1 = new HumanPlayer(this, "Player 1");
		Player player2 = new HumanPlayer(this, "Player 2");
		local.register(player1);
		local.register(player2);
	}

	private void buildBackground(CortexPreferences preferences) {
		String backgroundProperty = getBackgroundProperty(preferences);
		ScreenBackground background = new ScreenBackground(skin, backgroundProperty);
		stage.addActor(background);
	}
	
	private void buildMessageArea() {
		messageArea = new MessageArea(stage);
		messageArea.updateMessage("New message");
		messageArea.updateMessageWithNextNumber("Welcome to Number Quarto", 4);
		handler.notifyMessageAreaConstrucion(messageArea);
	}
	
	private String getBackgroundProperty(CortexPreferences preferences) {
		if (preferences.isBlue()) {
			return BLUE_BACKGROUND;
		} else {
			return RED_BACKGROUND;
		}
	}
	
	private void buildBoard(CortexPreferences preferences) {
		board = new NumberCortexBoard(stage, preferences);
		handler.notifyBoardConstruction(board);
	}
	
	private void buildNumberScroller() {
		numberScroller = new NumberScroller(stage, local);
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
		
	}

	@Override
	public void pause() {
		
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
