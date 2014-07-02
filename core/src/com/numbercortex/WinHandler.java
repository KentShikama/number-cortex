package com.numbercortex;

import java.util.Map;

public class WinHandler {

	private WinHandler() {}

	public static int[] handleWinningBoard(Map<Integer, Integer> coordinateNumberMap, GameSettings settings) {
		int[][] translatedCoordinateNumberMap = translateCoordinates(coordinateNumberMap);
		int[] winningSet;
		winningSet = checkAndHandleHorizontals(translatedCoordinateNumberMap, settings);
		if (winningSet != null) {
			return winningSet;
		}
		winningSet = checkAndHandleVerticals(translatedCoordinateNumberMap, settings);
		if (winningSet != null) {
			return winningSet;
		}
		if (settings.isDiagonals()) {
			winningSet = checkAndHandleLeftDiagonal(translatedCoordinateNumberMap, settings);
			if (winningSet != null) {
				return winningSet;
			}
			winningSet = checkAndHandleRightDiagonal(translatedCoordinateNumberMap, settings);
			if (winningSet != null) {
				return winningSet;
			}
		}
		if (settings.isFourSquare()) {
			winningSet = checkAndHandleFourSquare(translatedCoordinateNumberMap, settings);
			if (winningSet != null) {
				return winningSet;
			}
		}
		return null;
	}
	private static int[][] translateCoordinates(Map<Integer, Integer> coordinateNumberMap) {
		int numberOfRows = (int) Math.sqrt(coordinateNumberMap.size());
		int[][] translatedCoordinateNumberMap = new int[numberOfRows][numberOfRows];
		for (int i = 0; i < numberOfRows * numberOfRows; i++) {
			translatedCoordinateNumberMap[i / numberOfRows][i % numberOfRows] = coordinateNumberMap.get(i);
		}
		return translatedCoordinateNumberMap;
	}

	private static int[] checkAndHandleHorizontals(int[][] translatedCoordinateNumberMap, GameSettings settings) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		for (int row = 0; row < numberOfRows; row++) {
			int[] set = translatedCoordinateNumberMap[row];
			if (isBingo(set, settings)) {
				return set;
			}
		}
		return null;
	}
	private static int[] checkAndHandleVerticals(int[][] translatedCoordinateNumberMap, GameSettings settings) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		for (int column = 0; column < numberOfRows; column++) {
			int[] set = new int[numberOfRows];
			for (int i = 0; i < numberOfRows; i++) {
				set[i] = translatedCoordinateNumberMap[i][column];
			}
			if (isBingo(set, settings)) {
				return set;
			}
		}
		return null;
	}
	private static int[] checkAndHandleLeftDiagonal(int[][] translatedCoordinateNumberMap, GameSettings settings) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		int[] leftDiagonalSet = new int[numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			leftDiagonalSet[i] = translatedCoordinateNumberMap[i][i];
		}
		if (isBingo(leftDiagonalSet, settings)) {
			return leftDiagonalSet;
		}
		return null;
	}
	private static int[] checkAndHandleRightDiagonal(int[][] translatedCoordinateNumberMap, GameSettings settings) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		int[] rightDiagonalSet = new int[numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			rightDiagonalSet[i] = translatedCoordinateNumberMap[numberOfRows - 1 - i][i];
		}
		if (isBingo(rightDiagonalSet, settings)) {
			return rightDiagonalSet;
		}
		return null;
	}
	private static int[] checkAndHandleFourSquare(int[][] translatedCoordinateNumberMap, GameSettings settings) {
		int numberOfRows = translatedCoordinateNumberMap.length;
		int[] set = new int[4];
		for (int row = 0; row < numberOfRows - 1; row++) {
			for (int column = 0; column < numberOfRows - 1; column++) {
				set[0] = translatedCoordinateNumberMap[row][column];
				set[1] = translatedCoordinateNumberMap[row + 1][column];
				set[2] = translatedCoordinateNumberMap[row][column + 1];
				set[3] = translatedCoordinateNumberMap[row + 1][column + 1];
				if (isBingo(set, settings)) {
					return set;
				}
			}
		}
		return null;
	}

	private static boolean isBingo(int[] set, GameSettings settings) {
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
		if (settings.isMiddleExtreme() && isValidMiddleExtreme(set)) {
			return true;
		}
		return false;
	}
	private static boolean isValidEvenOdd(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (set[i] % 2 != set[i + 1] % 2) {
				return false;
			}
		}
		return true;
	}
	private static boolean isValidSingleDouble(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (set[i] / 10 != set[i + 1] / 10) {
				return false;
			}
		}
		return true;
	}
	private static boolean isValidPrimeComposite(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (isPrime(set[i]) != isPrime(set[i + 1])) {
				return false;
			}
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
	private static boolean isValidMiddleExtreme(int[] set) {
		for (int i = 0; i < set.length - 1; i++) {
			if (isMiddle(set[i]) != isMiddle(set[i + 1])) {
				return false;
			}
		}
		return true;
	}
	private static boolean isMiddle(int i) {
		if (i >= 5 && i <= 12) {
			return true;
		} else {
			return false;
		}
	}
}
