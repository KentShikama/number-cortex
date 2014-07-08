package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class EasyBrain implements Brain {

	private BrainUtilities utility;
	private String name = "Easy AI";

	public EasyBrain(GameSettings settings) {
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
		ArrayList<Integer> openCoordinates = utility.getOpenCoordinates(coordinateNumberMap);

		int chosenCoordinate = utility.assignWinningCoordinateIfExistent(chosenNumber, coordinateNumberMap,
				openCoordinates);
		if (chosenCoordinate == -1) {
			chosenCoordinate = utility.assignRandomNumberFromList(openCoordinates);
		}
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		return utility.assignRandomNumberFromList(availableNumbers);
	}

}
