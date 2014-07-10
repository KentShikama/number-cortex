package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PlayScreen implements Screen {

	public static final String TAG = PlayScreen.class.getCanonicalName();

	private static final int BOTTOM_RECTANGLE_WIDTH = Assets.gameSkin.getRegion("settings").getRegionWidth();
	private static final int BOTTOM_RECTANGLE_HEIGHT = Assets.gameSkin.getRegion("settings").getRegionHeight();

	private NumberCortexBoard board;
	private NumberScroller numberScroller;
	private MessageArea messageArea;
	private DragAndDropHandler handler = DragAndDropHandler.getInstance();

	private Game game;
	private Stage stage;

	private GameSettings settings;
	private CortexPreferences preferences;

	private ImageButton settingsButton;
	private ImageButton helpButton;

	PlayScreen(Game game) {
		this.game = game;
		stage = ((Launch) game).getStage();
	}

	public void setGameSettingsAndPreferences(GameSettings settings, CortexPreferences preferences) {
		this.settings = settings;
		this.preferences = preferences;
	}

	@Override
	public void show() {
		stage.clear();		
		buildBackground(preferences);
		buildMessageArea(game);
		buildBoard(settings, preferences);
		buildNumberScroller();
		buildBottomButtons();
		board.clearBoard();
	}
	private void buildBackground(CortexPreferences preferences) {
		Color backgroundProperty = getBackgroundColor(preferences);
		BackgroundScreen background = new BackgroundScreen(backgroundProperty);
		stage.addActor(background);
	}
	private Color getBackgroundColor(CortexPreferences preferences) {
		if (preferences.isBlue()) {
			return Launch.SEA_BLUE;
		} else {
			return Launch.RETRO_RED;
		}
	}
	private void buildMessageArea(Game game) {
		messageArea = MessageArea.createMessageArea(stage, game);
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
		settingsButton = new ImageButton(settingsRectangleSkin, settingsRectangleSkin, settingsRectangleSkin);
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
		helpButton = new ImageButton(helpRectangleSkin, helpRectangleSkin, helpRectangleSkin);
		helpButton.setBounds(543, Launch.SCREEN_HEIGHT - 1136, BOTTOM_RECTANGLE_WIDTH, BOTTOM_RECTANGLE_HEIGHT);
		stage.addActor(helpButton);
	}
	private void buildNumberScroller() {
		numberScroller = NumberScroller.createNumberScroller(stage);
	}

	public void updateState(CortexState state, Player currentPlayer) {
		final String winner = state.getWinner();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		if (winner == null && !openCoordinates.isEmpty()) {
			updateCurrentPlayer(currentPlayer);
			updateChosenNumber(state);
			updateMessageArea(state);
			updateBoardMap(state);
			updateNumberScroller(state);
		} else {
			animateEndingSequence(state);
			ScreenTracker.isInPlay = false;
		}
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
			messageArea.updateMessage(message);
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
	}
	private void updateNumberScroller(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		numberScroller.update(availableNumbers);
	}
	private void animateEndingSequence(CortexState state) {
		String winner = state.getWinner();
		String winningAttribute = state.getWinningAttribute();
		float currentAnimationTime = 0f;
		if (winner != null) {
			currentAnimationTime += handleShowingOfWinningCoordinates(state);
		} else {
			int tieDelay = 1;
			currentAnimationTime += tieDelay;
		}
		currentAnimationTime += moveDownBoardAndRemoveOtherElements(currentAnimationTime);
		messageArea.showEndingMessageSequence(winner, winningAttribute, currentAnimationTime);
	}
	private float handleShowingOfWinningCoordinates(CortexState state) {
		int[] winningValues = state.getWinningValues();
		Map<Integer, Integer> winningMap = buildWinningMap(state, winningValues);
		return board.showWinningCoordinates(winningMap);
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
	private float moveDownBoardAndRemoveOtherElements(float delay) {
		board.bringCellsDown(delay);
		removeOtherElementsWithAnimation(delay);
		return 1f;
	}
	private void removeOtherElementsWithAnimation(float delay) {
		numberScroller.removeScroller(delay);
		settingsButton.clearListeners();
		helpButton.clearListeners();
		AnimationUtilities.delayFadeAndRemoveActor(settingsButton, delay);
		AnimationUtilities.delayFadeAndRemoveActor(helpButton, delay);
	}

	public void showConfirmationDialog(String... dialogMessages) {
		Dialog confirmationDialogs = CortexDialog.createConfirmationDialogs(stage, dialogMessages);
		confirmationDialogs.show(stage);
	}
	public void showConfirmationDialog(float delay, final String... dialogMessages) {
		DelayAction delayAction = Actions.delay(delay);
		Action showConfirmationDialogAction = new Action() {
			@Override
			public boolean act(float delta) {
				Dialog confirmationDialogs = CortexDialog.createConfirmationDialogs(stage, dialogMessages);
				confirmationDialogs.show(stage);
				return true;
			}		
		};
		stage.addAction(Actions.sequence(delayAction, showConfirmationDialogAction));
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
