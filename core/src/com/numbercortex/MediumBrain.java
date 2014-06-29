package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class MediumBrain implements Brain {
	
	private CortexPreferences preferences = CortexPreferences.getInstance();

	@Override
	public int calculateCoordinate(CortexState state) {
		ArrayList<Integer> openCoordinates = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : state.getCoordinateNumberMap().entrySet()) {
			if (entry.getValue() == -1) {
				int openCoordinate = entry.getKey();
				openCoordinates.add(openCoordinate);
			}
		}
		int chosenCoordinate = assignWinningCoordinateIfExistent(state, openCoordinates);
		if (noWinningCoordinateExists(chosenCoordinate)) {
			chosenCoordinate = assignRandomCoordinate(openCoordinates);
		}
		return chosenCoordinate;
	}
	private int assignWinningCoordinateIfExistent(CortexState state, ArrayList<Integer> openCoordinates) {
		int chosenNumber = state.getChosenNumber();
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		int chosenCoordinate = -1;
		for (Integer openCoordinate : openCoordinates) {
			coordinateNumberMap.put(openCoordinate, chosenNumber);
			int[] winningCoordinates = WinHandler.handleWinningBoard(coordinateNumberMap, preferences);
			coordinateNumberMap.replace(openCoordinate, -1);
			if (winningCoordinates != null) {
				chosenCoordinate = openCoordinate;
				break;
			}
		}
		return chosenCoordinate;
	}
	private boolean noWinningCoordinateExists(int chosenCoordinate) {
		return chosenCoordinate == -1;
	}
	private int assignRandomCoordinate(ArrayList<Integer> openCoordinates) {
		int chosenCoordinate;
		int chosenCoordinatePosition = (int) (Math.random() * openCoordinates.size());
		chosenCoordinate = openCoordinates.get(chosenCoordinatePosition);
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
		int nextNumberPosition = (int) (Math.random() * availableNumbers.size());
		int nextNumber = availableNumbers.get(nextNumberPosition);
		return nextNumber;
	}

}
