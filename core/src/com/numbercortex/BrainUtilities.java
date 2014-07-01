package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class BrainUtilities {

	private static CortexPreferences preferences = CortexPreferences.getInstance();

	private BrainUtilities() {}

	public static ArrayList<Integer> getOpenCoordinates(Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
			if (entry.getValue() == -1) {
				int openCoordinate = entry.getKey();
				openCoordinates.add(openCoordinate);
			}
		}
		return openCoordinates;
	}

	public static int assignWinningCoordinateIfExistent(int chosenNumber, Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> openCoordinates) {
		int chosenCoordinate = -1;
		for (Integer openCoordinate : openCoordinates) {
			if (isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap)) {
				chosenCoordinate = openCoordinate;
				return chosenCoordinate;
			}
		}
		return chosenCoordinate;
	}

	public static ArrayList<Integer> getSafeNumbersIfExistent(Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> availableNumbers) {
		ArrayList<Integer> safeNumbers = new ArrayList<Integer>();
		for (Integer availableNumber : availableNumbers) {
			if (isSafeNumber(availableNumber, coordinateNumberMap)) {
				safeNumbers.add(availableNumber);
			}
		}
		return safeNumbers;
	}
	public static boolean isSafeNumber(int chosenNumber, Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		for (Integer openCoordinate : openCoordinates) {
			if (isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isWinningCoordinate(int coordinate, int number, Map<Integer, Integer> coordinateNumberMap) {
		coordinateNumberMap.put(coordinate, number);
		int[] winningValues = WinHandler.handleWinningBoard(coordinateNumberMap, preferences);
		coordinateNumberMap.put(coordinate, -1);
		if (winningValues != null) {
			return true;
		}
		return false;
	}

	public static int assignRandomNumberFromList(ArrayList<Integer> numberList) {
		int randomNumberPosition = (int) (Math.random() * numberList.size());
		int randomNumber = numberList.get(randomNumberPosition);
		return randomNumber;
	}
}
