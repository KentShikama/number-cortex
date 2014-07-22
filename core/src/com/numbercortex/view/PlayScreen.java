package com.numbercortex.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import libgdx.NumberTextButton;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.ModeTracker;
import com.numbercortex.Persistence;
import com.numbercortex.logic.BoardUtilities;
import com.numbercortex.logic.GameManager;
import com.numbercortex.logic.InteractableSendable;
import com.numbercortex.logic.Playable;
import com.numbercortex.logic.Player;
import com.numbercortex.logic.SinglePlayerGameManager;
import com.numbercortex.logic.TwoPlayerGameManager;

class PlayScreen extends GameScreen implements Playable {

	public static final String TAG = "Play Screen";

	private NumberCortexBoard board;
	private NumberScroller numberScroller;
	private MessageArea messageArea;
	private DragAndDropHandler handler = DragAndDropHandler.getInstance();

	private Stage stage;

	private GameSettings settings;
	private Persistence preferences;

	private Image exitButton;
	private Image informationButton;
	private Image helpButton;

	PlayScreen(Game game) {
		super(game);
	}

	@Override
	public void setGameSettingsAndPreferences(GameSettings settings, Persistence preferences) {
		this.settings = settings;
		this.preferences = preferences;
	}
	@Override
	public void show() {
		FitViewport fitViewport = new FitViewport(Launch.SCREEN_WIDTH, Launch.SCREEN_HEIGHT);
		stage = new Stage(fitViewport);
		Gdx.input.setInputProcessor(stage);

		buildBackground(preferences);
		buildMessageArea(game);
		buildBoard(settings, preferences);
		buildNumberScroller();
		buildBottomButtons();
		board.clearBoard();
		Gdx.input.setCatchBackKey(true);
		backKey = false;
		Sound.loopGameBGM();
	}
	private void buildBackground(Persistence preferences) {
		Color backgroundProperty = getBackgroundColor(preferences);
		Background background = new Background(backgroundProperty);
		stage.addActor(background);
	}
	private Color getBackgroundColor(Persistence preferences) {
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
	private void buildBoard(GameSettings settings, Persistence preferences) {
		boolean isBlue = preferences.isBlue();
		int numberOfRows = settings.getNumberOfRows();
		board = NumberCortexBoard.createNumberCortexBoard(stage, isBlue, numberOfRows);
		handler.notifyBoardConstruction(board);
	}
	private void buildBottomButtons() {
		TextureRegion exitRectangleTexture = Assets.gameSkin.getRegion("exit");
		TextureRegion informationRectangleTexture = Assets.gameSkin.getRegion("information");
		TextureRegion helpRectangleTexture = Assets.gameSkin.getRegion("help");
		bulidExitButton(exitRectangleTexture);
		buildInformationButton(informationRectangleTexture);
		buildHelpButton(helpRectangleTexture);
	}
	private void bulidExitButton(TextureRegion exitRectangleTexture) {
		exitButton = new Image(exitRectangleTexture);
		exitButton.setBounds(44, Launch.SCREEN_HEIGHT - 1136, exitRectangleTexture.getRegionWidth(),
				exitRectangleTexture.getRegionHeight());
		exitButton.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Dialog dialog = buildQuitCancelDialog();
				dialog.show(stage);
			}
		});
		stage.addActor(exitButton);
	}
	private void buildInformationButton(TextureRegion informationRectangleTexture) {
		informationButton = new Image(informationRectangleTexture);
		informationButton.setBounds(434, Launch.SCREEN_HEIGHT - 1136, informationRectangleTexture.getRegionWidth(),
				informationRectangleTexture.getRegionHeight());
		informationButton.addListener(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Sound.pauseGameBGM();
				if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
					game.setScreen(ScreenTracker.singlePlayerSettingsScreen);
				} else {
					game.setScreen(ScreenTracker.twoPlayerSettingsScreen);
				}
			}
		});
		stage.addActor(informationButton);
	}
	private void buildHelpButton(TextureRegion helpRectangleTexture) {
		helpButton = new Image(helpRectangleTexture);
		helpButton.setBounds(543, Launch.SCREEN_HEIGHT - 1136, helpRectangleTexture.getRegionWidth(),
				helpRectangleTexture.getRegionHeight());
		stage.addActor(helpButton);
	}
	private void buildNumberScroller() {
		numberScroller = NumberScroller.createNumberScroller(stage);
	}

	@Override
	public void updateState(CortexState state, Player currentPlayer) {
		final String winner = state.getWinner();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		if (winner == null && !openCoordinates.isEmpty()) {
			updateCurrentPlayer(currentPlayer);
			updateChosenNumber(state);
			updateMessageArea(state);
			updateBoardMap(state);
			updateNumberScroller(state);
		} else {
			updateBoardMap(state);
			animateEndingSequence(state);
			Persistence.getInstance().setInPlay(false);
		}
	}
	private void updateCurrentPlayer(Player currentPlayer) {
		if (currentPlayer instanceof InteractableSendable) {
			InteractableSendable sendable = (InteractableSendable) currentPlayer;
			numberScroller.setSendable(sendable);
			handler.setSendable(sendable);
		} else {
			numberScroller.setSendable(null);
			handler.setSendable(null);
		}
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
		exitButton.clearListeners();
		informationButton.clearListeners();
		helpButton.clearListeners();
		AnimationUtilities.delayFadeAndRemoveActor(exitButton, delay);
		AnimationUtilities.delayFadeAndRemoveActor(informationButton, delay);
		AnimationUtilities.delayFadeAndRemoveActor(helpButton, delay);
	}

	@Override
	public void showConfirmationDialog(String... dialogMessages) {
		Dialog confirmationDialogs = CortexDialog.createConfirmationDialogs(stage, dialogMessages);
		confirmationDialogs.show(stage);
	}
	@Override
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

	@Override
	public void flashChosenNumber(int chosenNumber) {
		messageArea.flashChosenNumber(chosenNumber);
	}

	@Override
	public void placeNumberWithAnimation(int coordinate, Action completePlaceNumberAction) {
		NumberTextButton nextNumberCell = messageArea.getNextNumberSquare();
		MoveToAction moveToAction = buildMoveToAction(coordinate, nextNumberCell);
		DelayAction delayAction = Actions.delay(0.5f);
		SequenceAction placeNumberAction = Actions.sequence(delayAction, moveToAction, completePlaceNumberAction);
		Label nextNumberLabel = nextNumberCell.getLabel();
		nextNumberLabel.addAction(placeNumberAction);
	}
	private MoveToAction buildMoveToAction(int coordinate, NumberTextButton nextNumberCell) {
		int numberOfRows = board.getNumberOfRows();
		int offset = numberOfRows == 3 ? 36 : 10;
		float nextNumberLabelX = nextNumberCell.getX() - offset;
		float nextNumberLabelY = nextNumberCell.getY() - offset;
		float dragToPositionX = board.getBoardCells().get(coordinate).getX();
		float dragToPositionY = board.getBoardCells().get(coordinate).getY();
		MoveToAction moveToAction = Actions.moveTo(dragToPositionX - nextNumberLabelX, dragToPositionY
				- nextNumberLabelY, 0.7f);
		return moveToAction;
	}

	@Override
	public void chooseNumberWithAnimation(int nextNumber, Action completeChooseNumberAction) {
		numberScroller.chooseNumberWithAnimation(nextNumber, completeChooseNumberAction);
	}

	@Override
	public void render(float delta) {
		handleBackKey();
		stage.act(delta);
		stage.draw();
	}
	private void handleBackKey() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			backKey = true;
		} else if (backKey) {
			backKey = false;
			handleScreenSwitch();
		}
	}
	private void handleScreenSwitch() {
		boolean dialogAlreadyExists = checkIfDialogAlreadyExists();
		final Persistence persistence = Persistence.getInstance();
		if (!persistence.isInPlay()) {
			game.setScreen(ScreenTracker.titleScreen);
		} else if (!dialogAlreadyExists) {
			Dialog dialog = buildQuitCancelDialog();
			dialog.show(stage);
		}
	}
	private Dialog buildQuitCancelDialog() {
		Dialog dialog = CortexDialog.createQuitCancelDialog(new ClickListenerWithSound() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Persistence persistence = Persistence.getInstance();
				persistence.setInPlay(false);
				Sound.stopGameBGM();
				game.setScreen(ScreenTracker.titleScreen);
			}
		});
		return dialog;
	}
	private boolean checkIfDialogAlreadyExists() {
		boolean dialogAlreadyExists = false;
		for (Actor actor : stage.getActors()) {
			if (actor instanceof Dialog) {
				dialogAlreadyExists = true;
			}
		}
		return dialogAlreadyExists;
	}
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	@Override
	public void pause() {
		Persistence persistence = Persistence.getInstance();
		if (persistence.isInPlay()) {
			persistence.setCurrentScreen(TAG);
			GameManager gameManager = getGameManagerInstance();
			CortexState currentState = gameManager.getState();
			persistence.setCurrentCortexState(currentState);
		} else {
			persistence.setCurrentScreen(TitleScreen.TAG); // Show title screen if game had ended
		}
		persistence.setMode(ModeTracker.mode.name());
	}
	private GameManager getGameManagerInstance() {
		GameManager gameManager;
		if (ModeTracker.mode == ModeTracker.Mode.SINGLE_PLAYER) {
			gameManager = SinglePlayerGameManager.getInstance();
		} else {
			gameManager = TwoPlayerGameManager.getInstance();
		}
		return gameManager;
	}
	@Override
	public void dispose() {
		MessageArea.dispose();
		NumberCortexBoard.dispose();
		NumberScroller.dispose();
	}
}
