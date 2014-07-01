package com.numbercortex;

import java.util.ArrayList;
import java.util.Map;

public class ImpossibleBrain implements Brain {

	private String name = "Impossible AI";
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
			ArrayList<Integer> availableNumbers = state.getAvailableNumbers();
			ArrayList<Integer> safeCoordinates = BrainUtilities.getSafeCoordinatesIfExistent(chosenNumber,
					coordinateNumberMap, openCoordinates, availableNumbers);
			if (safeCoordinates.isEmpty()) {
				chosenCoordinate = BrainUtilities.assignRandomNumberFromList(openCoordinates);
			} else {
				chosenCoordinate = BrainUtilities.assignRandomNumberFromList(safeCoordinates);
			}
		}
		return chosenCoordinate;
	}

	@Override
	public int calculateNextNumber(CortexState state) {
		Map<Integer, Integer> coordinateNumberMap = state.getCoordinateNumberMap();
		ArrayList<Integer> availableNumbers = state.getAvailableNumbers();

		ArrayList<Integer> safeNumbers = BrainUtilities.getSafeNumbersIfExistent(coordinateNumberMap, availableNumbers);
		int nextNumber;
		if (safeNumbers.isEmpty()) {
			nextNumber = BrainUtilities.assignRandomNumberFromList(availableNumbers);
		} else {
			int maxPoints = 0;
			ArrayList<Integer> bestSafeNumberList = new ArrayList<Integer>();
			ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
			for (Integer safeNumber : safeNumbers) {
				int points = 0;
				availableNumbers.remove(Integer.valueOf(safeNumber));
				for (Integer openCoordinate : openCoordinates) {
					coordinateNumberMap.put(openCoordinate, safeNumber);
					for (Integer availableNumber : availableNumbers) {
						ArrayList<Integer> newOpenCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
						int chosenCoordinate = BrainUtilities.assignWinningCoordinateIfExistent(availableNumber, coordinateNumberMap,
									newOpenCoordinates);
						if (chosenCoordinate != -1) {
							points++;
						}
					}
					coordinateNumberMap.put(openCoordinate, -1);
				}
				availableNumbers.add(safeNumber);
				if (points > maxPoints) {
					maxPoints = points;
					bestSafeNumberList.clear();
					bestSafeNumberList.add(safeNumber);
				} else if (points == maxPoints) {
					bestSafeNumberList.add(safeNumber);
				}
			}
			System.out.println(bestSafeNumberList.size() + "/" + safeNumbers.size() + " not filtered with max points: " + maxPoints + ".");
			for (Integer integer : bestSafeNumberList) {
				System.out.print(integer + ", ");
				System.out.println();
			}
			nextNumber = BrainUtilities.assignRandomNumberFromList(bestSafeNumberList);
		}
		return nextNumber;
	}

}
