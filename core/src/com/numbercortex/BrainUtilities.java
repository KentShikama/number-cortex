package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class BrainUtilities {

	private GameSettings settings;

	public BrainUtilities(GameSettings settings) {
		this.settings = settings;
	}

	public ArrayList<Integer> getOpenCoordinates(Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : coordinateNumberMap.entrySet()) {
			if (entry.getValue() == -1) {
				int openCoordinate = entry.getKey();
				openCoordinates.add(openCoordinate);
			}
		}
		return openCoordinates;
	}

	public int assignWinningCoordinateIfExistent(int chosenNumber, Map<Integer, Integer> coordinateNumberMap,
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

	public ArrayList<Integer> getSafeCoordinatesIfExistent(int chosenNumber,
			Map<Integer, Integer> coordinateNumberMap, ArrayList<Integer> openCoordinates,
			ArrayList<Integer> availableNumbers) {
		ArrayList<Integer> safeCoordinates = new ArrayList<Integer>();
		for (Integer openCoordinate : openCoordinates) {
			coordinateNumberMap.put(openCoordinate, chosenNumber);
			ArrayList<Integer> safeNumbers = getSafeNumbersIfExistent(coordinateNumberMap, availableNumbers);
			coordinateNumberMap.put(openCoordinate, -1);
			if (!safeNumbers.isEmpty()) {
				safeCoordinates.add(openCoordinate);
			}
		}
		return safeCoordinates;
	}

	public ArrayList<Integer> getSafeNumbersIfExistent(Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> availableNumbers) {
		ArrayList<Integer> safeNumbers = new ArrayList<Integer>();
		for (Integer availableNumber : availableNumbers) {
			if (isSafeNumber(availableNumber, coordinateNumberMap)) {
				safeNumbers.add(availableNumber);
			}
		}
		return safeNumbers;
	}
	public boolean isSafeNumber(int chosenNumber, Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = getOpenCoordinates(coordinateNumberMap);
		for (Integer openCoordinate : openCoordinates) {
			if (isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap)) {
				return false;
			}
		}
		return true;
	}

	public boolean isWinningCoordinate(int coordinate, int number, Map<Integer, Integer> coordinateNumberMap) {
		coordinateNumberMap.put(coordinate, number);
		int[] winningValues = WinHandler.handleWinningBoard(coordinateNumberMap, settings);
		coordinateNumberMap.put(coordinate, -1);
		if (winningValues != null) {
			return true;
		}
		return false;
	}

	public int assignRandomNumberFromList(ArrayList<Integer> numberList) {
		int randomNumberPosition = (int) (Math.random() * numberList.size());
		int randomNumber = numberList.get(randomNumberPosition);
		return randomNumber;
	}
}
