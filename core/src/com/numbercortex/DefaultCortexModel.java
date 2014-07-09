package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class DefaultCortexModel implements CortexModel {

	private static final String TAG = DefaultCortexModel.class.getCanonicalName();

	private String currentPlayer;
	private ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
	private Map<Integer, Integer> coordinateNumberMap = new HashMap<Integer, Integer>();
	private String message;
	private int chosenNumber = -1;

	private ArrayList<String> usernames = new ArrayList<String>();

	private String winner; // Optional
	private String winningAttribute; // Optional
	private int[] winningValues; // Optional

	private GameManager messenger;
	private GameSettings settings;

	private int firstPlayerPosition = -1;

	private WinHandler winHandler;

	public DefaultCortexModel(GameManager messenger, GameSettings settings) {
		this.messenger = messenger;
		this.settings = settings;
		this.winHandler = new WinHandler(settings);
	}

	@Override
	public void chooseNumber(String playerName, int nextNumber) {
		if (isChosenNumberValid(playerName, nextNumber)) {
			chosenNumber = nextNumber;
			availableNumbers.remove(Integer.valueOf(nextNumber));
			currentPlayer = (currentPlayer == usernames.get(0) ? usernames.get(1) : usernames.get(0));
			message = currentPlayer;
			CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber,
					coordinateNumberMap, availableNumbers).build();
			messenger.updateState(state);
		} else {
			Gdx.app.log(TAG, "Invalid chosen number: " + chosenNumber + ".");
			for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
				Gdx.app.log(TAG, entry.getKey() + ", " + entry.getValue());
			}
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

	@Override
	public void placeNumber(String playerName, int coordinate) {
		if (isNumberPlacementValid(playerName, coordinate)) {
			for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
				if (entry.getValue() == chosenNumber) {
					coordinateNumberMap.put(entry.getKey(), -1);
				}
			}
			coordinateNumberMap.put(coordinate, chosenNumber);
			chosenNumber = -1;
			winningValues = winHandler.handleWinningBoard(coordinateNumberMap);
			CortexState state;
			if (winningValues != null) {
				winningAttribute = winHandler.getWinningAttriute();
				state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber,
						coordinateNumberMap, availableNumbers).win(currentPlayer, winningAttribute, winningValues).build();
			} else {
				state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber,
						coordinateNumberMap, availableNumbers).build();
			}
			messenger.updateState(state);
		} else {
			Gdx.app.log(TAG, "Invalid number placement: " + coordinate + ", " + chosenNumber + ".");
			for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
				Gdx.app.log(TAG, entry.getKey() + ", " + entry.getValue());
			}
			Gdx.app.log(TAG, "Player's match: " + arePlayersMatching(playerName));
			Gdx.app.log(TAG, "Coordinate is empty: " + isCoordinateEmpty(coordinate));
		}
	}
	private boolean isNumberPlacementValid(String playerName, int coordinate) {
		return arePlayersMatching(playerName) && isCoordinateEmpty(coordinate);
	}
	private boolean arePlayersMatching(String playerName) {
		return playerName == currentPlayer;
	}
	private boolean isCoordinateEmpty(int coordinate) {
		return coordinateNumberMap.get(coordinate) == -1;
	}
	
	@Override
	public void setFirstPlayerPosition(int firstPlayerPosition) {
		this.firstPlayerPosition = firstPlayerPosition;
	}

	@Override
	public void register(String username) {
		usernames.add(username);
		if (usernames.size() == 2) {
			setFirstPlayer();
			startGame();
		}
	}
	private void setFirstPlayer() {
		if (firstPlayerPosition != -1) {
			currentPlayer = usernames.get(firstPlayerPosition);
		} else {
			assignRandomFirstPlayer();	
		}
	}
	private void assignRandomFirstPlayer() {
		if (Math.random() > 0.5) {
			currentPlayer = usernames.get(0);
		} else {
			currentPlayer = usernames.get(1);
		}
	}
	private void startGame() {
		clearVariables();
		setInitialBoardState();
		setInitialAvailableNumbers();
		message = currentPlayer + " starts the game!";
		CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber,
				coordinateNumberMap, availableNumbers).build();
		messenger.updateState(state);
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
