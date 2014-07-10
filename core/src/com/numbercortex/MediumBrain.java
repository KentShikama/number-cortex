package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class MediumBrain implements Brain {

	private String name = "Medium AI";
	private BrainUtilities utility;

	public MediumBrain(GameSettings settings) {
		this.utility = new BrainUtilities(settings);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);

		int chosenCoordinate = utility.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap,
				openCoordinates);
		if (chosenCoordinate == -1) {
			chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
		}
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();

		ArrayList<Integer> safeNumbers = utility.getSafeNumbersIfExistent(coordinateNumberMap, availableNumbers);
		int nextNumber;
		if (safeNumbers.isEmpty()) {
			nextNumber = utility.assignRandomNumberFromList(availableNumbers);
		} else {
			nextNumber = utility.assignRandomNumberFromList(safeNumbers);
		}
		return nextNumber;
	}

}
