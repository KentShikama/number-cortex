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
			ArrayList<Integer> openCoordinates = BrainUtilities.getOpenCoordinates(coordinateNumberMap);
			if (openCoordinates.size() > 9) {
				nextNumber = BrainUtilities.assignRandomNumberFromList(safeNumbers);
				return nextNumber;
			}
			int maxPoints = 0;
			ArrayList<Integer> bestSafeNumberList = new ArrayList<Integer>();
			for (Integer safeNumber : safeNumbers) {
				int points = 0;
				availableNumbers.remove(Integer.valueOf(safeNumber));
				for (Integer openCoordinate : openCoordinates) {
					coordinateNumberMap.put(openCoordinate, safeNumber); // Opponent places your number
					ArrayList<Integer> safeNumbersOpponentCanChoose = BrainUtilities.getSafeNumbersIfExistent(
							coordinateNumberMap, availableNumbers);
					if (safeNumbersOpponentCanChoose.isEmpty()) {
						// Your opponent will not choose this coordinate
					} else {
						ArrayList<Integer> newAvailableNumbers = (ArrayList<Integer>) safeNumbersOpponentCanChoose
								.clone();
						for (Integer possibleNextNumber : safeNumbersOpponentCanChoose) { // Opponent chooses your number
							boolean safe = false;
							newAvailableNumbers.remove(Integer.valueOf(possibleNextNumber));
							ArrayList<Integer> newOpenCoordinates = BrainUtilities
									.getOpenCoordinates(coordinateNumberMap);
							for (Integer newOpenCoordinate : newOpenCoordinates) {
								coordinateNumberMap.put(newOpenCoordinate, possibleNextNumber); // You place number
								ArrayList<Integer> list = BrainUtilities.getSafeNumbersIfExistent(coordinateNumberMap,
										newAvailableNumbers); // Check if you can give a safe number
								coordinateNumberMap.put(newOpenCoordinate, -1);
								if (!list.isEmpty()) { // If you can give a safe number...then that number is good
									safe = true;
									break;
								}
							}
							newAvailableNumbers.add(Integer.valueOf(possibleNextNumber));
							if (safe) { // Safe for you if opponent decides to give you this possibleNextNumber
								points++;
							}
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
			if (bestSafeNumberList.isEmpty()) {
				nextNumber = BrainUtilities.assignRandomNumberFromList(safeNumbers);
			} else {
				nextNumber = BrainUtilities.assignRandomNumberFromList(bestSafeNumberList);
			}
		}
		return nextNumber;
	}

}
