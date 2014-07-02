package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class RandomBrain implements Brain {

	private String name = "Random AI";
	private BrainUtilities utility;

	public RandomBrain(GameSettings settings) {
		this.utility = new BrainUtilities(settings);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = utility.getOpenCoordinates(coordinateNumberMap);
		int chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		return utility.assignRandomNumberFromList(availableNumbers);
	}

}
