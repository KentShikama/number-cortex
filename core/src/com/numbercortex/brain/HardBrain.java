package com.numbercortex.brain;

import java.util.ArrayList;
import java.util.Map;

import com.numbercortex.logic.BoardUtilities;
import com.numbercortex.logic.CortexState;
import com.numbercortex.logic.GameSettings;

public class HardBrain implements Brain {

	private String name = "Hard AI";
	private BrainCalculator utility;

	HardBrain(GameSettings settings) {
		this.utility = new BrainCalculator(settings);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);

		int chosenCoordinate = utility.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap,
				openCoordinates);
		if (chosenCoordinate == -1) {
			ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
			ArrayList<Integer> safeCoordinates = utility.getSafeCoordinatesIfExistent(chosenNumber,
					coordinateNumberMap, openCoordinates, availableNumbers);
			if (safeCoordinates.isEmpty()) {
				chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
			} else {
				chosenCoordinate = utility.assignRandomNumberFromList(safeCoordinates);
			}
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
