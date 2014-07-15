package com.numbercortex.logic.brain;

import java.util.ArrayList;
import java.util.Map;

import com.numbercortex.GameSettings;
import com.numbercortex.logic.BoardUtilities;
import com.numbercortex.logic.WinHandler;

public class BrainCalculator {

	private WinHandler winHandler;

	BrainCalculator(GameSettings settings) {
		this.winHandler = new WinHandler(settings);
	}

	int assignWinningCoordinateIfExistent(int chosenNumber, Map<Integer, Integer> coordinateNumberMap,
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

	ArrayList<Integer> getSafeCoordinatesIfExistent(int chosenNumber, Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> openCoordinates, ArrayList<Integer> availableNumbers) {
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

	ArrayList<Integer> getSafeNumbersIfExistent(Map<Integer, Integer> coordinateNumberMap,
			ArrayList<Integer> availableNumbers) {
		ArrayList<Integer> safeNumbers = new ArrayList<Integer>();
		for (Integer availableNumber : availableNumbers) {
			if (isSafeNumber(availableNumber, coordinateNumberMap)) {
				safeNumbers.add(availableNumber);
			}
		}
		return safeNumbers;
	}
	private boolean isSafeNumber(int chosenNumber, Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		for (Integer openCoordinate : openCoordinates) {
			if (isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap)) {
				return false;
			}
		}
		return true;
	}

	boolean isWinningCoordinate(int coordinate, int number, Map<Integer, Integer> coordinateNumberMap) {
		coordinateNumberMap.put(coordinate, number);
		int[] winningValues = winHandler.handleWinningBoard(coordinateNumberMap);
		coordinateNumberMap.put(coordinate, -1);
		if (winningValues != null) {
			return true;
		}
		return false;
	}

	int assignRandomNumberFromList(ArrayList<Integer> numberList) {
		int randomNumberPosition = (int) (Math.random() * numberList.size());
		int randomNumber = numberList.get(randomNumberPosition);
		return randomNumber;
	}
}
