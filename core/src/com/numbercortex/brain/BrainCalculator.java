package com.numbercortex.brain;

import java.util.ArrayList;
import java.util.Map;

import com.numbercortex.BoardUtilities;
import com.numbercortex.GameSettings;
import com.numbercortex.WinHandler;

public class BrainCalculator {

	private WinHandler winHandler;

	public BrainCalculator(GameSettings settings) {
		this.winHandler = new WinHandler(settings);
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

	public ArrayList<Integer> getSafeCoordinatesIfExistent(int chosenNumber, Map<Integer, Integer> coordinateNumberMap,
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
	private boolean isSafeNumber(int chosenNumber, Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		for (Integer openCoordinate : openCoordinates) {
			if (isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap)) {
				return false;
			}
		}
		return true;
	}

	public boolean isWinningCoordinate(int coordinate, int number, Map<Integer, Integer> coordinateNumberMap) {
		coordinateNumberMap.put(coordinate, number);
		int[] winningValues = winHandler.handleWinningBoard(coordinateNumberMap);
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
