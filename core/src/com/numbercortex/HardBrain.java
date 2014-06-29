package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class HardBrain implements Brain {
	
	private CortexPreferences preferences = CortexPreferences.getInstance();

	@Override
	public int calculateCoordinate(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		int chosenCoordinate = assignWinningCoordinateIfExistent(state, openCoordinates);
		if (noWinningCoordinateExists(chosenCoordinate)) {
			chosenCoordinate = assignRandomNumberFromList(openCoordinates);
		}
		return chosenCoordinate;
	}
	private int assignWinningCoordinateIfExistent(CortexState state, ArrayList<Integer> openCoordinates) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		int chosenCoordinate = -1;
		for (Integer openCoordinate : openCoordinates) {
			if (BrainUtilities.isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap, preferences)) {
				chosenCoordinate = openCoordinate;
				return chosenCoordinate;
			}
		}
		return chosenCoordinate;
	}
	private boolean noWinningCoordinateExists(int chosenCoordinate) {
		return chosenCoordinate == -1;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		int nextNumber = -1;
		for (Integer availableNumber : availableNumbers) {
			if (isSafeNumber(availableNumber, coordinateNumberMap)) {
				nextNumber = availableNumber;
			}
		}
		if (noSafeNumber(nextNumber)) {
			nextNumber = assignRandomNumberFromList(availableNumbers);
		}
		return nextNumber;
	}	
	private boolean isSafeNumber(int chosenNumber, Map<Integer, Integer> coordinateNumberMap) {
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		for (Integer openCoordinate : openCoordinates) {
			if (BrainUtilities.isWinningCoordinate(openCoordinate, chosenNumber, coordinateNumberMap, preferences)) {
				return false;
			}
		}
		return true;
	}
	private boolean noSafeNumber(int nextNumber) {
		return nextNumber == -1;
	}
	private int assignRandomNumberFromList(ArrayList<Integer> availableNumbers) {
		int nextNumber;
		int nextNumberPosition = (int) (Math.random() * availableNumbers.size());
		nextNumber = availableNumbers.get(nextNumberPosition);
		return nextNumber;
	}

}
