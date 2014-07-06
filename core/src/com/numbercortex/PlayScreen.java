package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PlayScreen implements Screen {

	public static final String TAG = PlayScreen.class.getCanonicalName();

	private static final int BOTTOM_RECTANGLE_WIDTH =  Assets.gameSkin.getRegion("settings").getRegionWidth();
	private static final int BOTTOM_RECTANGLE_HEIGHT =  Assets.gameSkin.getRegion("settings").getRegionHeight();

	private NumberCortexBoard board;
	private NumberScroller numberScroller;
	private MessageArea messageArea;
	private DragAndDropHandler handler = DragAndDropHandler.getInstance();

	private Game game;
	private Stage stage;

	private GameSettings settings;

	PlayScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}
	
	public void setGameSettings(GameSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void show() {
		stage.clear();
		
		CortexPreferences preferences = CortexPreferences.getInstance();
		
		buildBackground(preferences);
		buildMessageArea();
		buildBoard(settings, preferences);
		buildNumberScroller();
		buildBottomButtons();
		board.clearBoard();
		
		Dialog dialog = CortexDialog.createCortexDialog();
		dialog.show(stage);
		
		
	}
	private void buildBackground(CortexPreferences preferences) {
		Color backgroundProperty = getBackgroundColor(preferences);
		PlayScreenBackground background = new PlayScreenBackground(backgroundProperty);
		stage.addActor(background);
	}
	private Color getBackgroundColor(CortexPreferences preferences) {
		if (preferences.isBlue()) {
			return Launch.SEA_BLUE;
		} else {
			return Launch.RETRO_RED;
		}
	}
	private void buildMessageArea() {
		messageArea = MessageArea.createMessageArea(stage);
		messageArea.updateMessage("New message");
		messageArea.updateMessageWithNextNumber("Welcome to Number Quarto", 4);
		handler.notifyMessageAreaConstrucion(messageArea);
	}
	private void buildBoard(GameSettings settings, CortexPreferences preferences) {
		boolean isBlue = preferences.isBlue();
		int numberOfRows = settings.getNumberOfRows();
		board = NumberCortexBoard.createNumberCortexBoard(stage, isBlue, numberOfRows);
		handler.notifyBoardConstruction(board);
	}
	private void buildBottomButtons() {
		Drawable settingsRectangleSkin = Assets.gameSkin.getDrawable("settings");
		Drawable helpRectangleSkin = Assets.gameSkin.getDrawable("help");
		buildSettingsButton(settingsRectangleSkin);
		buildHelpButton(helpRectangleSkin);
	}
	private void buildSettingsButton(Drawable settingsRectangleSkin) {
		final ImageButton settingsButton = new ImageButton(settingsRectangleSkin, settingsRectangleSkin,
				settingsRectangleSkin);
		settingsButton.setBounds(434, Launch.SCREEN_HEIGHT - 1136, BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ScreenTracker.mode == ScreenTracker.Mode.SINGLE_PLAYER) {
					game.setScreen(ScreenTracker.singlePlayerSettingsScreen);
				} else {
					game.setScreen(ScreenTracker.twoPlayerSettingsScreen);
				}
			}
		});
		stage.addActor(settingsButton);
	}
	private void buildHelpButton(Drawable helpRectangleSkin) {
		ImageButton helpButton = new ImageButton(helpRectangleSkin, helpRectangleSkin, helpRectangleSkin);
		helpButton.setBounds(543, Launch.SCREEN_HEIGHT - 1136, BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		stage.addActor(helpButton);
	}
	private void buildNumberScroller() {
		numberScroller = NumberScroller.createNumberScroller(stage);
	}

	public void updateState(CortexState state, Player currentPlayer) {
		updateCurrentPlayer(currentPlayer);
		updateChosenNumber(state);
		updateMessageArea(state);
		updateBoardMap(state);
		updateNumberScroller(state);
	}
	private void updateCurrentPlayer(Player currentPlayer) {
		numberScroller.setSendable(currentPlayer);
		handler.setSendable(currentPlayer);
	}

	private void updateChosenNumber(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			handler.setChosenNumber(chosenNumber);
		}
	}
	private void updateMessageArea(CortexState state) {
		String message = state.getMessage();
		int chosenNumber = state.getChosenNumber();
		if (chosenNumber != -1) {
			messageArea.updateMessageWithNextNumber(message, chosenNumber);
		} else {
			if (state.getWinningValues() != null) {
				messageArea.updateMessage(state.getWinner() + " wins!");
			} else {
				messageArea.updateMessage(message);
			}
		}
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
		if (state.getWinningValues() != null) {
			int[] winningValues = state.getWinningValues();
			Map<Integer, Integer> winningMap = buildWinningMap(state, winningValues);
			board.showWinningCoordinates(winningMap);
		}
	}
	private Map<Integer, Integer> buildWinningMap(CortexState state, int[] winningValues) {
		Map<Integer, Integer> winningMap = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : state.getCoordinateNumberMap().entrySet()) {
			for (Integer winningValue : winningValues) {
				if (entry.getValue() == winningValue) {
					int winningCoordinate = entry.getKey();
					winningMap.put(winningCoordinate, winningValue);
				}
			}
		}
		return winningMap;
	}
	private void updateNumberScroller(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		numberScroller.update(availableNumbers);
	}

	public ArrayList<Object> getRequiredComponentsForComputerAnimation(int coordinate) {
		NumberTextButton nextNumberCell = messageArea.getNextNumberSquare();
		int numberOfRows = board.getNumberOfRows();
		Label nextNumberLabel = nextNumberCell.getLabel();
		int offset = numberOfRows == 3 ? 36 : 10;
		float nextNumberLabelX = nextNumberCell.getX() - offset;
		float nextNumberLabelY = nextNumberCell.getY() - offset;

		float dragToPositionX = board.getBoardCells().get(coordinate).getX();
		float dragToPositionY = board.getBoardCells().get(coordinate).getY();
		Vector3 pos = new Vector3(dragToPositionX, dragToPositionY, 0);
		stage.getCamera().unproject(pos);
		MoveToAction moveToAction = Actions.moveTo(dragToPositionX - nextNumberLabelX, dragToPositionY
				- nextNumberLabelY, 0.7f);
		ArrayList<Object> components = new ArrayList<Object>();
		components.add(nextNumberLabel);
		components.add(moveToAction);
		return components;
	}

	@Override
	public void resume() {
		if (FontGenerator.isNull()) {
			FontGenerator.load();
		}
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
	public void hide() {}
	@Override
	public void dispose() {}
	@Override
	public void pause() {}

}
