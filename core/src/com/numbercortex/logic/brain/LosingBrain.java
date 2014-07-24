package com.numbercortex.logic.brain;

import java.util.ArrayList;
import java.util.Map;

import com.numbercortex.CortexState;
import com.numbercortex.GameSettings;
import com.numbercortex.logic.BoardUtilities;

class LosingBrain implements Brain {

	private String name = " Losing AI ";
	private BrainCalculator utility;

	LosingBrain(GameSettings settings) {
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
		ArrayList<Integer> nonWinningCoordinates = utility.getNonWinningCoordinatesIfExistent(chosenNumber,
				coordinateNumberMap, openCoordinates);
		if (nonWinningCoordinates.isEmpty()) {
			return utility.assignRandomNumberFromList(openCoordinates);
		} else {
			return utility.assignRandomNumberFromList(nonWinningCoordinates);
		}
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		ArrayList<Integer> safeNumbers = utility.getSafeNumbersIfExistent(coordinateNumberMap, availableNumbers);
		if (!safeNumbers.isEmpty()) {
			ArrayList<Integer> unsafeNumbers = (ArrayList<Integer>) availableNumbers.clone();
			unsafeNumbers.removeAll(safeNumbers);
			if (unsafeNumbers.isEmpty()) {
				return utility.assignRandomNumberFromList(availableNumbers);
			} else {
				return utility.assignRandomNumberFromList(unsafeNumbers);
			}
		} else {
			return utility.assignRandomNumberFromList(availableNumbers);
		}
	}

}
