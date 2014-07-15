package com.numbercortex.brain;

import java.util.ArrayList;
import java.util.Map;

import com.numbercortex.logic.BoardUtilities;
import com.numbercortex.logic.CortexState;
import com.numbercortex.logic.GameSettings;

public class RandomBrain implements Brain {

	private String name = "Random AI";
	private BrainCalculator utility;

	RandomBrain(GameSettings settings) {
		this.utility = new BrainCalculator(settings);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int calculateCoordinate(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> openCoordinates = BoardUtilities.getOpenCoordinates(coordinateNumberMap);
		int chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		return utility.assignRandomNumberFromList(availableNumbers);
	}

}
