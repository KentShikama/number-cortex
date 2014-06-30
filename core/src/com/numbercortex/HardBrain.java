package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class HardBrain implements Brain {

	private String name = "Hard Computer";
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);

		int chosenCoordinate = BrainUtilities.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap,
				openCoordinates);
		if (chosenCoordinate == -1) {
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
