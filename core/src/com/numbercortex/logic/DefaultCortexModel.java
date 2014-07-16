package com.numbercortex.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;

class DefaultCortexModel implements CortexModel {

	private static final String TAG = DefaultCortexModel.class.getCanonicalName();

	private String currentPlayer;
	private ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
	private Map<Integer, Integer> coordinateNumberMap = new HashMap<Integer, Integer>();
	private String message;
	private int chosenNumber = -1;
	private ArrayList<String> players = new ArrayList<String>();

	private String winner;
	private String winningAttribute;
	private int[] winningValues;

	private GameManager messenger;
	private GameSettings settings;

	private int firstPlayerPosition = -1;

	private WinHandler winHandler;

	DefaultCortexModel(GameManager messenger, GameSettings settings) {
		this(messenger, settings, null);
	}
	DefaultCortexModel(GameManager messenger, GameSettings settings, CortexState currentState) {
		this.messenger = messenger;
		this.settings = settings;
		this.winHandler = new WinHandler(settings);
		if (currentState != null) {
			assignCurrentCortexState(currentState);
		}
	}
	private void assignCurrentCortexState(CortexState currentState) {
		this.currentPlayer = currentState.getCurrentPlayer();
		this.availableNumbers = currentState.getAvailableNumbers();
		this.coordinateNumberMap = currentState.getCoordinateNumberMap();
		this.message = currentState.getMessage();
		this.chosenNumber = currentState.getChosenNumber();
		this.players = currentState.getPlayers();
		this.winner = currentState.getWinner();
		this.winningAttribute = currentState.getWinningAttribute();
		this.winningValues = currentState.getWinningValues();
	}

	@Override
	public void chooseNumber(String playerName, int nextNumber) {
		if (isChosenNumberValid(playerName, nextNumber)) {
			updateModelToReflectChoosing(nextNumber);
			sendPostChoosingModelState();
		} else {
			Gdx.app.log(TAG, "Invalid chosen number: " + chosenNumber + ".");
		}
	}
	private boolean isChosenNumberValid(String playerName, int nextNumber) {
		return arePlayersMatching(playerName) && chosenNumber == -1 && isAvailable(nextNumber);
	}
	private boolean isAvailable(int nextNumber) {
		for (Integer number : availableNumbers) {
			if (nextNumber == number) {
				return true;
			}
		}
		return false;
	}
	private void updateModelToReflectChoosing(int nextNumber) {
		chosenNumber = nextNumber;
		availableNumbers.remove(Integer.valueOf(nextNumber));
		currentPlayer = switchCurrentPlayer();
		message = currentPlayer;
	}
	private String switchCurrentPlayer() {
		return currentPlayer.equals(players.get(0)) ? players.get(1) : players.get(0);
	}
	private void sendPostChoosingModelState() {
		CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, players, chosenNumber,
				coordinateNumberMap, availableNumbers).build();
		messenger.updateState(state);
	}

	@Override
	public void placeNumber(String playerName, int coordinate) {
		if (isPlacementValid(playerName, coordinate)) {
			updateModelToReflectPlacement(coordinate);
			sendPostPlacementModelState();
		} else {
			Gdx.app.log(TAG, "Invalid number placement: " + coordinate + ", " + chosenNumber + ".");
			Gdx.app.log(TAG, "Player's match: " + arePlayersMatching(playerName));
			Gdx.app.log(TAG, "Coordinate is empty: " + isCoordinateEmpty(coordinate));
		}
	}
	private boolean isPlacementValid(String playerName, int coordinate) {
		return arePlayersMatching(playerName) && isCoordinateEmpty(coordinate);
	}
	private boolean arePlayersMatching(String playerName) {
		return playerName == currentPlayer;
	}
	private boolean isCoordinateEmpty(int coordinate) {
		return coordinateNumberMap.get(coordinate) == -1;
	}
	private void updateCoordinateNumberMap(int coordinate) {
		for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
			if (entry.getValue() == chosenNumber) {
				coordinateNumberMap.put(entry.getKey(), -1);
			}
		}
		coordinateNumberMap.put(coordinate, chosenNumber);
	}
	private void updateModelToReflectPlacement(int coordinate) {
		updateCoordinateNumberMap(coordinate);
		chosenNumber = -1;
		winningValues = winHandler.handleWinningBoard(coordinateNumberMap);
	}
	private void sendPostPlacementModelState() {
		CortexState state = buildPostPlacementCortexState();
		messenger.updateState(state);
	}
	private CortexState buildPostPlacementCortexState() {
		CortexState state;
		if (winningValues != null) {
			winningAttribute = winHandler.getWinningAttriute();
			state = new CortexState.CortexStateBuilder(message, currentPlayer, players, chosenNumber,
					coordinateNumberMap, availableNumbers).win(currentPlayer, winningAttribute, winningValues).build();
		} else {
			state = new CortexState.CortexStateBuilder(message, currentPlayer, players, chosenNumber,
					coordinateNumberMap, availableNumbers).build();
		}
		return state;
	}

	@Override
	public void setFirstPlayerPosition(int firstPlayerPosition) {
		this.firstPlayerPosition = firstPlayerPosition;
	}

	@Override
	public void register(String username) {
		players.add(username);
		if (players.size() == 2) {
			setFirstPlayer();
			startGame();
		}
	}
	private void setFirstPlayer() {
		if (firstPlayerPosition != -1) {
			currentPlayer = players.get(firstPlayerPosition);
		} else {
			assignRandomFirstPlayer();
		}
	}
	private void assignRandomFirstPlayer() {
		if (Math.random() > 0.5) {
			currentPlayer = players.get(0);
		} else {
			currentPlayer = players.get(1);
		}
	}
	private void startGame() {
		clearVariables();
		setInitialBoardState();
		setInitialAvailableNumbers();
		message = currentPlayer + " starts!";
		sendPostChoosingModelState();
	}
	private void clearVariables() {
		winner = null;
		winningValues = null;
		firstPlayerPosition = -1;
	}
	private void setInitialBoardState() {
		int numberOfRows = settings.getNumberOfRows();
		int boardSize = numberOfRows * numberOfRows;
		coordinateNumberMap.clear();
		for (int i = 0; i < boardSize; i++) {
			coordinateNumberMap.put(i, -1);
		}
	}
	private void setInitialAvailableNumbers() {
		availableNumbers.clear();
		for (int i = 1; i < 18; i++) {
			if (i == 9) {
				continue;
			}
			availableNumbers.add(i);
		}
	}
}
