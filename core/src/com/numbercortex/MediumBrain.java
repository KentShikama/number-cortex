package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class MediumBrain implements Brain {
	
	private String name = "Medium Computer";
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
			chosenCoordinate = BrainUtilities.assignRandomNumberFromList(openCoordinates);
		}
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		return BrainUtilities.assignRandomNumberFromList(availableNumbers);
	}

}
