package com.numbercortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultCortexModel implements CortexModel {
	
	private String currentPlayer;
	private ArrayList<Integer> availableNumbers =  new ArrayList<Integer>();
	private Map<Integer, Integer> coordinateNumberMap = new HashMap<Integer, Integer>();
	private String message;
	private int chosenNumber = -1;

	private ArrayList<String> usernames = new ArrayList<String>();

	private static final int BOARD_SIZE = 16;
	
	private String winner; // Optional
	private int[] winningCoordinates; // Optional
	
	private Exchangeable listener;
	
	public DefaultCortexModel(Exchangeable listener) {
		this.listener = listener;
	}

	@Override
	public void chooseNumber(String playerName, int nextNumber) {
		if (isChosenNumberValid(playerName, nextNumber)) {
			chosenNumber = nextNumber;
			availableNumbers.remove(Integer.valueOf(nextNumber));
			currentPlayer = (currentPlayer == usernames.get(0) ? usernames.get(1) : usernames.get(0));
			message = currentPlayer;
			CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber, coordinateNumberMap, availableNumbers).build();
			listener.updateState(state);
		}
	}

	@Override
	public void placeNumber(String playerName, int coordinate, int number) {
		if (isNumberPlacementValid(playerName, coordinate, number)) {
			for (Map.Entry<Integer, Integer> entry: coordinateNumberMap.entrySet()) {
				if (entry.getValue() == number) {
					coordinateNumberMap.put(entry.getKey(), -1);
				}
			}
			coordinateNumberMap.put(coordinate, number);
			chosenNumber = -1;
			checkIfWinningBoard();
			CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber, coordinateNumberMap, availableNumbers).build();
			listener.updateState(state);
		}
	}

	@Override
	public void register(String username) {
		usernames.add(username);
		if (usernames.size() == 2) {
			startGame();
		}
	}

	private void checkIfWinningBoard() {
		
	}

	private boolean isAvailable(int nextNumber) {
		for (Integer number : availableNumbers) {
			if (nextNumber == number) {
				return true;
			}
		}
		return false;
	}

	private boolean isChosenNumberValid(String playerName, int nextNumber) {
		return playerName == currentPlayer && chosenNumber == -1 && isAvailable(nextNumber);
	}

	private boolean isNumberPlacementValid(String playerName, int coordinate,
			int number) {
		return playerName == currentPlayer && coordinateNumberMap.get(coordinate) == -1;
	}

	private void setFirstPlayer() {
		if (Math.random() > 0.5) {
			currentPlayer = usernames.get(0);
		} else {
			currentPlayer = usernames.get(1);
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

	private void setInitialBoardState() {
		coordinateNumberMap.clear();
		for (int i = 0; i < BOARD_SIZE; i++) {
			coordinateNumberMap.put(i, -1);
		}
	}

	private void startGame() {
		// clearVariables();
		setInitialBoardState();
		setInitialAvailableNumbers();
		setFirstPlayer();
		message = currentPlayer + " starts the game!";
		CortexState state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber, coordinateNumberMap, availableNumbers).build();
		listener.updateState(state);
	}

}
