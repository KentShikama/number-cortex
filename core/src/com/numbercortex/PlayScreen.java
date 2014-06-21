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
	
	private static final String TAG = PlayScreen.class.getCanonicalName();

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
	private ArrayList<Player> players = new ArrayList<Player>();
	
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
		
		CortexPreferences preferences = CortexPreferences.getInstance();
		
		buildBackground(preferences);
		buildMessageArea();
		buildBoard(preferences);
		buildNumberScroller();
		buildBottomButtons();
		
		buildGame();
	}

	public void updateState(CortexState state) {
		updateCurrentPlayer(state);
		updateMessageArea(state);
		updateBoardMap(state);
		updateNumberScroller(state);
	}

	private void buildBackground(CortexPreferences preferences) {
		String backgroundProperty = getBackgroundProperty(preferences);
		ScreenBackground background = new ScreenBackground(skin, backgroundProperty);
		stage.addActor(background);
	}

	private void buildBoard(CortexPreferences preferences) {
		board = new NumberCortexBoard(stage, preferences);
		handler.notifyBoardConstruction(board);
	}
	
	private void buildBottomButtons() {
		Drawable settingsRectangleSkin = skin.getDrawable("settings");
		Drawable helpRectangleSkin = skin.getDrawable("help");
		buildSettingsButton(settingsRectangleSkin);	
		buildHelpButton(helpRectangleSkin);
	}
	
	private void buildGame() {
		Local local = new Local();
		Player playerOne = new HumanPlayer("Player 1", this, local);
		Player playerTwo = new HumanPlayer("Player 2", this, local);
		players.add(playerOne);
		players.add(playerTwo);
		for (Player player : players) {
			local.register(player);			
		}
	}
	
	private void buildHelpButton(Drawable helpRectangleSkin) {
		ImageButton helpButton = new ImageButton(helpRectangleSkin, helpRectangleSkin,
				helpRectangleSkin);
		helpButton.setBounds(543, Launch.SCREEN_HEIGHT - 1136,
				BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		stage.addActor(helpButton);
	}
	
	private void buildMessageArea() {
		messageArea = new MessageArea(stage);
		messageArea.updateMessage("New message");
		messageArea.updateMessageWithNextNumber("Welcome to Number Quarto", 4);
		handler.notifyMessageAreaConstrucion(messageArea);
	}
	
	private void buildNumberScroller() {
		numberScroller = new NumberScroller(stage);
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
	
	private String getBackgroundProperty(CortexPreferences preferences) {
		if (preferences.isBlue()) {
			return BLUE_BACKGROUND;
		} else {
			return RED_BACKGROUND;
		}
	}
	
	private Sendable getCurrentPlayer(String currentPlayerName) {
		for (Player player : players) {
			String playerName = player.getName();
			if (playerName.equals(currentPlayerName)) {
				return player;
			}
		}
		Gdx.app.log(TAG, "The player was not found.");
		return null;
	}

	private void updateBoardMap(CortexState state) {
		Map<Integer, Integer> boardMap = state.getCoordinateNumberMap();
		for (Map.Entry<Integer, Integer> entry : boardMap.entrySet()) {
			int coordinate = entry.getKey();
			int number = entry.getValue();
			if (number != -1) {
				board.updateCell(coordinate, number);
			}
		}
	}

	private void updateCurrentPlayer(CortexState state) {
		String currentPlayerName = state.getCurrentPlayer();
		Sendable currentPlayer = getCurrentPlayer(currentPlayerName);
		numberScroller.setSendable(currentPlayer);
		handler.setSendable(currentPlayer);
	}

	private void updateMessageArea(CortexState state) {
		String message = state.getMessage();
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			messageArea.updateMessageWithNextNumber(message, chosenNumber);
		} else {
			messageArea.updateMessage(message);
		}		
	}

	private void updateNumberScroller(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		numberScroller.update(availableNumbers);
	}
	
	@Override
	public void dispose() {}

	@Override
	public void hide() {}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}

}
