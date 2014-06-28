package com.numbercortex;

import java.util.Map;

public class WinHandler {
	
	private static final int LENGTH = (int) Math.sqrt(DefaultCortexModel.BOARD_SIZE);

	public static int[] handleWinningBoard(Map<Integer, Integer> coordinateNumberMap, CortexPreferences preferences) {
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
		if (preferences.isDiagonalsEnabled()) {
			winningSet = checkAndHandleLeftDiagonal(translatedCoordinateNumberMap);
			if (winningSet != null) {
				return winningSet;
			}
			winningSet = checkAndHandleRightDiagonal(translatedCoordinateNumberMap);
			if (winningSet != null) {
				return winningSet;
			}
		}
		if (preferences.isFourSquareEnabled()) {
			winningSet = checkAndHandleFourSquare(translatedCoordinateNumberMap);
			if (winningSet != null) {
				return winningSet;
			}
		}
		return null;
	}
	private static int[][] translateCoordinates(Map<Integer, Integer> coordinateNumberMap) {
		int[][] translatedCoordinateNumberMap = new int[LENGTH][LENGTH];
		for (int i = 0; i < LENGTH * LENGTH; i++) {
			translatedCoordinateNumberMap[i / 4][i % 4] = coordinateNumberMap.get(i);
		}
		return translatedCoordinateNumberMap;
	}
	private static int[] checkAndHandleHorizontals(int[][] translatedCoordinateNumberMap) {
		for (int row = 0; row < LENGTH; row++) {
			int[] set = translatedCoordinateNumberMap[row];
			if (isBingo(set)) {
				return set;
			}
		}
		return null;
	}
	private static int[] checkAndHandleVerticals(int[][] translatedCoordinateNumberMap) {
		for (int column = 0; column < LENGTH; column++) {
			int[] set = new int[LENGTH];
			for (int i = 0; i < LENGTH; i++) {
				set[i] = translatedCoordinateNumberMap[i][column];
			}
			if (isBingo(set)) {
				 return set;
			}
		}
		return null;
	}
	private static int[] checkAndHandleLeftDiagonal(int[][] translatedCoordinateNumberMap) {
		int[] leftDiagonalSet = new int[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			leftDiagonalSet[i] = translatedCoordinateNumberMap[i][i]; 
		}
		if (isBingo(leftDiagonalSet)) {
			return leftDiagonalSet;
		}
		return null;
	}
	private static int[] checkAndHandleRightDiagonal(int[][] translatedCoordinateNumberMap) {
		int[] rightDiagonalSet = new int[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			rightDiagonalSet[i] = translatedCoordinateNumberMap[3 - i][i];
		}
		if (isBingo(rightDiagonalSet)) {
			return rightDiagonalSet;
		}
		return null;
	}
	private static int[] checkAndHandleFourSquare(int[][] translatedCoordinateNumberMap) {
		int[] set = new int[LENGTH];
		for (int row = 0; row < LENGTH - 1; row++) {
			for (int column = 0; column < LENGTH - 1; column++) {
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
	private static boolean isBingo(int[] set) {
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
		for (int i = 3; i*i <= number; i++) {
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
