package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class EasyBrain implements Brain {
	
	private String name = "Easy Computer";
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
		int chosenCoordinate = BrainUtilities.assignRandomNumberFromList(openCoordinates);
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		return BrainUtilities.assignRandomNumberFromList(availableNumbers);
	}

}
