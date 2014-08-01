package com.numbercortex.logic;

import java.util.Map;

import com.numbercortex.GameSettings;

public class WinHandler {

	private GameSettings settings;
	private String winningAttribute;

	public WinHandler(GameSettings settings) {
		this.settings = settings;
	}

	public String getWinningAttriute() {
		return winningAttribute;
	}

	public int[] handleWinningBoard(Map<Integer, Integer> coordinateNumberMap) {
		int[][] translatedCoordinateNumberMap = translateCoordinates(coordinateNumberMap);
		int[] winningSet;
		winningSet = checkAndHandleHorizontals(translatedCoordinateNumberMap);
		if (winningSet != null) {
			return winningSet;
		}
		winningSet = checkAndHandleVerticals(translatedCoordinateNumberMap);
		if (winningSet != null) {
			return winningSet;
		}
		if (settings.isDiagonals()) {
			winningSet = checkAndHandleLeftDiagonal(translatedCoordinateNumberMap);
			if (winningSet != null) {
				return winningSet;
			}
			winningSet = checkAndHandleRightDiagonal(translatedCoordinateNumberMap);
			if (winningSet != null) {
				return winningSet;
			}
		}
		if (settings.isFourSquare()) {
			winningSet = checkAndHandleFourSquare(translatedCoordinateNumberMap);
			if (winningSet != null) {
				return winningSet;
			}
		}
		return null;
	}
	private int[][] translateCoordinates(Map<Integer, Integer> coordinateNumberMap) {
		int numberOfRows = (int) Math.sqrt(coordinateNumberMap.size());
		int[][] translatedCoordinateNumberMap = new int[numberOfRows][numberOfRows];
		for (int i = 0; i < numberOfRows * numberOfRows; i++) {
			translatedCoordinateNumberMap[i / numberOfRows][i % numberOfRows] = coordinateNumberMap.get(i);
		}
		return translatedCoordinateNumberMap;
	}

	private int[] checkAndHandleHorizontals(int[][] translatedCoordinateNumberMap) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		for (int row = 0; row < numberOfRows; row++) {
			int[] set = translatedCoordinateNumberMap[row];
			if (isBingo(set)) {
				return set;
			}
		}
		return null;
	}
	private int[] checkAndHandleVerticals(int[][] translatedCoordinateNumberMap) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		for (int column = 0; column < numberOfRows; column++) {
			int[] set = new int[numberOfRows];
			for (int i = 0; i < numberOfRows; i++) {
				set[i] = translatedCoordinateNumberMap[i][column];
			}
			if (isBingo(set)) {
				return set;
			}
		}
		return null;
	}
	private int[] checkAndHandleLeftDiagonal(int[][] translatedCoordinateNumberMap) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		int[] leftDiagonalSet = new int[numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			leftDiagonalSet[i] = translatedCoordinateNumberMap[i][i];
		}
		if (isBingo(leftDiagonalSet)) {
			return leftDiagonalSet;
		}
		return null;
	}
	private int[] checkAndHandleRightDiagonal(int[][] translatedCoordinateNumberMap) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		int[] rightDiagonalSet = new int[numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			rightDiagonalSet[i] = translatedCoordinateNumberMap[numberOfRows - 1 - i][i];
		}
		if (isBingo(rightDiagonalSet)) {
			return rightDiagonalSet;
		}
		return null;
	}
	private int[] checkAndHandleFourSquare(int[][] translatedCoordinateNumberMap) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		int[] set = new int[4];
		for (int row = 0; row < numberOfRows - 1; row++) {
			for (int column = 0; column < numberOfRows - 1; column++) {
				set[0] = translatedCoordinateNumberMap[row][column];
				set[1] = translatedCoordinateNumberMap[row + 1][column];
				set[2] = translatedCoordinateNumberMap[row][column + 1];
				set[3] = translatedCoordinateNumberMap[row + 1][column + 1];
				if (isBingo(set)) {
					return set;
				}
			}
		}
		return null;
	}

	private boolean isBingo(int[] set) {
		for (int i = 0; i < set.length; i++) {
			if (set[i] == -1) {
				return false;
			}
		}
		if (settings.isEvenOdd() && isValidEvenOdd(set)) {
			return true;
		}
		if (settings.isSingleDouble() && isValidSingleDouble(set)) {
			return true;
		}
		if (settings.isPrimeComposite() && isValidPrimeComposite(set)) {
			return true;
		}
		if (settings.isMiddleEdge() && isValidMiddleEdge(set)) {
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
		if (set[0] % 2 == 0) {
			winningAttribute = "Evens";
		} else {
			winningAttribute = "Odds";
		}
		return true;
	}
	private boolean isValidSingleDouble(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (set[i] / 10 != set[i + 1] / 10) {
				return false;
			}
		}
		if (set[0] / 10 == 0) {
			winningAttribute = "Single Digits";
		} else {
			winningAttribute = "Double Digits";
		}
		return true;
	}
	private boolean isValidPrimeComposite(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (isPrime(set[i]) != isPrime(set[i + 1])) {
				return false;
			}
		}
		if (isPrime(set[0]) == true) {
			winningAttribute = "Primes";
		} else {
			winningAttribute = "Composites";
		}
		return true;
	}
	private static boolean isPrime(int number) {
		if (number == 1 || number == 2) {
			return true;
		}
		if (number % 2 == 0) {
			return false;
		}
		for (int i = 3; i * i <= number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}
	private boolean isValidMiddleEdge(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (isMiddle(set[i]) != isMiddle(set[i + 1])) {
				return false;
			}
		}
		if (isMiddle(set[0]) == true) {
			winningAttribute = "Middles - 5~13";
		} else {
			winningAttribute = "Edges - 1~4 & 14~17";
		}
		return true;
	}
	private static boolean isMiddle(int i) {
		if (i >= 5 && i <= 13) {
			return true;
		} else {
			return false;
		}
	}
}
