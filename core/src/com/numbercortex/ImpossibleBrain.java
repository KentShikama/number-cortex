package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class ImpossibleBrain implements Brain {
	
	private String name = "Impossible Computer";
	public String getName() {
		return name;
	}
	
	@Override
	public int calculateCoordinate(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		
		int chosenCoordinate = BrainUtilities.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap, openCoordinates);
		if (chosenCoordinate == -1) {
			// Check if there is a safe number that matches the chosenCoordinate instead of a random coordinate
			chosenCoordinate = BrainUtilities.assignRandomNumberFromList(openCoordinates);
		}
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();

		int nextNumber = BrainUtilities.assignSafeNumberIfExistent(coordinateNumberMap, availableNumbers);
		if (nextNumber == -1) {
			nextNumber = BrainUtilities.assignRandomNumberFromList(availableNumbers);
		}
		return nextNumber;
	}

}
