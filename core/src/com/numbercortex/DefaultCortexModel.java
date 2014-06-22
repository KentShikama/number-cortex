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
	private static final int LENGTH = (int) Math.sqrt(BOARD_SIZE);
	
	private String winner; // Optional
	private int[] winningCoordinates; // Optional
	
	private Exchangeable listener;
	private CortexPreferences preferences;
	
	public DefaultCortexModel(Exchangeable listener, CortexPreferences preferences) {
		this.listener = listener;
		this.preferences = preferences;
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
			handleWinningBoard();
			CortexState state;
			if (winningCoordinates != null) {
				state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber, coordinateNumberMap, availableNumbers).win(currentPlayer, winningCoordinates).build();
			} else {
				state = new CortexState.CortexStateBuilder(message, currentPlayer, usernames, chosenNumber, coordinateNumberMap, availableNumbers).build();
			}
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

	private void handleWinningBoard() {
		int[][] translatedCoordinateNumberMap = translateCoordinates(coordinateNumberMap);
		if (checkAndHandleHorizontals(translatedCoordinateNumberMap)) {
			return;
		}
		if (checkAndHandleVerticals(translatedCoordinateNumberMap)) {
			return;
		}
		if (checkAndHandleLeftDiagonal(translatedCoordinateNumberMap)) {
			return;
		}
		if (preferences.isDiagonalsEnabled() && checkAndHandleRightDiagonal(translatedCoordinateNumberMap)) {
			return;
		}
		if (preferences.isFourSquareEnabled() && checkAndHandleFourSquare(translatedCoordinateNumberMap)) {
			return;
		}
	}

	private boolean checkAndHandleHorizontals(int[][] translatedCoordinateNumberMap) {
		for (int row = 0; row < LENGTH; row++) {
			int[] set = translatedCoordinateNumberMap[row];
			if (isBingo(set)) {
				winningCoordinates = set;
				return true;
			}
		}
		return false;
	}

	private boolean checkAndHandleVerticals(int[][] translatedCoordinateNumberMap) {
		for (int column = 0; column < LENGTH; column++) {
			int[] set = new int[LENGTH];
			for (int i = 0; i < LENGTH; i++) {
				set[i] = translatedCoordinateNumberMap[i][column];
			}
			if (isBingo(set)) {
				winningCoordinates = set;
				return true;
			}
		}
		return false;
	}

	private boolean checkAndHandleLeftDiagonal(
			int[][] translatedCoordinateNumberMap) {
		int[] leftDiagonalSet = new int[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			leftDiagonalSet[i] = translatedCoordinateNumberMap[i][i]; 
		}
		if (isBingo(leftDiagonalSet)) {
			winningCoordinates = leftDiagonalSet;
			return true;
		}
		return false;
	}

	private boolean checkAndHandleRightDiagonal(int[][] translatedCoordinateNumberMap) {
		int[] rightDiagonalSet = new int[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			rightDiagonalSet[i] = translatedCoordinateNumberMap[3 - i][i];
		}
		if (isBingo(rightDiagonalSet)) {
			winningCoordinates = rightDiagonalSet;
			return true;
		}
		return false;
	}

	private boolean checkAndHandleFourSquare(int[][] translatedCoordinateNumberMap) {
		int[] set = new int[LENGTH];
		for (int row = 0; row < LENGTH - 1; row++) {
			for (int column = 0; column < LENGTH - 1; column++) {
				set[0] = translatedCoordinateNumberMap[row][column];
				set[1] = translatedCoordinateNumberMap[row + 1][column];
				set[2] = translatedCoordinateNumberMap[row][column + 1];
				set[3] = translatedCoordinateNumberMap[row + 1][column + 1];
				if (isBingo(set)) {
					winningCoordinates = set;
					return true;
				}
			}
		}
		return false;
	}

	private boolean isBingo(int[] set) {
		for (int i = 0; i < set.length; i++) {
			if (set[i] == -1) {
				return false;
			}
		}
		if (isValidEvenOdd(set)) {
			return true;
		}
		if (isValidSingleDouble(set)) {
			return true;
		}
		if (isValidPrimeComposite(set)) {
			return true;
		}
		if (isValidMiddleExtreme(set)) {
			return true;
		}
		return false;
	}

	private boolean isValidEvenOdd(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (set[i] % 2 != set[i + 1] % 2) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isValidSingleDouble(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (set[i] / 10 != set[i + 1] / 10) {
				return false;
			}
		}
		return true;
	}

	private boolean isValidPrimeComposite(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (isPrime(set[i]) != isPrime(set[i + 1])) {
				return false;
			}
		}
		return true;
	}

	private boolean isPrime(int number) {
		if (number == 1 || number == 2) {
			return true;
		}
		if (number % 2 == 0) {
			return false;
		}
		for (int i = 3; i*i <= number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isValidMiddleExtreme(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (isMiddle(set[i]) != isMiddle(set[i + 1])) {
				return false;
			}
		}
		return true;
	}

	private boolean isMiddle(int i) {
		if (i >= 5 && i <= 12) {
			return true;
		} else {
			return false;
		}
	}

	private int[][] translateCoordinates(Map<Integer, Integer> coordinateNumberMap2) {
		int[][] translatedCoordinateNumberMap = new int[LENGTH][LENGTH];
		for (int i = 0; i < BOARD_SIZE; i++) {
			translatedCoordinateNumberMap[i / 4][i % 4] = coordinateNumberMap.get(i);
		}
		return translatedCoordinateNumberMap;
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
